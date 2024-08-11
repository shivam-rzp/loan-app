package com.loanapp.models;

import com.loanapp.entities.LoanStatus;
import lombok.Data;

@Data
public class LoanEvaluationRequest {

    private String loanId;
    private LoanStatus loanStatus;

}
