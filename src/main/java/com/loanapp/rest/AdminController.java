package com.loanapp.rest;


import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.services.AdminService;
import com.loanapp.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/evaluateLoan")
    public ResponseEntity<LoanEvaluationResponse> evaluateLoan(@RequestBody LoanEvaluationRequest assesLoanRequest) {
        LoanEvaluationResponse response = adminService.evaluateLoan(assesLoanRequest);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
