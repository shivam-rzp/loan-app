package com.loanapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Refund extends BaseEntity{

    @Id
    private String Id;
    private String customerId;
    private double amount;
    private String loanId;
}
