package com.bankAccount.bankAccount.services.transaction;

import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import org.springframework.http.HttpStatus;
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
                .httpStatus(HttpStatus.OK)
                .build();

        transactionList.add(transaction);
        messagesList.add(transaction.getMessage());
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return List.copyOf(transactionList);
    }

    @Override
    public List<String> getTransactionsList() {
        return transactionList.stream()
                .map(TransactionResponseDTO::getMessage)
                .toList();
    }
}
