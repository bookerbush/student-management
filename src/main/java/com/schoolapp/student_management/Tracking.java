// ✅ UPDATED: Tracking.java
package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Tracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    private String studentId;
    private String student;
    private Integer percentPresent;

    private String studentClass;  // ✅ NEW FIELD
    private String stream;        // ✅ NEW FIELD

    private LocalDate monDate, tueDate, wedDate, thursDate, friDate, satDate;
    private String monStatus, tueStatus, wedStatus, thursStatus, friStatus, satStatus;

    // Getters and Setters
    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudent() { return student; }
    public void setStudent(String student) { this.student = student; }

    public Integer getPercentPresent() { return percentPresent; }
    public void setPercentPresent(Integer percentPresent) { this.percentPresent = percentPresent; }

    public String getStudentClass() { return studentClass; }  // ✅
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }

    public String getStream() { return stream; }              // ✅
    public void setStream(String stream) { this.stream = stream; }

    public LocalDate getMonDate() { return monDate; }
    public void setMonDate(LocalDate monDate) { this.monDate = monDate; }

    public LocalDate getTueDate() { return tueDate; }
    public void setTueDate(LocalDate tueDate) { this.tueDate = tueDate; }

    public LocalDate getWedDate() { return wedDate; }
    public void setWedDate(LocalDate wedDate) { this.wedDate = wedDate; }

    public LocalDate getThursDate() { return thursDate; }
    public void setThursDate(LocalDate thursDate) { this.thursDate = thursDate; }

    public LocalDate getFriDate() { return friDate; }
    public void setFriDate(LocalDate friDate) { this.friDate = friDate; }

    public LocalDate getSatDate() { return satDate; }
    public void setSatDate(LocalDate satDate) { this.satDate = satDate; }

    public String getMonStatus() { return monStatus; }
    public void setMonStatus(String monStatus) { this.monStatus = monStatus; }

    public String getTueStatus() { return tueStatus; }
    public void setTueStatus(String tueStatus) { this.tueStatus = tueStatus; }

    public String getWedStatus() { return wedStatus; }
    public void setWedStatus(String wedStatus) { this.wedStatus = wedStatus; }

    public String getThursStatus() { return thursStatus; }
    public void setThursStatus(String thursStatus) { this.thursStatus = thursStatus; }

    public String getFriStatus() { return friStatus; }
    public void setFriStatus(String friStatus) { this.friStatus = friStatus; }

    public String getSatStatus() { return satStatus; }
    public void setSatStatus(String satStatus) { this.satStatus = satStatus; }
}
