package com.schoolapp.student_management;

import jakarta.persistence.*;

@Entity
@Table(name = "school_fee") // Ensure Hibernate maps to correct table name
public class SchoolFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "pp0fee")
    private Double pp0Fee;   // maps to column "pp0fee"

    @Column(name = "pp12fee")
    private Double pp12Fee;  // maps to column "pp12fee"

    @Column(name = "grade13fee")
    private Double grade13Fee; // maps to column "grade13fee"

    @Column(name = "grade46fee")
    private Double grade46Fee; // maps to column "grade46fee"

    @Column(name = "remarks")
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
