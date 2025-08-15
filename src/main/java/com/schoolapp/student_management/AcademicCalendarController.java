package com.schoolapp.student_management;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:3001")
public class AcademicCalendarController {

    private final AcademicCalendarService service;

    public AcademicCalendarController(AcademicCalendarService service) {
        this.service = service;
    }

    @PostMapping
    public AcademicCalendar save(@RequestBody AcademicCalendar calendar) {
        return service.save(calendar);
    }

    @GetMapping
    public List<AcademicCalendar> getAll() {
        return service.getAll();
    }
}
