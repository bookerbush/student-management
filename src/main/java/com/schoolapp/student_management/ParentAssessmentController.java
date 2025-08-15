package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/parent/assessments")
@CrossOrigin(origins = "*")
public class ParentAssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @GetMapping("/list")
    public List<Assessment> getAssessmentsByStudentId(@RequestParam("studentid") String studentid) {
        return assessmentRepository.findByStudentIdOrderByDateRecordedDesc(studentid);
    }
}
