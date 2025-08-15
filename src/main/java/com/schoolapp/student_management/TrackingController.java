package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "http://localhost:3001")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    // ✅ CHANGE: call upsert method
    @PostMapping
    public Tracking saveOrUpdate(@RequestBody Tracking tracking) {
        return trackingService.saveOrUpdateTracking(tracking);
    }

    @GetMapping
    public List<Tracking> getAll() {
        return trackingService.getAllTracking();
    }

    // ✅ Already correct
    @GetMapping("/filter")
    public List<Tracking> getByClassStreamAndDates(
        @RequestParam("class") String cls,
        @RequestParam("stream") String stream,
        @RequestParam("from") String fromDate,
        @RequestParam("to") String toDate
    ) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        return trackingService.findByClassStreamAndWeek(cls, stream, from, to);
    }
    @GetMapping("/by-date")
public List<Tracking> getAttendanceByDate(@RequestParam("date") String dateStr) {
    LocalDate date = LocalDate.parse(dateStr);
    return trackingService.getAttendanceByAnyDate(date);
}
@GetMapping("/dashboard/attendance")
public List<Tracking> getDashboardAttendance(@RequestParam("date") String date) {
    LocalDate parsedDate = LocalDate.parse(date);
    return trackingService.getAttendanceByDate(parsedDate);
}

}
