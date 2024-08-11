package com.loanapp.models;

import com.loanapp.entities.Frequency;
import lombok.Data;

@Data
public class LoanRequest {

    private double amountRequired;
    private int term;
    private String customerId;
    private Frequency paymentFrequency;

}
