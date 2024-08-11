package com.loanapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PaymentOrder extends BaseEntity {

    @Id
    private String id;
    private String loanId;
    private double amount;
    private LocalDate paymentDueDate;

    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus paymentOrderStatus;

}
