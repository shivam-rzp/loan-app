package com.loanapp.models;

import com.loanapp.entities.LoanStatus;
import com.loanapp.entities.Frequency;
import lombok.Data;

import java.util.List;

@Data
public class LoanResponse {

    private String Id;
    private double amount;
    private int loanTerm;
    private String customerId;
    private Frequency paymentFrequency;
    private LoanStatus loanStatus;

    private List<PaymentOrderResponse> paymentSchedules;

}
