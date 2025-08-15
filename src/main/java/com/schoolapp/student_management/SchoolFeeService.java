package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolFeeService {

    @Autowired
    private SchoolFeeRepository schoolFeeRepository;

    public List<SchoolFee> saveAll(List<SchoolFee> fees) {
        return schoolFeeRepository.saveAll(fees);
    }

    public List<SchoolFee> getAllFees() {
        return schoolFeeRepository.findAll();
    }
}
