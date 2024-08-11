package com.loanapp.models;

import com.loanapp.entities.PaymentOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentOrderResponse {
    private double amount;
    private LocalDate paymentDueDate;
    private PaymentOrderStatus paymentStatus;
}
