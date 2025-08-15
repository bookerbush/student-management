package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentDataController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SchoolFeeRepository schoolFeeRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/load/{admNo}/{year}/{term}")
    public ResponseEntity<Map<String, Object>> loadStudentFeeData(
            @PathVariable String admNo,
            @PathVariable String year,
            @PathVariable String term) {

        // Normalize inputs
        admNo = admNo.trim();
        year = year.trim();
        term = term.trim();

        System.out.println("🔍 Loading payment data for: admNo=" + admNo + ", year=" + year + ", term=" + term);

        Map<String, Object> response = new HashMap<>();

        Optional<Student> studentOpt = studentRepository.findByAdmissionNumber(admNo);
        if (studentOpt.isEmpty()) {
            System.out.println("❌ No student found for admission number: " + admNo);
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOpt.get();
        String className = student.getClassEnrolled() != null ? student.getClassEnrolled() : "";
        String stream = student.getStream() != null ? student.getStream() : "";
        boolean boardingStatus = Boolean.TRUE.equals(student.getBoardingStatus());

        System.out.println("✅ Student found: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("📘 Class: " + className + ", Stream: " + stream + ", Boarding: " + boardingStatus);

        // Fetch and filter fee items
        List<SchoolFee> feeItems = schoolFeeRepository.findAll();
        List<Map<String, Object>> filteredItems = new ArrayList<>();

        for (SchoolFee fee : feeItems) {
            double amount = 0.0;

            try {
                amount = switch (className.toLowerCase()) {
                    case "pp0" -> Optional.ofNullable(fee.getPp0Fee()).orElse(0.0);
                    case "pp1", "pp2" -> Optional.ofNullable(fee.getPp12Fee()).orElse(0.0);
                    case "grade1", "grade2", "grade3" -> Optional.ofNullable(fee.getGrade13Fee()).orElse(0.0);
                    case "grade4", "grade5", "grade6" -> Optional.ofNullable(fee.getGrade46Fee()).orElse(0.0);
                    default -> 0.0;
                };
            } catch (Exception e) {
                System.out.println("⚠️ Error reading fee for: " + fee.getItemDescription());
            }

            if (!boardingStatus && fee.getItemDescription() != null &&
                    fee.getItemDescription().toLowerCase().contains("boarding")) {
                continue;
            }

            Map<String, Object> item = new HashMap<>();
            item.put("itemDescription", fee.getItemDescription());
            item.put("amount", amount);
            item.put("remarks", fee.getRemarks());
            filteredItems.add(item);
        }

        System.out.println("💰 Fee items after filtering: " + filteredItems.size());

        // Group and summarize by remark
        List<String> remarkOrder = List.of("Paid once on admission", "Paid Annually", "Paid Every Term");
        List<Map<String, Object>> sortedGroupedItems = new ArrayList<>();

        for (String remarkType : remarkOrder) {
            List<Map<String, Object>> group = filteredItems.stream()
                    .filter(f -> remarkType.equals(f.get("remarks")))
                    .toList();

            sortedGroupedItems.addAll(group);

            double total = group.stream()
                    .mapToDouble(f -> (double) f.get("amount"))
                    .sum();

            Map<String, Object> totalRow = new HashMap<>();
            totalRow.put("itemDescription", "");
            totalRow.put("amount", total);
            totalRow.put("remarks", "<TOTAL:" + remarkType + ">");
            sortedGroupedItems.add(totalRow);
        }

        // Normalize term again before query
        List<Payment> previousPayments = new ArrayList<>();
        try {
            previousPayments = paymentRepository.findByStudentidAndStudyyearAndTerm(admNo, year, term);
            System.out.println("📄 Payment records found: " + previousPayments.size());
        } catch (Exception e) {
            System.out.println("⚠️ Failed to load payment history: " + e.getMessage());
        }

        List<Map<String, Object>> paymentRecords = new ArrayList<>();
        for (Payment p : previousPayments) {
            Map<String, Object> payMap = new HashMap<>();
            payMap.put("amount", p.getAmount() != null ? p.getAmount() : 0.0);
            payMap.put("paydate", p.getPaydate() != null ? p.getPaydate().toString() : "--");
            payMap.put("balance", p.getBalance() != null ? p.getBalance() : 0.0);
            paymentRecords.add(payMap);
        }

        response.put("classEnrolled", className);
        response.put("stream", stream);
        response.put("feeItems", sortedGroupedItems);
        response.put("payments", paymentRecords);

        return ResponseEntity.ok(response);
    }
}
