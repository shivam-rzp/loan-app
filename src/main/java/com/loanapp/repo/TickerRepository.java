package com.loanapp.repo;

import com.loanapp.entities.PaymentTicker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TickerRepository extends JpaRepository<PaymentTicker, String> {
}

