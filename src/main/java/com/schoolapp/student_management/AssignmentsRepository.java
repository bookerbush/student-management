package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentsRepository extends JpaRepository<Assignments, Long> {
    List<Assignments> findByClassNameAndStream(String className, String stream);
}
