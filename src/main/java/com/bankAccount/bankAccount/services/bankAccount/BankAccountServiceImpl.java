package com.bankAccount.bankAccount.services.bankAccount;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.BankAccountRepository;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;

    @Override
    public List<BankAccount> getAllAccountsByUser(long idUser) {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccountResponseDTO getBalanceByAccount(long idAccount) {
        Optional<BankAccount> accountDB = bankAccountRepository.findById(idAccount);

        if(accountDB.isEmpty()) {
            return BankAccountResponseDTO.builder()
                    .message("The account with ID: " + idAccount + " does not exist.")
                    .success(false)
                    .data(null)
                    .build();
        }

        BankAccount account = accountDB.get();

        return BankAccountResponseDTO.builder()
                .message("The balance for account : " + account.getNumberAccount() + " is: " + account.getBalance())
                .success(true)
                .data(account.getBalance())
                .build();
    }

    @Override
    public BankAccountResponseDTO createAccount(long idUser, BankAccount account) {
        try{
            Optional<User> userDB = userRepository.findById(idUser);

            if (userDB.isEmpty()) {
                return BankAccountResponseDTO.builder()
                        .message("User with ID: " + idUser + " not found.")
                        .success(false)
                        .data(null)
                        .build();
            }
            User user = userDB.get();
            account.setUser(user);

            BankAccount createdAccount = bankAccountRepository.save(account);

            return BankAccountResponseDTO.builder()
                    .message("Account created successfully")
                    .success(true)
                    .data(createdAccount)
                    .build();
        } catch (Exception e) {
            return BankAccountResponseDTO.builder()
                    .message("Error creating an account: " + e.getMessage())
                    .success(false)
                    .data(null)
                    .build();
        }
    }

    @Override
    public BankAccountResponseDTO updateAccount(long idAccount, BankAccount account) {
        Optional<BankAccount> accountDB = bankAccountRepository.findById(idAccount);

        if(accountDB.isEmpty()) {
            return BankAccountResponseDTO.builder()
                    .message("The account with ID: " + idAccount + " does not exist.")
                    .success(false)
                    .data(null)
                    .build();
        }

        try{
            BankAccount accountToUpdate = accountDB.get();
            accountToUpdate.setNumberAccount(account.getNumberAccount());
            accountToUpdate.setType(account.getType());

            BankAccount updatedAccount = bankAccountRepository.save(accountToUpdate);

            return BankAccountResponseDTO.builder()
                    .message("Account updated successfully")
                    .success(true)
                    .data(updatedAccount)
                    .build();
        } catch (Exception e) {
            return BankAccountResponseDTO.builder()
                    .message("Error updating an account: " + e.getMessage())
                    .success(false)
                    .data(null)
                    .build();
        }


    }

    @Override
    public BankAccountResponseDTO deleteAccount(long idAccount) {
        Optional<BankAccount> accountDB = bankAccountRepository.findById(idAccount);

        if(accountDB.isEmpty()) {
            return BankAccountResponseDTO.builder()
                    .message("The account with ID: " + idAccount + " does not exist.")
                    .success(false)
                    .data(null)
                    .build();
        }

        try{
            bankAccountRepository.deleteById(idAccount);

            return BankAccountResponseDTO.builder()
                    .message("Account deleted successfully")
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return BankAccountResponseDTO.builder()
                    .message("Error deleting an account: " + e.getMessage())
                    .success(false)
                    .data(null)
                    .build();
        }
    }

    @Override
    public BankAccountResponseDTO depositMoney(long idAccount, double amount) {
        if(amount <= 0) {
            return BankAccountResponseDTO.builder()
                    .message("Invalid amount.")
                    .success(false)
                    .data(null)
                    .build();
        }

        Optional<BankAccount> accountDB = bankAccountRepository.findById(idAccount);

        if(accountDB.isEmpty()) {
            return BankAccountResponseDTO.builder()
                    .message("The account with ID: " + idAccount + " does not exist.")
                    .success(false)
                    .data(null)
                    .build();
        }

        try{
            BankAccount account = accountDB.get();
            double newBalance = account.getBalance() + amount;
            account.setBalance(newBalance);

            bankAccountRepository.save(account);

            transactionService.addTransaction(
                    "Successful deposit to account No. " + account.getNumberAccount() + " for an amount of: " + amount + " . Current balance: " + account.getBalance(),
                    amount
            );

            return BankAccountResponseDTO.builder()
                    .message("Transaction type: " + account.getType() + ". Amount: " + amount + " . Current Balance: " + account.getBalance())
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return BankAccountResponseDTO.builder()
                    .message("An error occurred while processing the transaction. Please try again later.")
                    .success(false)
                    .data(null)
                    .build();
        }
    }

    @Override
    public BankAccountResponseDTO withdrawMoney(long idAccount, double amount) {
        if(amount <= 0) {
            return BankAccountResponseDTO.builder()
                    .message("Invalid amount.")
                    .success(false)
                    .data(null)
                    .build();
        }

        Optional<BankAccount> accountDB = bankAccountRepository.findById(idAccount);

        if(accountDB.isEmpty()) {
            return BankAccountResponseDTO.builder()
                    .message("The account with ID: " + idAccount + " does not exist.")
                    .success(false)
                    .data(null)
                    .build();
        }

        BankAccount account = accountDB.get();

        if(amount > account.getBalance()) {
            return BankAccountResponseDTO.builder()
                    .message("Insufficient funds.")
                    .success(false)
                    .data(null)
                    .build();
        }

        try{
            double newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);

            bankAccountRepository.save(account);

            transactionService.addTransaction(
                    "Successful withdrawal to account No. " + account.getNumberAccount() + " for an amount of: " + amount + " . Current balance: " + account.getBalance(),
                    amount
            );

            return BankAccountResponseDTO.builder()
                    .message("Transaction type: " + account.getType() + ". Amount: " + amount + " . Current Balance: " + account.getBalance())
                    .success(true)
                    .data(null)
                    .build();

        } catch (Exception e) {
            return BankAccountResponseDTO.builder()
                    .message("An error occurred while processing the transaction. Please try again later.")
                    .success(false)
                    .data(null)
                    .build();
        }

    }
}
