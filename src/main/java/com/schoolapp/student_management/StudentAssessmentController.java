package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/student/assessments")
@CrossOrigin(origins = "*")
public class StudentAssessmentController {

    private static final Logger log = LoggerFactory.getLogger(StudentAssessmentController.class);

    @Autowired
    private AssessmentRepository assessmentRepository;

    // ✅ Simple test endpoint to check if the controller is reachable
    @GetMapping("/ping")
    public String ping() {
        log.info("DEBUG: Ping endpoint hit!");
        return "pong";
    }

    // ✅ Fetch all assessments for a given student, ordered by date (most recent first)
    @GetMapping("/list")
    public List<Assessment> getAssessmentsByStudentId(@RequestParam("studentid") String studentid) {
        log.info("DEBUG: Received studentid from frontend = '{}'", studentid);

        if (studentid == null || studentid.trim().isEmpty()) {
            log.error("ERROR: studentid parameter is missing or empty!");
            return List.of(); // Return empty list if studentid is invalid
        }

        List<Assessment> results = assessmentRepository.findByStudentIdOrderByDateRecordedDesc(studentid);

        if (results == null || results.isEmpty()) {
            log.info("DEBUG: No assessments found for studentid = '{}'", studentid);
        } else {
            log.info("DEBUG: Found {} assessment(s) for studentid '{}'", results.size(), studentid);
        }

        return results;
    }
}
