package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BalancesRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudyyearAndTermOrderByStudentidAscPaymentidDesc(String studyyear, String term);

    // ✅ Add this method for filtering by student
    List<Payment> findByStudentidAndStudyyearAndTermOrderByPaymentidDesc(String studentid, String studyyear, String term);
}
