package com.loanapp.service;

import com.loanapp.entities.Loan;
import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.repo.LoanRepository;
import com.loanapp.services.AdminService;
import com.loanapp.services.LoanService;
import com.loanapp.transformers.AdminDetailsTransformer;
import com.loanapp.utils.ValidationUtils;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private Loan loan;

    @Mock
    private ValidationUtils validationUtils;

    @InjectMocks
    private AdminService adminService;  // Replace with your actual service class

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluateLoan_Success() {
        Loan loan = new Loan();
        loan.setId("1");
        loan.setLoanStatus(LoanStatus.PENDING);

        LoanEvaluationRequest request = new LoanEvaluationRequest();
        request.setLoanId("1");
        request.setLoanStatus(LoanStatus.APPROVED);

        when(loanRepository.findById("1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(AdminDetailsTransformer.transformPaymentScheduleToResponse(request)).thenReturn(new LoanEvaluationResponse());

        LoanEvaluationResponse response = adminService.evaluateLoan(request);

        assertNotNull(response);
        assertEquals(LoanStatus.APPROVED, loan.getLoanStatus());
    }

    @Test
    void testEvaluateLoan_LoanNotFound() {
        LoanEvaluationRequest request = new LoanEvaluationRequest();
        request.setLoanId("1");

        when(loanRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            adminService.evaluateLoan(request);
        });

        assertEquals("Invalid Loan id", exception.getMessage());
    }

    @Test
    void testEvaluateLoan_InvalidLoanStatus() {
        Loan loan = new Loan();
        loan.setId("1");
        loan.setLoanStatus(LoanStatus.APPROVED);

        LoanEvaluationRequest request = new LoanEvaluationRequest();
        request.setLoanId("1");
        request.setLoanStatus(LoanStatus.REJECTED);

        when(loanRepository.findById("1")).thenReturn(Optional.of(loan));

        Exception exception = assertThrows(ValidationException.class, () -> {
            adminService.evaluateLoan(request);
        });

        assertEquals("Invalid loan status", exception.getMessage());
    }

    @Test
    void testEvaluateLoan_TransformResponse() {
        Loan loan = new Loan();
        loan.setId("1");
        loan.setLoanStatus(LoanStatus.PENDING);

        LoanEvaluationRequest request = new LoanEvaluationRequest();
        request.setLoanId("1");
        request.setLoanStatus(LoanStatus.APPROVED);

        LoanEvaluationResponse expectedResponse = new LoanEvaluationResponse();

        when(loanRepository.findById("1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(AdminDetailsTransformer.transformPaymentScheduleToResponse(request)).thenReturn(expectedResponse);

        LoanEvaluationResponse response = adminService.evaluateLoan(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }
}
