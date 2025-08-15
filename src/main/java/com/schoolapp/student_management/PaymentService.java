package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SchoolFeeRepository schoolFeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStudentAndYear(String studentid, String studyyear) {
        return paymentRepository.findByStudentidAndStudyyear(studentid, studyyear);
    }

    public Payment savePayment(Payment payment) {
        Optional<Student> studentOpt = studentRepository.findByAdmissionNumber(payment.getStudentid());
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found: " + payment.getStudentid());
        }

        Student student = studentOpt.get();
        String fullName = student.getFirstName() + " " + student.getLastName();
        payment.setStudentname(fullName); // ✅ set student name from DB

        List<Payment> existing = paymentRepository.findByStudentid(payment.getStudentid());
        boolean isFirstPayment = existing.isEmpty();

        double totalOnce = 0, totalAnnual = 0, totalEveryTerm = 0;
        List<SchoolFee> allFees = schoolFeeRepository.findAll();

        for (SchoolFee fee : allFees) {
            double amount = 0.0;
            try {
                switch (student.getClassEnrolled().toLowerCase()) {
                    case "pp0" -> amount = Optional.ofNullable(fee.getPp0Fee()).orElse(0.0);
                    case "pp1", "pp2" -> amount = Optional.ofNullable(fee.getPp12Fee()).orElse(0.0);
                    case "grade1", "grade2", "grade3" -> amount = Optional.ofNullable(fee.getGrade13Fee()).orElse(0.0);
                    case "grade4", "grade5", "grade6" -> amount = Optional.ofNullable(fee.getGrade46Fee()).orElse(0.0);
                    default -> amount = 0.0;
                }
            } catch (Exception e) {
                amount = 0.0;
            }

            if (!Boolean.TRUE.equals(student.getBoardingStatus()) &&
                    fee.getItemDescription() != null &&
                    fee.getItemDescription().toLowerCase().contains("boarding")) {
                continue;
            }

            if ("Paid once on admission".equals(fee.getRemarks())) {
                totalOnce += amount;
            } else if ("Paid Annually".equals(fee.getRemarks())) {
                totalAnnual += amount;
            } else if ("Paid Every Term".equals(fee.getRemarks())) {
                totalEveryTerm += amount;
            }
        }

        double oldBal;
        if (isFirstPayment) {
            oldBal = totalOnce + totalAnnual + totalEveryTerm;
        } else {
            Payment latest = existing.stream()
                    .max(Comparator.comparing(Payment::getPaymentid))
                    .orElse(null);

            double carryForward = latest != null && latest.getBalance() != null ? latest.getBalance() : 0;
            boolean isNewYear = latest != null && !latest.getStudyyear().equals(payment.getStudyyear());
            boolean isNewTerm = latest != null && !latest.getTerm().equals(payment.getTerm());

            if (isNewYear) {
                oldBal = carryForward + totalAnnual + totalEveryTerm;
            } else if (isNewTerm) {
                oldBal = carryForward + totalEveryTerm;
            } else {
                oldBal = carryForward;
            }
        }

        double paidAmount = payment.getAmount() != null ? payment.getAmount() : 0.0;
        double newBalance = oldBal - paidAmount;

        payment.setOldbalance(oldBal);
        payment.setBalance(newBalance);

        return paymentRepository.save(payment);
    }

    // ✅ Trigger Hibernate to touch Payment entity at startup
    @Bean
    public Runnable initializePaymentEntityOnStartup() {
        return () -> {
            System.out.println("📦 Triggering Hibernate to scan 'payments' table...");
            long count = paymentRepository.count(); // triggers Hibernate mapping
            System.out.println("✅ Payment entity touched. Records: " + count);
        };
    }
}
