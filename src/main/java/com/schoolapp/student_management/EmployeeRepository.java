package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Additional custom queries if needed later
}
