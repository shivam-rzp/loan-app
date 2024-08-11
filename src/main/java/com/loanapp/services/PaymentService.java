package com.loanapp.services;

import com.loanapp.models.PaymentRequestedEvent;
import com.loanapp.repo.LoanRepository;
import com.loanapp.repo.PaymentRepository;
import com.loanapp.repo.TickerRepository;
import com.loanapp.utils.CommonUtils;
import com.loanapp.utils.RandomIdGenerator;
import com.loanapp.utils.ValidationUtils;
import com.loanapp.entities.*;
import com.loanapp.models.PaymentRequest;
import com.loanapp.models.PaymentResponse;
import com.loanapp.models.RefundRequest;
import com.loanapp.repo.PaymentOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private PaymentRequestProducer paymentRequestProducer;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PaymentResponse makePayment(PaymentRequest paymentRequest) {

        validatePaymentRequest(paymentRequest);

        Loan loan = getLoan(paymentRequest.getLoanId());
        validateLoanStatus(loan);

        String tickerId = processPaymentRequest(paymentRequest);

        monitorPaymentStatus(tickerId);

        return createPaymentResponse(tickerId, paymentRequest.getAmount());
    }

    private void validatePaymentRequest(PaymentRequest paymentRequest) {
        ValidationUtils.isInvalid(paymentRequest.getLoanId() == null, "Loan id can't be null");
        ValidationUtils.isInvalid(paymentRequest.getAmount() <= 0, "Amount must be greater than 0");
    }

    private Loan getLoan(String loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan id is invalid"));
    }

    private void validateLoanStatus(Loan loan) {
        ValidationUtils.isInvalid(loan.getLoanStatus() != LoanStatus.PENDING, "Loan is already closed or paid");
    }

    private String processPaymentRequest(PaymentRequest paymentRequest) {
        PaymentRequestedEvent paymentRequestedEvent = new PaymentRequestedEvent();
        paymentRequestedEvent.setAmount(paymentRequest.getAmount());
        paymentRequestedEvent.setLoanId(paymentRequest.getLoanId());

        String tickerId = RandomIdGenerator.generateRandomId();

        PaymentTicker paymentTicker = new PaymentTicker();
        paymentTicker.setId(tickerId);
        paymentTicker.setTickerStatus(TickerStatus.PENDING);

        tickerRepository.save(paymentTicker);
        paymentRequestedEvent.setTickerId(tickerId);

        paymentRequestProducer.sendMessage(CommonUtils.convertObjectToString(paymentRequestedEvent));

        return tickerId;
    }

    private void monitorPaymentStatus(String tickerId) {
        final long startTime = System.currentTimeMillis();

        Runnable task = () -> {
            PaymentTicker ticker = tickerRepository.findById(tickerId).orElse(null);
            if (ticker == null || isTimeoutOrStatusChanged(startTime, ticker)) {
                scheduler.shutdown();
                logPaymentStatus(ticker);
            } else {
                System.out.println("Checking payment status...");
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);
    }

    private boolean isTimeoutOrStatusChanged(long startTime, PaymentTicker ticker) {
        return System.currentTimeMillis() - startTime >= 10 * 1000 || ticker.getTickerStatus() != TickerStatus.PENDING;
    }

    private void logPaymentStatus(PaymentTicker ticker) {
        if (ticker.getTickerStatus() == TickerStatus.PENDING) {
            System.out.println("Ticker stopped after 10 seconds. Payment status is still pending.");
        } else {
            System.out.println("Payment status changed to " + ticker.getTickerStatus() + ". Ticker stopped.");
        }
    }

    private PaymentResponse createPaymentResponse(String tickerId, double originalAmount) {
        PaymentTicker paymentTicker = tickerRepository.findById(tickerId).orElse(null);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setAmount(originalAmount);

        if (paymentTicker != null && paymentTicker.getTickerStatus() == TickerStatus.FAILED) {
            paymentResponse.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            paymentResponse.setPaymentStatus(PaymentStatus.PAID);
        }
        return paymentResponse;
    }
}
