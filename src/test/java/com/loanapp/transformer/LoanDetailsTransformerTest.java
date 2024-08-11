package com.loanapp.transformer;

import com.loanapp.entities.Frequency;
import com.loanapp.entities.Loan;
import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanRequest;
import com.loanapp.transformers.LoanDetailsTransformer;
import com.loanapp.utils.RandomIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoanDetailsTransformerTest {

    @Test
    void testTransformLoanRequestToLoan() {
        // Arrange
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setAmountRequired(50000.00);
        loanRequest.setPaymentFrequency(Frequency.WEEKLY);
        loanRequest.setCustomerId("cust123");
        loanRequest.setTerm(12);

        String expectedId = "loan123";
        RandomIdGenerator idGeneratorMock = mock(RandomIdGenerator.class);
        when(idGeneratorMock.generateRandomId()).thenReturn(expectedId);

        // Act
        Loan loan = LoanDetailsTransformer.transformLoanRequestToLoan(loanRequest);

        // Assert
        assertNotNull(loan);
        assertEquals(expectedId, loan.getId());
        assertEquals(loanRequest.getAmountRequired(), loan.getAmount());
        assertEquals(loanRequest.getPaymentFrequency(), loan.getPaymentFrequency());
        assertEquals(loanRequest.getCustomerId(), loan.getCustomerId());
        assertEquals(LoanStatus.PENDING, loan.getLoanStatus());
        assertEquals(loanRequest.getTerm(), loan.getTerm());
    }
}
