package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assessment")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String studentName; // ✅ NEW
    private String subject;
    private String assess;
    private String strand;
    private String subStrand;
    private String performanceIndicator;
    private int rating;
    private String comment;
    private String term;
    private LocalDate dateRecorded;
    private String teacherId;
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; } // ✅
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getAssess() { return assess; }
    public void setAssess(String assess) { this.assess = assess; }

    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }

    public String getSubStrand() { return subStrand; }
    public void setSubStrand(String subStrand) { this.subStrand = subStrand; }

    public String getPerformanceIndicator() { return performanceIndicator; }
    public void setPerformanceIndicator(String performanceIndicator) { this.performanceIndicator = performanceIndicator; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public LocalDate getDateRecorded() { return dateRecorded; }
    public void setDateRecorded(LocalDate dateRecorded) { this.dateRecorded = dateRecorded; }
    public String getTeacherId() {
    return teacherId;
}

public void setTeacherId(String teacherId) {
    this.teacherId = teacherId;
}
}
