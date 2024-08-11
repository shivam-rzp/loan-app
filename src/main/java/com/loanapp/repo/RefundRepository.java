package com.loanapp.repo;

import com.loanapp.entities.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, String> {
}
