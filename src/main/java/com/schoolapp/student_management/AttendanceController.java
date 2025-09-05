package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracking/dashboard/attendance")
@CrossOrigin // ✅ Allow React frontend calls
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // 🔹 Existing method (by studentid, still available if needed)
    @GetMapping("/by-student")
    public List<Tracking> getAttendanceByStudentId(@RequestParam String studentid) {
        return attendanceRepository.findByStudentIdOrderByTrackIdDesc(studentid);
    }

    // 🔹 New method (by date, used by DashboardAttendance.js)
    @GetMapping
    public List<Tracking> getAttendanceByDate(@RequestParam String date) {
        // Make sure AttendanceRepository has this method:
        // List<Tracking> findByTrackDate(String trackDate);
        return attendanceRepository.findByTrackDate(date);
    }
}
