package com.loanapp.transformers;

import com.loanapp.entities.Loan;
import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanRequest;
import com.loanapp.models.LoanResponse;
import com.loanapp.utils.RandomIdGenerator;
import com.loanapp.entities.PaymentOrder;
import com.loanapp.models.PaymentOrderResponse;

import java.util.ArrayList;
import java.util.List;

public class LoanDetailsTransformer {

    public static Loan transformLoanRequestToLoan(LoanRequest loanRequest) {

        Loan loan = new Loan();
        loan.setId(RandomIdGenerator.generateRandomId());
        loan.setAmount(loanRequest.getAmountRequired());
        loan.setPaymentFrequency(loanRequest.getPaymentFrequency());
        loan.setCustomerId(loanRequest.getCustomerId());
        loan.setLoanStatus(LoanStatus.PENDING);
        loan.setTerm(loanRequest.getTerm());

        return loan;
    }

    public static LoanResponse transformLoanToLoanResponse(Loan loan) {

        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setLoanTerm(loan.getTerm());
        loanResponse.setId(loan.getId());
        loanResponse.setLoanStatus(loan.getLoanStatus());
        loanResponse.setAmount(loan.getAmount());
        loanResponse.setPaymentFrequency(loan.getPaymentFrequency());
        loanResponse.setCustomerId(loan.getCustomerId());

        return loanResponse;
    }

    public static List<PaymentOrderResponse> transformPaymentScheduleToResponse (List<PaymentOrder> schedules) {
        List<PaymentOrderResponse> responses = new ArrayList<>();

        for (PaymentOrder sch : schedules) {
            PaymentOrderResponse response = new PaymentOrderResponse();
            response.setAmount(sch.getAmount());
            response.setPaymentStatus(sch.getPaymentOrderStatus());
            response.setPaymentDueDate(sch.getPaymentDueDate());

            responses.add(response);
        }

        return responses;
    }

}
