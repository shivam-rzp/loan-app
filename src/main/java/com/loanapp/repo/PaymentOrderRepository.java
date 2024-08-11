package com.loanapp.repo;

import com.loanapp.entities.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, String> {

    List<PaymentOrder> findByLoanId(String loanId);

    @Query("SELECT p FROM PaymentOrder p WHERE p.paymentOrderStatus = 'pending' AND p.loanId = :loanId AND p.amount > 0 ORDER BY p.paymentDueDate ASC")
    List<PaymentOrder> findAllPendingPaymentsOrderedByDateAndAmount(
            @Param("loanId") String loanId
    );
}
