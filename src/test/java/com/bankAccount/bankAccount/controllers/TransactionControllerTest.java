package com.bankAccount.bankAccount.controllers;

import com.bankAccount.bankAccount.config.TestSecurityConfig;
import com.bankAccount.bankAccount.controllers.transaction.TransactionController;
import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import com.bankAccount.bankAccount.services.auth.CustomUserDetailsService;
import com.bankAccount.bankAccount.config.JwtUtil;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
@AllArgsConstructor
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void testGetAllTransactions() throws Exception {
        // Arrange
        List<TransactionResponseDTO> mockTransactions = List.of(
                TransactionResponseDTO.builder()
                        .amount(100.0)
                        .date(LocalDateTime.now())
                        .build(),
                TransactionResponseDTO.builder()
                        .amount(200.0)
                        .date(LocalDateTime.now())
                        .build()
        );

        // Simulamos la llamada al servicio
        when(transactionService.getAllTransactions()).thenReturn(mockTransactions);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/get-transactions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(100.0))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[1].amount").value(200.0))
                .andExpect(jsonPath("$[1].date").exists());
    }

    @Test
    void testGetTransactionsList() throws Exception {
        // Arrange
        List<String> mockTransactionsList = List.of(
                "Transaction-1",
                "Transaction-2",
                "Transaction-3"
        );

        // Simulamos la llamada al servicio
        when(transactionService.getTransactionsList()).thenReturn(mockTransactionsList);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/get-transactions-list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("Transaction-1"))
                .andExpect(jsonPath("$[1]").value("Transaction-2"))
                .andExpect(jsonPath("$[2]").value("Transaction-3"));
    }
}