package com.loanapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.loanapp.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, String> {
}
