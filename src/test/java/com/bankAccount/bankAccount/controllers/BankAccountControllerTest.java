package com.bankAccount.bankAccount.controllers;

import com.bankAccount.bankAccount.config.TestSecurityConfig;
import com.bankAccount.bankAccount.controllers.bankAccount.BankAccountController;
import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.services.auth.CustomUserDetailsService;
import com.bankAccount.bankAccount.config.JwtUtil;
import com.bankAccount.bankAccount.services.bankAccount.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
@Import(TestSecurityConfig.class)
@AllArgsConstructor
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private BankAccountService bankAccountService;

    @Test
    void testGetAllAccountsByUser() throws Exception {
        // Arrange
        long userId = 1L;

        List<BankAccount> mockAccounts = Arrays.asList(
                BankAccount.builder()
                        .idAccount(1L)
                        .numberAccount("123456")
                        .balance(1000.0)
                        .type("Saving")
                        .build(),
                BankAccount.builder()
                        .idAccount(2L)
                        .numberAccount("654321")
                        .balance(2000.0)
                        .type("Checking")
                        .build()
        );

        // Simulamos la llamada al servicio
        when(bankAccountService.getAllAccountsByUser(userId)).thenReturn(mockAccounts);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/get-accounts-by-user/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idAccount").value(1L))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[1].type").value("Checking"));
    }


    @Test
    void testGetBalanceByAccount() throws Exception {
        // Arrange
        long accountId = 1L;

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Balance tested successfully")
                .success(true)
                .data(1000.0)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.getBalanceByAccount(accountId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/get-balance-by-account/{id}", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Balance tested successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(1000.0));
    }

    @Test
    void testCreateAccount() throws Exception {
        // Arrange
        long userId = 1L;
        BankAccount accountRequest = BankAccount.builder()
                .numberAccount("123456789")
                .balance(1500.0)
                .type("Saving")
                .build();

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Account created successfully")
                .success(true)
                .data(accountRequest)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.createAccount(userId, accountRequest)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/create/{idUser}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(accountRequest)))  // Convertimos el objeto a JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Account created successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.numberAccount").value("123456789"))
                .andExpect(jsonPath("$.data.balance").value(1500.0))
                .andExpect(jsonPath("$.data.type").value("Saving"));
    }

    @Test
    void testDepositMoney() throws Exception {
        // Arrange
        long accountId = 1L;
        double depositAmount = 500.0;

        String depositRequest = "{\"amount\": 500.0}";

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Deposit successful")
                .success(true)
                .data(null)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.depositMoney(accountId, depositAmount)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/deposit/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositRequest))  // Enviamos el JSON en el cuerpo
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Deposit successful"))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    void testWithdrawMoney() throws Exception {
        // Arrange
        long accountId = 1L;
        double withdrawAmount = 300.0;

        String withdrawRequest = "{\"amount\": 300.0}";

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Withdrawal successful")
                .success(true)
                .data(null)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.withdrawMoney(accountId, withdrawAmount)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/accounts/withdraw/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawRequest))  // Enviamos el JSON en el cuerpo
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Withdrawal successful"))
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    void testUpdateAccount() throws Exception {
        // Arrange
        long accountId = 1L;
        BankAccount updatedAccount = BankAccount.builder()
                .numberAccount("987654")
                .type("Checking")
                .build();

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Account updated successfully")
                .success(true)
                .data(updatedAccount)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.updateAccount(accountId, updatedAccount)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/accounts/update/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedAccount)))  // Convertimos el objeto a JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Account updated successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.numberAccount").value("987654"))
                .andExpect(jsonPath("$.data.type").value("Checking"));
    }


    @Test
    void testDeleteAccount() throws Exception {
        // Arrange
        long accountId = 1L;

        // DTO esperado
        BankAccountResponseDTO mockResponse = BankAccountResponseDTO.builder()
                .message("Account deleted successfully")
                .success(true)
                .data(null)
                .build();

        // Simulamos la llamada al servicio
        when(bankAccountService.deleteAccount(accountId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(delete("/api/accounts/delete/{id}", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Account deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }
}