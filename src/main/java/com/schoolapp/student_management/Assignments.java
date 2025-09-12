package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.sql.Date; // ✅ use sql.Date, not util.Date

@Entity
@Table(name = "assignment")
public class Assignments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assigno;

    private String className;  // avoid using reserved word "class"
    private String stream;
    private String subject;

    @Lob
    @Basic(fetch = FetchType.LAZY) // ✅ ensures large files load only when needed
    @Column(name = "worktodo", nullable = false)
    private byte[] worktodo;

    private Date date; // ✅ sql.Date maps correctly in both MySQL & Postgres

    private String teacher;

    // Constructors
    public Assignments() {}

    public Assignments(String className, String stream, String subject, byte[] worktodo, Date date, String teacher) {
        this.className = className;
        this.stream = stream;
        this.subject = subject;
        this.worktodo = worktodo;
        this.date = date;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getAssigno() {
        return assigno;
    }

    public void setAssigno(Long assigno) {
        this.assigno = assigno;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public byte[] getWorktodo() {
        return worktodo;
    }

    public void setWorktodo(byte[] worktodo) {
        this.worktodo = worktodo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
