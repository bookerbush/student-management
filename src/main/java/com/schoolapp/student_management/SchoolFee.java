package com.schoolapp.student_management;

import jakarta.persistence.*;

@Entity
public class SchoolFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemDescription;
    private Double pp0Fee;      // for "PP0" column in frontend
    private Double pp12Fee;     // for "PP1-PP2" column in frontend
    private Double grade13Fee;
    private Double grade46Fee;
    private String remarks;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public Double getPp0Fee() { return pp0Fee; }
    public void setPp0Fee(Double pp0Fee) { this.pp0Fee = pp0Fee; }

    public Double getPp12Fee() { return pp12Fee; }
    public void setPp12Fee(Double pp12Fee) { this.pp12Fee = pp12Fee; }

    public Double getGrade13Fee() { return grade13Fee; }
    public void setGrade13Fee(Double grade13Fee) { this.grade13Fee = grade13Fee; }

    public Double getGrade46Fee() { return grade46Fee; }
    public void setGrade46Fee(Double grade46Fee) { this.grade46Fee = grade46Fee; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
