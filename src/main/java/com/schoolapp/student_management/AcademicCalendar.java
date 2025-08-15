package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "academic_calendar")
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studyyear;
    private String term;
    private LocalDate openningDate;
    private LocalDate midtermDate;
    private LocalDate clossingDate;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudyyear() { return studyyear; }
    public void setStudyyear(String studyyear) { this.studyyear = studyyear; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public LocalDate getOpenningDate() { return openningDate; }
    public void setOpenningDate(LocalDate openningDate) { this.openningDate = openningDate; }

    public LocalDate getMidtermDate() { return midtermDate; }
    public void setMidtermDate(LocalDate midtermDate) { this.midtermDate = midtermDate; }

    public LocalDate getClossingDate() { return clossingDate; }
    public void setClossingDate(LocalDate clossingDate) { this.clossingDate = clossingDate; }
}
