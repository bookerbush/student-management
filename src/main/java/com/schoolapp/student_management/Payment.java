package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentid;

    private String studentid;
    private String studentname;
    private String term;
    private String studyyear;
    private Double amount;

    @Temporal(TemporalType.DATE)
    private Date paydate;

    @Column(name = "payment_reference") // ✅ Fixed: no reserved keyword
    private String paymentReference;

    private Double balance;
    private Double oldbalance;

    // Getters and Setters

    public Long getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Long paymentid) {
        this.paymentid = paymentid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStudyyear() {
        return studyyear;
    }

    public void setStudyyear(String studyyear) {
        this.studyyear = studyyear;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getPaydate() {
        return paydate;
    }

    public void setPaydate(Date paydate) {
        this.paydate = paydate;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getOldbalance() {
        return oldbalance;
    }

    public void setOldbalance(Double oldbalance) {
        this.oldbalance = oldbalance;
    }
}
