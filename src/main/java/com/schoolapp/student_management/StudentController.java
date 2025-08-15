package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository; // ✅ ADDED FOR MESSAGING

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadStudent(
            @RequestParam(value = "passportPhoto", required = false) MultipartFile passportPhoto,
            @RequestParam("studentData") String studentJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Student student = objectMapper.readValue(studentJson, Student.class);

            if (passportPhoto != null && !passportPhoto.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + passportPhoto.getOriginalFilename();
                Path path = Paths.get("uploads", fileName);
                Files.copy(passportPhoto.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                student.setPassportPhoto(fileName);
            }

            studentService.saveStudent(student);

            // ✅ Save users
            createStudentAndParentUsers(student);

            return ResponseEntity.ok("Student saved successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to save student: " + e.getMessage());
        }
    }

    private void createStudentAndParentUsers(Student student) {
        // Student account
        saveUserIfNotExists(student.getAdmissionNumber(), "STUDENT", student.getAdmissionNumber(), null);

        // Father's account
        if (student.getFatherPhone() != null && !student.getFatherPhone().trim().isEmpty()) {
            saveUserIfNotExists(student.getFatherPhone(), "PARENT", student.getAdmissionNumber(), null);
        }

        // Mother's account
        if (student.getMotherPhone() != null && !student.getMotherPhone().trim().isEmpty()) {
            saveUserIfNotExists(student.getMotherPhone(), "PARENT", student.getAdmissionNumber(), null);
        }
    }

    private void saveUserIfNotExists(String username, String role, String admissionNo, String employeeId) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(username);  // Password = username for now
            user.setRole(role);
            user.setAdmissionNo(admissionNo);
            user.setEmployeeId(employeeId);
            user.setStatus("ACTIVE");
            userRepository.save(user);
        }
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentService.updateStudent(id, updatedStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/latest-id")
    public Long getLastStudentId() {
        return studentService.getLatestStudentId();
    }

    @GetMapping("/api/students")
    public List<Student> getAllStudentsAlias() {
        return studentService.getAllStudents();
    }

    @GetMapping("/summary")
    public Map<String, Long> getStudentSummary() {
        List<Student> all = studentService.getAllStudents();

        long total = all.stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStudentStatus()))
                .count();

        long boys = all.stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStudentStatus()) &&
                        "Male".equalsIgnoreCase(s.getGender()))
                .count();

        long girls = all.stream()
                .filter(s -> "Active".equalsIgnoreCase(s.getStudentStatus()) &&
                        "Female".equalsIgnoreCase(s.getGender()))
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("total", total);
        result.put("boys", boys);
        result.put("girls", girls);

        return result;
    }

    // ✅ ADDED FOR MESSAGING — Fetch all active student phone numbers
    @GetMapping("/active-phones")
    public List<String> getActiveStudentPhones() {
        return studentRepository.findByStudentStatusIgnoreCase("Active").stream()
                .map(Student::getFatherPhone) // You can also collect both father & mother numbers if needed
                .filter(Objects::nonNull)
                .filter(phone -> !phone.trim().isEmpty())
                .distinct()
                .toList();
    }

    // ✅ NEW — Fetch student details by admission number (for Enter key search)
    @GetMapping("/by-adm/{admNo}")
    public ResponseEntity<Student> getStudentByAdmissionNo(@PathVariable String admNo) {
        return studentRepository.findByAdmissionNumber(admNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
