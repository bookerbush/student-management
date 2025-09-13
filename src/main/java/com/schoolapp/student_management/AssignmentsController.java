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
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> uploadAssignment(
            @RequestParam("className") String className,
            @RequestParam("stream") String stream,
            @RequestParam("subject") String subject,
            @RequestParam("teacher") String teacher,
            @RequestParam("date") String dateStr,
            @RequestParam("worktodo") MultipartFile file
    ) {
        try {
            // --- safer date parsing ---
            Date sqlDate;
            try {
                sqlDate = Date.valueOf(dateStr.trim()); // must be YYYY-MM-DD
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("❌ Invalid date format: " + dateStr + ". Expected YYYY-MM-DD.");
            }

            // --- read file ---
            byte[] fileData = file.getBytes();

            // --- build assignment ---
            Assignments assignment = new Assignments();
            assignment.setClassName(className);
            assignment.setStream(stream);
            assignment.setSubject(subject);
            assignment.setTeacher(teacher);
            assignment.setDate(sqlDate);
            assignment.setWorktodo(fileData);

            // --- save assignment ---
            service.saveAssignment(assignment);

            return ResponseEntity.ok("✅ Assignment uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Unexpected error: " + e.getMessage());
        }
    }

    // ✅ Get all assignments
    @GetMapping
    public List<Assignments> getAllAssignments() {
        return service.getAllAssignments();
    }

    // ✅ Download assignment file (uses assigno as primary key)
    @GetMapping("/download/{assigno}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long assigno) {
        Assignments assignment = service.getAssignmentById(assigno);
        if (assignment == null || assignment.getWorktodo() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // ✅ Give a meaningful filename (subject + assigno)
        String filename = assignment.getSubject() + "_assignment_" + assignment.getAssigno() + ".bin";
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentLength(assignment.getWorktodo().length);

        return new ResponseEntity<>(assignment.getWorktodo(), headers, HttpStatus.OK);
    }
}
