package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalancesService {

    @Autowired
    private BalancesRepository balancesRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UMSSender smsSender;

    public List<BalanceReportDTO> getBalanceReport(String year, String term, String admissionNumber) {

        if (admissionNumber != null && !admissionNumber.trim().isEmpty()) {
            // ✅ CASE 1: Detailed Statement for specific admission number
            List<Payment> studentPayments = balancesRepository
                .findByStudentidAndStudyyearAndTermOrderByPaymentidDesc(admissionNumber.trim(), year, term);

            return studentPayments.stream().map(payment -> {
                String stream = studentRepository.findByAdmissionNumber(payment.getStudentid())
                        .map(Student::getStream).orElse("--");

                return new BalanceReportDTO(
                        payment.getPaymentid(),
                        payment.getStudentid(),
                        stream,
                        payment.getStudentname(),
                        payment.getAmount(),
                        payment.getPaydate(),
                        payment.getBalance(),
                        payment.getPaymentReference()
                );
            }).collect(Collectors.toList());
        }

        // ✅ CASE 2: Summary Mode (no admission number)
        List<Payment> allPayments = balancesRepository.findByStudyyearAndTermOrderByStudentidAscPaymentidDesc(year, term);
        Map<String, List<Payment>> grouped = allPayments.stream()
            .collect(Collectors.groupingBy(Payment::getStudentid, LinkedHashMap::new, Collectors.toList()));

        List<BalanceReportDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<Payment>> entry : grouped.entrySet()) {
            String studentid = entry.getKey();
            List<Payment> payments = entry.getValue();

            Payment latest = payments.get(0); // most recent

            if (latest.getBalance() <= 0) continue;

            double totalPaid = payments.stream().mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();

            String stream = studentRepository.findByAdmissionNumber(studentid)
                    .map(Student::getStream).orElse("--");

            result.add(new BalanceReportDTO(
                    latest.getPaymentid(),
                    studentid,
                    stream,
                    latest.getStudentname(),
                    totalPaid,
                    latest.getPaydate(),
                    latest.getBalance(),
                    latest.getPaymentReference()
            ));
        }

        return result;
    }

    // ✅ SMS sending logic
    public String sendBalanceSMS(String year, String term) {
        List<BalanceReportDTO> balanceList = getBalanceReport(year, term, null);  // summary mode
        int sentCount = 0;

        for (BalanceReportDTO dto : balanceList) {
            if (dto.getBalance() > 0) {
                String studentid = dto.getStudentid();
                Optional<Student> studentOpt = studentRepository.findByAdmissionNumber(studentid);
                if (studentOpt.isPresent()) {
                    String phone = studentOpt.get().getFatherPhone();
                    if (phone != null && phone.length() >= 10) {
                        smsSender.sendSMS(phone, dto.getStudentname(), dto.getBalance());
                        sentCount++;
                    }
                }
            }
        }

        return "SMS sent to " + sentCount + " parents.";
    }
}
