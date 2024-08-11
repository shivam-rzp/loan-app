package com.loanapp;

import com.loanapp.entities.LoanStatus;
import com.loanapp.models.LoanRequest;
import com.loanapp.models.LoanResponse;
import com.loanapp.rest.LoanController;
import com.loanapp.services.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Test
    public void testCreateLoan() throws Exception {

        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setLoanStatus(LoanStatus.PENDING);


        Mockito.when(loanService.saveOrUpdateLoan(Mockito.any(LoanRequest.class))).thenReturn(loanResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"term\": 3, \"amountRequired\": 10231.23, \"customerId\": \"1\" }")) // Example JSON payload
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("loanStatus").exists());
    }

    @Test
    public void testFetchLoan() throws Exception {

        LoanResponse entity = new LoanResponse();
        entity.setLoanTerm(3);
        entity.setCustomerId("1");

        when(loanService.getLoanById("1L")).thenReturn(entity);

        mockMvc.perform(get("/api/v1/loan/1L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanTerm").value(3));
    }


}
