package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "http://localhost:3001")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping
    public List<Assessment> getAllAssessments() {
        return assessmentService.getAllAssessments();
    }

    @GetMapping("/{id}")
    public Optional<Assessment> getAssessmentById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Assessment> getByStudentId(@PathVariable String studentId) {
        return assessmentService.getByStudentId(studentId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<Assessment> getByTeacherId(@PathVariable String teacherId) {
        return assessmentService.getByTeacherId(teacherId);
    }

    @PostMapping
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessment(assessment);
    }

    @PutMapping("/{id}")
    public Assessment updateAssessment(@PathVariable Long id, @RequestBody Assessment updated) {
        return assessmentService.updateAssessment(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
    }
}
