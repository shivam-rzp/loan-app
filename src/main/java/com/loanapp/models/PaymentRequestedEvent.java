package com.loanapp.models;

import com.loanapp.entities.Loan;
import lombok.Data;

@Data
public class PaymentRequestedEvent {

    private double amount;
    private String loanId;
    private String tickerId;
    private Loan loan;
}
