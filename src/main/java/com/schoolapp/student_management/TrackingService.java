package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrackingService {

    @Autowired
    private TrackingRepository repository;

    public Tracking saveTracking(Tracking t) {
        return repository.save(t);
    }

    public List<Tracking> getAllTracking() {
        return repository.findAll();
    }

    // ✅ Used by legacy /by-date route — keep as-is
    public List<Tracking> getAttendanceByAnyDate(LocalDate date) {
        return repository.findByAnyMatchingDate(date);
    }

    // ✅ Used by dashboard — now optimized for speed and accuracy
    public List<Tracking> getAttendanceByDate(LocalDate date) {
        return repository.findByAnyMatchingDate(date);
    }

    public List<Tracking> findByClassStreamAndWeek(String cls, String str, LocalDate from, LocalDate to) {
        return repository.findByClassStreamAndDateRange(cls, str, from, to);
    }

    // ✅ Upsert logic (unchanged)
    public Tracking saveOrUpdateTracking(Tracking t) {
        return repository.findByStudentIdAndStudentClassAndStreamAndMonDate(
                t.getStudentId(),
                t.getStudentClass(),
                t.getStream(),
                t.getMonDate()
        ).map(existing -> {
            existing.setStudent(t.getStudent());
            existing.setPercentPresent(t.getPercentPresent());

            existing.setTueDate(t.getTueDate());
            existing.setWedDate(t.getWedDate());
            existing.setThursDate(t.getThursDate());
            existing.setFriDate(t.getFriDate());
            existing.setSatDate(t.getSatDate());

            existing.setMonStatus(t.getMonStatus());
            existing.setTueStatus(t.getTueStatus());
            existing.setWedStatus(t.getWedStatus());
            existing.setThursStatus(t.getThursStatus());
            existing.setFriStatus(t.getFriStatus());
            existing.setSatStatus(t.getSatStatus());

            return repository.save(existing);
        }).orElseGet(() -> repository.save(t));
    }
}
