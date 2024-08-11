package com.loanapp.transformers;

import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;

public class AdminDetailsTransformer {

    public static LoanEvaluationResponse transformPaymentScheduleToResponse (LoanEvaluationRequest evaluateLoanRequest) {

        LoanEvaluationResponse response = new LoanEvaluationResponse();
        response.setLoanId(evaluateLoanRequest.getLoanId());
        response.setLoanStatus(evaluateLoanRequest.getLoanStatus());

        return response;
    }
}
