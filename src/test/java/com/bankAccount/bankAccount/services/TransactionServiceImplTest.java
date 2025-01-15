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
        assertNotNull(transactions);
        assertEquals(1, transactions.size());

        TransactionResponseDTO transaction = transactions.get(0);
        assertTrue(transactions.stream().anyMatch(t -> t.getMessage().equals(message) && t.getAmount() == amount));
        assertNotNull(transaction.getDate());
    }

    @Test
    public void testGetAllTransactions() {
        // Arrange
        transactionService.addTransaction("Deposit of $100", 100.0);
        transactionService.addTransaction("Withdrawal of $50", 50.0);

        // Act
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();

        // Assert
        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        assertTrue(transactions.stream().anyMatch(t -> t.getMessage().equals("Deposit of $100")));
        assertTrue(transactions.stream().anyMatch(t -> t.getMessage().equals("Withdrawal of $50")));
    }

    @Test
    public void testGetTransactionsList() {
        // Arrange
        transactionService.addTransaction("Deposit of $100", 100.0);
        transactionService.addTransaction("Withdrawal of $50", 50.0);

        // Act
        List<String> transactionMessages = transactionService.getTransactionsList();

        // Assert
        assertNotNull(transactionMessages);
        assertEquals(2, transactionMessages.size());

        assertTrue(transactionMessages.stream().anyMatch(message -> message.equals("Deposit of $100")));
        assertTrue(transactionMessages.stream().anyMatch(message -> message.equals("Withdrawal of $50")));
    }
}