package com.loanapp.models;

import lombok.Data;

@Data
public class PaymentRequest {

    private double amount;
    private String loanId;
}
