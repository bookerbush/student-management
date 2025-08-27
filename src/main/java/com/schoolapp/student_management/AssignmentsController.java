package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentsController {

    @Autowired
    private AssignmentsService service;

    // ✅ Upload assignment with file
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
            assignment.setDate(Date.valueOf(dateStr)); // expects "YYYY-MM-DD"
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

    // ✅ Get all assignments
    @GetMapping
    public List<Assignments> getAllAssignments() {
        return service.getAllAssignments();
    }

    // ✅ Download assignment file
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long id) {
        Assignments assignment = service.getAssignmentById(id);
        if (assignment == null || assignment.getWorktodo() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Give a more meaningful filename (subject + id)
        String filename = assignment.getSubject() + "_assignment_" + id + ".bin";
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentLength(assignment.getWorktodo().length);

        return new ResponseEntity<>(assignment.getWorktodo(), headers, HttpStatus.OK);
    }
}
