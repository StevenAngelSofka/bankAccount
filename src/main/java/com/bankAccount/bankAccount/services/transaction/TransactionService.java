package com.bankAccount.bankAccount.services.transaction;
import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {

    void addTransaction(String message, double amount);

    List<TransactionResponseDTO> getAllTransactions();

    List<String> getTransactionsList();

}
