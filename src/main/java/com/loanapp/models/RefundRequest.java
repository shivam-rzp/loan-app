package com.loanapp.models;

import lombok.Data;

@Data
public class RefundRequest {

    private double amount;
    private String customerId;
    private String loanId;
}
