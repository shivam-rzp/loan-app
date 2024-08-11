package com.loanapp.service;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ValidationException;
import static org.mockito.Mockito.*;
import com.loanapp.entities.*;
import com.loanapp.models.LoanRequest;
import com.loanapp.models.LoanResponse;
import com.loanapp.models.PaymentOrderResponse;
import com.loanapp.repo.LoanRepository;
import com.loanapp.repo.TickerRepository;
import com.loanapp.services.LoanService;
import com.loanapp.transformers.LoanDetailsTransformer;
import com.loanapp.utils.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private LoanService loanService;  // Replace with your actual service class

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrUpdateLoan_Success() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId("1");
        loanRequest.setTerm(12);
        loanRequest.setAmountRequired(10000.0);
        loanRequest.setPaymentFrequency(Frequency.WEEKLY);

        Loan loan = new Loan();
        loan.setId("1");
        loan.setAmount(10000.0);
        loan.setTerm(12);
        loan.setLoanStatus(LoanStatus.PENDING);

        when(LoanDetailsTransformer.transformLoanRequestToLoan(loanRequest)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(loan);

        List<PaymentOrder> paymentSchedulesList = new ArrayList<>();
        List<PaymentOrderResponse> paymentOrderResponses = new ArrayList<>();
        for (int i = 1; i <= loan.getTerm(); i++) {
            PaymentOrder paymentOrder = new PaymentOrder();
            paymentOrder.setLoanId(loan.getId());
            paymentOrder.setId("randomId" + i);
            paymentOrder.setAmount(loan.getAmount() / loan.getTerm());
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
            paymentOrder.setPaymentDueDate(LocalDate.now().plusMonths(i));

            paymentSchedulesList.add(paymentOrder);
        }

        when(tickerRepository.save(any(PaymentTicker.class))).thenAnswer(invocation -> {
            PaymentOrder savedOrder = invocation.getArgument(0);
            savedOrder.setId("randomId");
            return savedOrder;
        });

        when(LoanDetailsTransformer.transformPaymentScheduleToResponse(paymentSchedulesList)).thenReturn(paymentOrderResponses);
        when(LoanDetailsTransformer.transformLoanToLoanResponse(loan)).thenReturn(new LoanResponse());

        LoanResponse response = loanService.saveOrUpdateLoan(loanRequest);

        assertNotNull(response);
        assertEquals(loan.getAmount(), response.getAmount());
        assertEquals(loan.getTerm(), response.getLoanTerm());
        assertEquals(loanRequest.getPaymentFrequency(), response.getPaymentFrequency());
        assertEquals(paymentOrderResponses, response.getPaymentSchedules());
    }

    @Test
    void testSaveOrUpdateLoan_InvalidCustomerId() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId(null);
        loanRequest.setTerm(12);
        loanRequest.setAmountRequired(10000.0);

        Exception exception = assertThrows(ValidationException.class, () -> {
            loanService.saveOrUpdateLoan(loanRequest);
        });

        assertEquals("Customer Id can't be null", exception.getMessage());
    }

    @Test
    void testSaveOrUpdateLoan_InvalidTerm() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId("1");
        loanRequest.setTerm(0);
        loanRequest.setAmountRequired(10000.0);

        Exception exception = assertThrows(ValidationException.class, () -> {
            loanService.saveOrUpdateLoan(loanRequest);
        });

        assertEquals("Term can't be less than 0", exception.getMessage());
    }

    @Test
    void testSaveOrUpdateLoan_InvalidAmountRequired() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId("1");
        loanRequest.setTerm(12);
        loanRequest.setAmountRequired(0);

        Exception exception = assertThrows(ValidationException.class, () -> {
            loanService.saveOrUpdateLoan(loanRequest);
        });

        assertEquals("Amount required can't be less than 0", exception.getMessage());
    }

    @Test
    void testSaveOrUpdateLoan_DefaultPaymentFrequency() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setCustomerId("1");
        loanRequest.setTerm(12);
        loanRequest.setAmountRequired(10000.0);
        loanRequest.setPaymentFrequency(null);

        Loan loan = new Loan();
        loan.setId("1");
        loan.setAmount(10000.0);
        loan.setTerm(12);
        loan.setLoanStatus(LoanStatus.PENDING);

        when(LoanDetailsTransformer.transformLoanRequestToLoan(loanRequest)).thenReturn(loan);
        when(loanRepository.save(loan)).thenReturn(loan);

        LoanResponse expectedResponse = new LoanResponse();
        when(LoanDetailsTransformer.transformLoanToLoanResponse(loan)).thenReturn(expectedResponse);

        LoanResponse response = loanService.saveOrUpdateLoan(loanRequest);

        assertNotNull(response);
        assertEquals(Frequency.WEEKLY, loanRequest.getPaymentFrequency());
    }
}
