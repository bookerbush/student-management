package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fees")
//@CrossOrigin(origins = "*")
public class SchoolFeeController {

    @Autowired
    private SchoolFeeService schoolFeeService;

    // ✅ Save fee items
    @PostMapping("/save")
    public ResponseEntity<?> saveFees(@RequestBody List<SchoolFee> fees) {
        try {
            List<SchoolFee> savedFees = schoolFeeService.saveAll(fees);
            return ResponseEntity.ok(savedFees); // returns 200 + saved data
        } catch (Exception e) {
            e.printStackTrace(); // logs error in backend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving fee items: " + e.getMessage());
        }
    }

    // ✅ Fetch all fee items
    @GetMapping
    public ResponseEntity<?> getAllFees() {
        try {
            List<SchoolFee> fees = schoolFeeService.getAllFees();
            return ResponseEntity.ok(fees);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving fee items: " + e.getMessage());
        }
    }
}
