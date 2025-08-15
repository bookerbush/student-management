package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByStudentId(String studentId);
    List<Assessment> findByTeacherId(String teacherId);
    List<Assessment> findByStudentIdOrderByDateRecordedDesc(String studentId);

}
