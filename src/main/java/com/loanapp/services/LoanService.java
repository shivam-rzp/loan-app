package com.loanapp.services;

import com.loanapp.entities.Frequency;
import com.loanapp.models.LoanRequest;
import com.loanapp.models.LoanResponse;
import com.loanapp.utils.RandomIdGenerator;
import com.loanapp.entities.Loan;
import com.loanapp.entities.PaymentOrder;
import com.loanapp.entities.PaymentOrderStatus;
import com.loanapp.models.PaymentOrderResponse;
import com.loanapp.repo.LoanRepository;
import com.loanapp.repo.PaymentOrderRepository;
import com.loanapp.transformers.LoanDetailsTransformer;
import com.loanapp.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PaymentOrderRepository paymentSchedulesRepository;

    @Transactional
    public LoanResponse getLoanById(String id) {
        Loan loan =  loanRepository.findById(id).orElse(null);
        ValidationUtils.isInvalid(loan== null, "Invalid loan id");

        LoanResponse loanResponse = LoanDetailsTransformer.transformLoanToLoanResponse(loan);

        List<PaymentOrder> schedules = paymentSchedulesRepository.findByLoanId(loan.getId());

        loanResponse.setPaymentSchedules(LoanDetailsTransformer.transformPaymentScheduleToResponse(schedules));

        return loanResponse;
    }

    @Transactional
    public LoanResponse saveOrUpdateLoan(LoanRequest loanRequest) {

        ValidationUtils.isInvalid(loanRequest.getCustomerId() == null, "Customer Id can't be null");
        ValidationUtils.isInvalid(loanRequest.getTerm() <= 0, "Term can't be less than 0");
        ValidationUtils.isInvalid(loanRequest.getAmountRequired() <= 0, "Amount required can't be less than 0");

        if(loanRequest.getPaymentFrequency() == null) {
            loanRequest.setPaymentFrequency(Frequency.WEEKLY);
        }

        Loan loan = LoanDetailsTransformer.transformLoanRequestToLoan(loanRequest);
        loan =  loanRepository.save(loan);

        // Create Loan payment schedule.
        double amount = loan.getAmount()/loan.getTerm();
        // create payment schedules.

        LocalDate currentDate = LocalDate.now();
        List<PaymentOrderResponse> scheduleResponseList = new ArrayList<>();
        List<PaymentOrder> paymentSchedulesList = new ArrayList<>();

        for (int i=1;i<=loan.getTerm();i++) {

            LocalDate paymentDueDate = generatePaymentDueDate(currentDate,loanRequest.getPaymentFrequency(), i );
            PaymentOrder schedules = new PaymentOrder();
            schedules.setLoanId(loan.getId());
            schedules.setId(RandomIdGenerator.generateRandomId());
            schedules.setAmount(amount);
            schedules.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
            schedules.setPaymentDueDate(paymentDueDate);

            schedules = paymentSchedulesRepository.save(schedules);

            paymentSchedulesList.add(schedules);
        }

        LoanResponse loanResponse = LoanDetailsTransformer.transformLoanToLoanResponse(loan);
        loanResponse.setPaymentSchedules(LoanDetailsTransformer.transformPaymentScheduleToResponse(paymentSchedulesList));

        return loanResponse;
    }

    private LocalDate generatePaymentDueDate(LocalDate currentDate, Frequency paymentFrequency, int count) {

        switch (paymentFrequency) {
            case WEEKLY :
                return currentDate.plusWeeks(count);
        }

        return  null;
    }
}
