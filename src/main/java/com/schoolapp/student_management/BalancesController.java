package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balances")
@CrossOrigin(origins = "*")
public class BalancesController {

    @Autowired
    private BalancesService balancesService;

    @GetMapping("/{year}/{term}")
    public List<BalanceReportDTO> getBalances(
            @PathVariable String year,
            @PathVariable String term,
            @RequestParam(value = "admissionNumber", required = false) String admissionNumber
    ) {
        return balancesService.getBalanceReport(year, term, admissionNumber);
    }

    // ✅ Changed from GET to POST to match frontend request
    @PostMapping("/send-sms/{year}/{term}")
    public ResponseEntity<String> sendSMSForBalances(@PathVariable String year, @PathVariable String term) {
        String result = balancesService.sendBalanceSMS(year, term);
        return ResponseEntity.ok(result);
    }
}
