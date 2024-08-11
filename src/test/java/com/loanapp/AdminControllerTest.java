package com.loanapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanEvaluationRequest;
import com.loanapp.models.LoanEvaluationResponse;
import com.loanapp.rest.AdminController;
import com.loanapp.services.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @Test
    public void testEvaluateLoan() throws Exception {

        LoanEvaluationRequest loanEvaluationRequest = new LoanEvaluationRequest();
        loanEvaluationRequest.setLoanStatus(LoanStatus.APPROVED);

        LoanEvaluationResponse loanEvaluationResponse = new LoanEvaluationResponse();
        loanEvaluationResponse.setLoanStatus(LoanStatus.APPROVED);

        when(adminService.evaluateLoan(any(LoanEvaluationRequest.class))).thenReturn(loanEvaluationResponse);

        mockMvc.perform(post("/api/v1/admin/evaluateLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanEvaluationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanStatus").value("approved"));
    }

}
