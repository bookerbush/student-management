// File: ParentAssignmentsController.java
package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/parent/assignments")
@CrossOrigin(origins = "*")
public class ParentAssignmentsController {

    @Autowired
    private AssignmentsRepository assignmentsRepository; // ✅ Using your existing repository name

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/list")
    public ResponseEntity<?> getAssignmentsForStudent(@RequestParam String studentid) {
        Optional<Student> studentOpt = studentRepository.findByAdmissionNumber(studentid);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOpt.get();
        String className = student.getClassEnrolled();
        String stream = student.getStream();

        if (className == null || stream == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Student class or stream missing"));
        }

        List<Assignments> assignmentsList = assignmentsRepository.findByClassNameAndStream(className, stream);

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Assignments a : assignmentsList) {
            Map<String, Object> assignmentData = new HashMap<>();
            assignmentData.put("assigno", a.getAssigno());
            assignmentData.put("subject", a.getSubject());
            assignmentData.put("date", a.getDate());
            assignmentData.put("teacher", a.getTeacher());
            assignmentData.put("worktodo", Base64.getEncoder().encodeToString(a.getWorktodo())); // For frontend file rendering
            responseList.add(assignmentData);
        }

        return ResponseEntity.ok(responseList);
    }
}
