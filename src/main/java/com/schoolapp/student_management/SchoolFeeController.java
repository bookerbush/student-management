package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fees")
@CrossOrigin(origins = "*")
public class SchoolFeeController {

    @Autowired
    private SchoolFeeService schoolFeeService;

    @PostMapping("/save")
    public List<SchoolFee> saveFees(@RequestBody List<SchoolFee> fees) {
        return schoolFeeService.saveAll(fees);
    }

    @GetMapping
    public List<SchoolFee> getAllFees() {
        return schoolFeeService.getAllFees();
    }
}
