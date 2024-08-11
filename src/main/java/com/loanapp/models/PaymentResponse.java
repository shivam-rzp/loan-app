package com.loanapp.models;

import com.loanapp.entities.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponse {

    private double amount;
    private String loanId;
    private PaymentStatus paymentStatus;

}
