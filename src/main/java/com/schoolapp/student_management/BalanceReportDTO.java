package com.schoolapp.student_management;

import java.util.Date;

public class BalanceReportDTO {
    private Long paymentid;
    private String studentid;
    private String stream;
    private String studentname;
    private Double amount;
    private Date paydate;
    private Double balance;
    private String paymentReference;

    // ✅ CONSTRUCTOR
    public BalanceReportDTO(Long paymentid, String studentid, String stream, String studentname,
                            Double amount, Date paydate, Double balance, String paymentReference) {
        this.paymentid = paymentid;
        this.studentid = studentid;
        this.stream = stream;
        this.studentname = studentname;
        this.amount = amount;
        this.paydate = paydate;
        this.balance = balance;
        this.paymentReference = paymentReference;
    }

    // ✅ GETTERS (Add setters if needed)
    public Long getPaymentid() {
        return paymentid;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getStream() {
        return stream;
    }

    public String getStudentname() {
        return studentname;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getPaydate() {
        return paydate;
    }

    public Double getBalance() {
        return balance;
    }

    public String getPaymentReference() {
        return paymentReference;
    }
}
