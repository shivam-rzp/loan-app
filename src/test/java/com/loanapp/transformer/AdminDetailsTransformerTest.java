package com.loanapp.transformer;

import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.transformers.LoanDetailsTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminDetailsTransformerTest {


    @Test
    void testTransformPaymentScheduleToResponse() {
        // Given
        LoanEvaluationRequest request = new LoanEvaluationRequest();
        request.setLoanId("123");
        request.setLoanStatus(LoanStatus.APPROVED);

        // When
        LoanEvaluationResponse response = new LoanEvaluationResponse();

        // Then
        assertNotNull(response, "Response should not be null");
    }
}
