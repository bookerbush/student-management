package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:3001")
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

    // ✅ FIXED: Accept multipart/form-data correctly
    @PostMapping(consumes = {"multipart/form-data"})
    public Employee createEmployee(
            @RequestParam("fullname") String fullname,
            @RequestParam("role") String role,
            @RequestParam("nationalid") String nationalid,
            @RequestParam("nextofkin") String nextofkin,
            @RequestParam("nextofkinno") String nextofkinno,
            @RequestParam("salary") double salary,
            @RequestParam("krapin") String krapin,
            @RequestParam("sha") String sha,
            @RequestParam("telephone") String telephone,
             @RequestParam("nssfno") String nssfno,  // ✅ Add this line
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
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
        employee.setNssfno(nssfno);  // ✅ Set here too

        if (photo != null && !photo.isEmpty()) {
            employee.setPhoto(photo.getBytes());
        }

        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable String id, @ModelAttribute Employee updatedEmployee,
                                   @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        updatedEmployee.setEmployeeId(id);
        if (photo != null && !photo.isEmpty()) {
            updatedEmployee.setPhoto(photo.getBytes());
        }
        return employeeService.saveEmployee(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
    }
}
