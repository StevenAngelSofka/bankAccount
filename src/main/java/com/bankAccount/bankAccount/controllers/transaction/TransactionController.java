package com.bankAccount.bankAccount.controllers.transaction;

import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/get-transactions")
    List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/get-transactions-list")
    List<String> getTransactionsList() {
        return transactionService.getTransactionsList();
    }
}
