package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Save a new student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get a student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Update a student
    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            updatedStudent.setId(id);
            return studentRepository.save(updatedStudent);
        }).orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    // Delete a student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

   // ✅ Get latest student ID
public Long getLatestStudentId() {
    Student latestStudent = studentRepository.findTopByOrderByIdDesc();
    return latestStudent != null ? latestStudent.getId() : 0L;
}

}
