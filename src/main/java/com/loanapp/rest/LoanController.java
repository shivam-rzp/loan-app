package com.loanapp.rest;

import com.loanapp.models.LoanRequest;
import com.loanapp.models.LoanResponse;
import com.loanapp.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable String id) {
        LoanResponse response = loanService.getLoanById(id);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest) {
        return loanService.saveOrUpdateLoan(loanRequest);
    }
}
