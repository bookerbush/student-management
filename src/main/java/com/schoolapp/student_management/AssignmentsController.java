package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentsController {

    @Autowired
    private AssignmentsService service;

    // ✅ Changed path from "/upload" to just POST at root ("/api/assignments")
    @PostMapping
    public ResponseEntity<String> uploadAssignment(
            @RequestParam("className") String className,
            @RequestParam("stream") String stream,
            @RequestParam("subject") String subject,
            @RequestParam("teacher") String teacher,
            @RequestParam("date") String dateStr,
            @RequestParam("worktodo") MultipartFile file
    ) {
        try {
            byte[] fileData = file.getBytes();

            Assignments assignment = new Assignments();
            assignment.setClassName(className);
            assignment.setStream(stream);
            assignment.setSubject(subject);
            assignment.setTeacher(teacher);
            assignment.setDate(Date.valueOf(dateStr)); // parsing "YYYY-MM-DD"
            assignment.setWorktodo(fileData);

            service.saveAssignment(assignment);
            return ResponseEntity.ok("✅ Assignment uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error uploading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Invalid date format. Use YYYY-MM-DD.");
        }
    }

    @GetMapping
    public List<Assignments> getAllAssignments() {
        return service.getAllAssignments();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long id) {
        Assignments assignment = service.getAssignmentById(id);
        if (assignment == null || assignment.getWorktodo() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("assignment_" + id + ".doc").build());

        return new ResponseEntity<>(assignment.getWorktodo(), headers, HttpStatus.OK);
    }
}
