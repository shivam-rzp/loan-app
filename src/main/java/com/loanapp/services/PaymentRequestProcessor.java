package com.loanapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanapp.entities.*;
import com.loanapp.models.PaymentRequestedEvent;
import com.loanapp.models.RefundRequest;
import com.loanapp.repo.LoanRepository;
import com.loanapp.repo.PaymentOrderRepository;
import com.loanapp.repo.PaymentRepository;
import com.loanapp.repo.TickerRepository;
import com.loanapp.utils.CommonUtils;
import com.loanapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class PaymentRequestProcessor {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RefundService refundService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TickerRepository tickerRepository;

    @KafkaListener(topics = "payment_requested", groupId = "my-group")
    public void consume(String message) {

        PaymentRequestedEvent paymentRequestedEvent = CommonUtils.convertJsonStringToObject(message, PaymentRequestedEvent.class);
        List<PaymentOrder> paymentOrderList = paymentOrderRepository.findAllPendingPaymentsOrderedByDateAndAmount(paymentRequestedEvent.getLoanId());

        double remainingAmount = processPayments(paymentRequestedEvent, paymentOrderList);

        if (remainingAmount <= 0) {
            updateLoanStatus(paymentRequestedEvent.getLoan());
        }

        createAndSavePayment(paymentRequestedEvent);

        if (remainingAmount > 0) {
            processRefund(paymentRequestedEvent, remainingAmount);
        }

        updateTickerStatus(paymentRequestedEvent.getTickerId(), TickerStatus.SUCCESS);
    }

    private double processPayments(PaymentRequestedEvent paymentRequestedEvent, List<PaymentOrder> paymentOrderList) {
        double amount = paymentRequestedEvent.getAmount();
        for (PaymentOrder paymentOrder : paymentOrderList) {
            if (amount <= 0) break;

            if (paymentOrder.getAmount() <= amount) {
                amount -= paymentOrder.getAmount();
                paymentOrder.setAmount(0);
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PAID);
            } else {
                paymentOrder.setAmount(paymentOrder.getAmount() - amount);
                amount = 0;
            }
            paymentOrderRepository.save(paymentOrder);
        }
        return amount;
    }

    private void updateLoanStatus(Loan loan) {
        loan.setLoanStatus(LoanStatus.PAID);
        loanRepository.save(loan);
    }

    private void createAndSavePayment(PaymentRequestedEvent paymentRequestedEvent) {
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setCustomerId(paymentRequestedEvent.getLoan().getCustomerId());
        payment.setId(RandomIdGenerator.generateRandomId());

        paymentRepository.save(payment);
    }

    private void processRefund(PaymentRequestedEvent paymentRequestedEvent, double amount) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAmount(amount);
        refundRequest.setLoanId(paymentRequestedEvent.getLoan().getId());
        refundRequest.setCustomerId(paymentRequestedEvent.getLoan().getCustomerId());

        refundService.makeRefund(refundRequest);
    }

    private void updateTickerStatus(String tickerId, TickerStatus status) {
        PaymentTicker ticker = tickerRepository.findById(tickerId)
                .orElseThrow(() -> new IllegalArgumentException("Ticker not found"));
        ticker.setTickerStatus(status);
        tickerRepository.save(ticker);
    }
}
