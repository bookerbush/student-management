package com.schoolapp.student_management;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    private String fullname;
    private String role;
    private String nationalid;
    private String nextofkin;

    @Column(name = "nextofkin_no")
    private String nextofkinNo;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo; // ✅ changed from String to byte[]

    private double salary;
    private String krapin;
    private String sha;
    private String telephone;
    private String nssfno;

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNationalid() { return nationalid; }
    public void setNationalid(String nationalid) { this.nationalid = nationalid; }

    public String getNextofkin() { return nextofkin; }
    public void setNextofkin(String nextofkin) { this.nextofkin = nextofkin; }

    public String getNextofkinNo() { return nextofkinNo; }
    public void setNextofkinNo(String nextofkinNo) { this.nextofkinNo = nextofkinNo; }

    public byte[] getPhoto() { return photo; } // ✅ updated
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getKrapin() { return krapin; }
    public void setKrapin(String krapin) { this.krapin = krapin; }

    public String getSha() { return sha; }
    public void setSha(String sha) { this.sha = sha; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getNssfno() { return nssfno; }
    public void setNssfno(String nssfno) { this.nssfno = nssfno; }
}
