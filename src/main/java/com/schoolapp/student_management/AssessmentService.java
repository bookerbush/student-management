package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Optional<Assessment> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);
    }

    public List<Assessment> getByStudentId(String studentId) {
        return assessmentRepository.findByStudentId(studentId);
    }

    public List<Assessment> getByTeacherId(String teacherId) {
        return assessmentRepository.findByTeacherId(teacherId);
    }

    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public Assessment updateAssessment(Long id, Assessment updated) {
        return assessmentRepository.findById(id)
            .map(assessment -> {
                assessment.setStudentId(updated.getStudentId());
                assessment.setTerm(updated.getTerm());
                assessment.setSubject(updated.getSubject());
                assessment.setStrand(updated.getStrand());
                assessment.setSubStrand(updated.getSubStrand());
                assessment.setPerformanceIndicator(updated.getPerformanceIndicator());
                assessment.setRating(updated.getRating());
                assessment.setComment(updated.getComment());
                assessment.setTeacherId(updated.getTeacherId());
                assessment.setAssess(updated.getAssess());
                assessment.setDateRecorded(updated.getDateRecorded());
                return assessmentRepository.save(assessment);
            })
            .orElseGet(() -> {
                updated.setId(id);
                return assessmentRepository.save(updated);
            });
    }

    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }
}
