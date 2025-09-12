package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
//@CrossOrigin(origins = "http://localhost:3001")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    // ✅ FIXED: Proper error handling + clear logs
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createEmployee(
            @RequestParam("fullname") String fullname,
            @RequestParam("role") String role,
            @RequestParam("nationalid") String nationalid,
            @RequestParam("nextofkin") String nextofkin,
            @RequestParam("nextofkinno") String nextofkinno,
            @RequestParam("salary") double salary,
            @RequestParam("krapin") String krapin,
            @RequestParam("sha") String sha,
            @RequestParam("telephone") String telephone,
            @RequestParam("nssfno") String nssfno,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            Employee employee = new Employee();
            employee.setEmployeeId(UUID.randomUUID().toString());
            employee.setFullname(fullname);
            employee.setRole(role);
            employee.setNationalid(nationalid);
            employee.setNextofkin(nextofkin);
            employee.setNextofkinNo(nextofkinno);
            employee.setSalary(salary);
            employee.setKrapin(krapin);
            employee.setSha(sha);
            employee.setTelephone(telephone);
            employee.setNssfno(nssfno);

            if (photo != null && !photo.isEmpty()) {
                employee.setPhoto(photo.getBytes());
            }

            Employee saved = employeeService.saveEmployee(employee);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace(); // Logs full stacktrace in server logs
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error saving employee: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable String id,
            @ModelAttribute Employee updatedEmployee,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            updatedEmployee.setEmployeeId(id);
            if (photo != null && !photo.isEmpty()) {
                updatedEmployee.setPhoto(photo.getBytes());
            }
            Employee saved = employeeService.saveEmployee(updatedEmployee);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error updating employee: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("✔ Employee deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error deleting employee: " + e.getMessage());
        }
    }
}
