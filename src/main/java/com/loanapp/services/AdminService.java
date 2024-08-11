package com.loanapp.services;

import com.loanapp.entities.Loan;
import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.repo.LoanRepository;
import com.loanapp.transformers.AdminDetailsTransformer;
import com.loanapp.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private LoanRepository loanRepository;

    @Transactional
    public LoanEvaluationResponse evaluateLoan(LoanEvaluationRequest evaluateLoanRequest) {
        Loan loan =  loanRepository.findById(evaluateLoanRequest.getLoanId()).orElse(null);

        ValidationUtils.isInvalid(loan == null, "Invalid Loan id");
        ValidationUtils.isInvalid(loan.getLoanStatus() != LoanStatus.PENDING, "Invalid loan status");

        loan.setLoanStatus(evaluateLoanRequest.getLoanStatus());

        loan = loanRepository.save(loan);

        return AdminDetailsTransformer.transformPaymentScheduleToResponse(evaluateLoanRequest);
    }
}
