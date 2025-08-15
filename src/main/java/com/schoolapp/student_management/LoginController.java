package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")  // You can restrict this later as needed
public class LoginController {

    @Autowired
    private LoginAuthService authService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = authService.authenticate(request.getUsername(), request.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("role", user.getRole());
            response.put("employeeId", user.getEmployeeId());
            response.put("admissionNo", user.getAdmissionNo());
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid credentials or inactive account");

            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
