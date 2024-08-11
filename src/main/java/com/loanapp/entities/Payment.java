package com.loanapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Payment extends BaseEntity{

    @Id
    private String id;
    private String customerId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
