package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentidAndStudyyear(String studentid, String studyyear);
    List<Payment> findByStudentidAndStudyyearAndTerm(String studentid, String studyyear, String term);
      List<Payment> findByStudentid(String studentid);
}

