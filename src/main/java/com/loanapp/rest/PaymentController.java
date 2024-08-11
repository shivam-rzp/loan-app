package com.loanapp.rest;


import com.loanapp.services.PaymentService;
import com.loanapp.models.PaymentRequest;
import com.loanapp.models.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public PaymentResponse makePayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.makePayment(paymentRequest);
    }

}
