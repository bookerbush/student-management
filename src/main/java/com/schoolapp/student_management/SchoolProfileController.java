package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/school-profile")
@CrossOrigin(origins = "*")
public class SchoolProfileController {

    @Autowired
    private SchoolProfileService service;

    @GetMapping
    public SchoolProfile getProfile() {
        return service.getProfile();
    }
}
