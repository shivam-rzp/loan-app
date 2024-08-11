package com.loanapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanapp.entities.LoanStatus;
import com.loanapp.entities.Payment;
import com.loanapp.entities.PaymentStatus;
import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.models.PaymentRequest;
import com.loanapp.models.PaymentResponse;
import com.loanapp.rest.AdminController;
import com.loanapp.rest.PaymentController;
import com.loanapp.services.AdminService;
import com.loanapp.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void testMakePayment() throws Exception {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1212.12);
        paymentRequest.setLoanId("1");

        PaymentResponse response = new PaymentResponse();
        response.setPaymentStatus(PaymentStatus.PAID);
        response.setAmount(1212.12);

        when(paymentService.makePayment(any(PaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("paid"));
    }
}
