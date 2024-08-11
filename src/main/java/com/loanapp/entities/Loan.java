package com.loanapp.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Loan extends BaseEntity{

    @Id
    private String id;
    private String customerId;
    private int term;
    private double amount;

    @Enumerated(EnumType.STRING)
    private Frequency paymentFrequency;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;
}


