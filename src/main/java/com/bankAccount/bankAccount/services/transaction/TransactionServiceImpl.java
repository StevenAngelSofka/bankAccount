package com.bankAccount.bankAccount.services.transaction;

import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final List<TransactionResponseDTO> transactionList = new ArrayList<>();
    private final List<String> messagesList = new ArrayList<>();

    @Override
    public void addTransaction(String message, double amount) {
        TransactionResponseDTO transaction = TransactionResponseDTO.builder()
                .message(message)
                .success(true)
                .amount(amount)
                .date(LocalDateTime.now())
                .build();

        transactionList.add(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return new ArrayList<>(transactionList);
    }

    @Override
    public List<String> getTransactionsList() {

        for (TransactionResponseDTO transaction : transactionList) {
            messagesList.add(transaction.getMessage());
        }

        return messagesList;
    }
}
