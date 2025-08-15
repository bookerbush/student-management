package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolFeeRepository extends JpaRepository<SchoolFee, Long> {
}
