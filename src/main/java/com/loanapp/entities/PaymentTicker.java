package com.loanapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PaymentTicker extends BaseEntity{

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private TickerStatus tickerStatus;


}
