package com.loanapp.services;

import com.loanapp.entities.Refund;
import com.loanapp.models.RefundRequest;
import com.loanapp.repo.RefundRepository;
import com.loanapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundService {


    @Autowired
    private RefundRepository refundRepository;

    public void makeRefund(RefundRequest refundRequest) {

        Refund refund = new Refund();
        refund.setAmount(refundRequest.getAmount());
        refund.setLoanId(refundRequest.getLoanId());
        refund.setCustomerId(refundRequest.getCustomerId());
        refund.setId(RandomIdGenerator.generateRandomId());

        refundRepository.save(refund);
    }
}
