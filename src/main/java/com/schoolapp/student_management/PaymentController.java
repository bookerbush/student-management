// File: PaymentController.java  
package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/save")
    public ResponseEntity<?> savePayment(@RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentService.savePayment(payment);
            return ResponseEntity.ok(savedPayment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/history")
    public List<Payment> getPaymentHistory(@RequestParam String studentid, @RequestParam String studyyear) {
        return paymentService.getPaymentsByStudentAndYear(studentid, studyyear);
    }
@GetMapping("/statement")
public ResponseEntity<List<Payment>> getFeeStatement(@RequestParam String studentid, @RequestParam String studyyear) {
    List<Payment> statement = paymentService.getPaymentsByStudentAndYear(studentid, studyyear);
    System.out.println("✅ Fetched fee statement for studentid: " + studentid + " → " + statement.size() + " records");

    return ResponseEntity.ok(statement);
}

    // ✅ Corrected Fee Balance Endpoint
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getFeeBalance(@RequestParam String studentid, @RequestParam String studyyear) {
        Optional<Student> studentOpt = studentRepository.findByAdmissionNumber(studentid);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOpt.get();

        List<Payment> payments = paymentService.getPaymentsByStudentAndYear(studentid, studyyear);

        double latestBalance = payments.stream()
                .sorted(Comparator.comparing(Payment::getPaymentid, Comparator.nullsLast(Long::compareTo)).reversed())
                .findFirst()
                .map(p -> Optional.ofNullable(p.getBalance()).orElse(0.0))
                .orElse(0.0);

        Map<String, Object> response = new HashMap<>();
        response.put("studentid", student.getAdmissionNumber());
        response.put("studentname", student.getFirstName() + " " + student.getLastName());
        response.put("studyyear", studyyear);
        response.put("balance", latestBalance);

        System.out.println("✅ Fetched balance for studentid: " + studentid + " → Balance: " + latestBalance);

        return ResponseEntity.ok(response);
    }
}
