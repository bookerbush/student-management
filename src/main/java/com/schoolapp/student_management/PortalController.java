package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/portal")
@CrossOrigin(origins = "*")
public class PortalController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AssignmentsRepository assignmentsRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ✅ 1. Fetch Fee Balance for a Student
    @GetMapping("/balance/{admissionNo}")
    public ResponseEntity<?> getFeeBalance(@PathVariable String admissionNo) {
        List<Payment> payments = paymentRepository.findByStudentid(admissionNo);
        if (payments.isEmpty()) {
            return ResponseEntity.ok(Map.of("balance", 0));
        }

        Payment latest = payments.stream().max(Comparator.comparing(Payment::getPaymentid)).orElse(null);
        return ResponseEntity.ok(Map.of("balance", latest != null ? latest.getBalance() : 0));
    }

    // ✅ 2. Payment History for Parent Portal
    @GetMapping("/payment-history/{admissionNo}")
    public List<Payment> getPaymentHistory(@PathVariable String admissionNo) {
        return paymentRepository.findByStudentid(admissionNo);
    }

    // ✅ 3. Assignments for Student (Filtered by Class and Stream)
    @GetMapping("/assignments/{classEnrolled}/{stream}")
    public List<Assignments> getAssignments(
            @PathVariable String classEnrolled,
            @PathVariable String stream) {
        return assignmentsRepository.findByClassNameAndStream(classEnrolled, stream);
    }

    // ✅ 4. Assessment Results for Student
    @GetMapping("/results/{admissionNo}")
    public List<Assessment> getAssessmentResults(@PathVariable String admissionNo) {
        return assessmentRepository.findByStudentId(admissionNo);
    }

    // ✅ 5. General School Messages for Parent Portal
    @GetMapping("/messages/general")
    public List<String> getGeneralMessages() {
        // For now, hardcoded demo messages:
        return List.of(
                "School reopens on 10th September.",
                "Parent-Teacher meeting on 5th August."
        );
    }
}
