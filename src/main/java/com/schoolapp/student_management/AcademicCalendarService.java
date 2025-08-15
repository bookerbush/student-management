package com.schoolapp.student_management;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AcademicCalendarService {

    private final AcademicCalendarRepository repository;

    public AcademicCalendarService(AcademicCalendarRepository repository) {
        this.repository = repository;
    }

    public AcademicCalendar save(AcademicCalendar calendar) {
        return repository.save(calendar);
    }

    public List<AcademicCalendar> getAll() {
        return repository.findAll();
    }
}
