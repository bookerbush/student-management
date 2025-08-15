package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolProfileService {

    @Autowired
    private SchoolProfileRepository repository;

    public SchoolProfile getProfile() {
        return repository.findById(1L).orElse(null); // Default to profile with ID 1
    }
}
