package com.bankAccount.bankAccount.services.bankAccount;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;

import java.util.List;

public interface BankAccountService {

    List<BankAccount> getAllAccountsByUser(long idUser);

    BankAccountResponseDTO getBalanceByAccount(long idAccount);

    BankAccountResponseDTO createAccount(long idUser, BankAccount account);

    BankAccountResponseDTO updateAccount(long idAccount, BankAccount account);

    BankAccountResponseDTO deleteAccount(long idAccount);

    BankAccountResponseDTO depositMoney(long idAccount, double amount);

    BankAccountResponseDTO withdrawMoney(long idAccount, double amount);

}