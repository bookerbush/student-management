package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByAdmissionNumber(String admissionNumber);

    // ✅ Fetch the latest student by ID
    Student findTopByOrderByIdDesc();

    // ✅ ADDED FOR MESSAGING — fetch all active students (or customize filter)
    List<Student> findByStudentStatusIgnoreCase(String status);

    List<Student> findByStudentStatus(String status);


}
