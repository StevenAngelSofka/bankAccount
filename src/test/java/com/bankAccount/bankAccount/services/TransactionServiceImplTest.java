package com.bankAccount.bankAccount.services;

import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import com.bankAccount.bankAccount.services.transaction.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    public void testAddTransaction() {
        // Arrange
        String message = "Deposit of $500";
        double amount = 500.0;

        // Act
        transactionService.addTransaction(message, amount);

        // Assert
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(message, transactions.get(0).getMessage());
        assertEquals(amount, transactions.get(0).getAmount());
        assertNotNull(transactions.get(0).getDate());
    }

    @Test
    public void testGetAllTransactions() {
        // Arrange
        transactionService.addTransaction("Deposit of $100", 100.0);
        transactionService.addTransaction("Withdrawal of $50", 50.0);

        // Act
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();

        // Assert
        assertEquals(2, transactions.size());
        assertEquals("Deposit of $100", transactions.get(0).getMessage());
        assertEquals("Withdrawal of $50", transactions.get(1).getMessage());
    }

    @Test
    public void testGetTransactionsList() {
        // Arrange
        transactionService.addTransaction("Deposit of $100", 100.0);
        transactionService.addTransaction("Withdrawal of $50", 50.0);

        // Act
        List<String> transactionMessages = transactionService.getTransactionsList();

        // Assert
        assertEquals(2, transactionMessages.size());
        assertEquals("Deposit of $100", transactionMessages.get(0));
        assertEquals("Withdrawal of $50", transactionMessages.get(1));
    }
}