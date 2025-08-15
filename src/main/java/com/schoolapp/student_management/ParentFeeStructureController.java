package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/parentdata")
@CrossOrigin(origins = "*")
public class ParentFeeStructureController {

    @Autowired
    private SchoolFeeRepository feeRepository;

    @GetMapping("/feestructure")
    public List<SchoolFee> getFeeStructure() {
        return feeRepository.findAll();
    }
}
