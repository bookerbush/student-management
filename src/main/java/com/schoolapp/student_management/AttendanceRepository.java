package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Tracking, Long> {
    List<Tracking> findByStudentIdOrderByTrackIdDesc(String studentId);
}
