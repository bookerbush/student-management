package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssignmentsService {

    @Autowired
    private AssignmentsRepository repository;

    public Assignments saveAssignment(Assignments assignment) {
        return repository.save(assignment);
    }

    public List<Assignments> getAllAssignments() {
        return repository.findAll();
    }

    public Assignments getAssignmentById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
