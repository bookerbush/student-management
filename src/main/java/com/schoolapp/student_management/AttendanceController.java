package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parentdata/attendance")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/list")
    public List<Tracking> getAttendanceByStudentId(@RequestParam String studentid) {
        return attendanceRepository.findByStudentIdOrderByTrackIdDesc(studentid);
    }
}
