package com.bankAccount.bankAccount.services.bankAccount;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.repository.BankAccountRepository;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import com.bankAccount.bankAccount.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ResponseHandler responseHandler;

    @Override
    public List<BankAccount> getAllAccountsByUser(long idUser) {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccountResponseDTO getBalanceByAccount(long idAccount) {
        return responseHandler.executeSafelyAccount(() ->
            bankAccountRepository.findById(idAccount)
                .map(account ->
                    responseHandler.buildSuccessAccount("The balance for account : " + account.getNumberAccount() + " is: " + account.getBalance(), account.getBalance())
                )
                .orElseGet(() ->
                    responseHandler.buildErrorAccount("The account with ID: " + idAccount + " does not exist.", HttpStatus.NOT_FOUND)
                )
        );
    }

    @Override
    public BankAccountResponseDTO createAccount(long idUser, BankAccount account) {
        return responseHandler.executeSafelyAccount(() ->
            userRepository.findById(idUser)
                .map(user -> {
                    account.setUser(user);
                    BankAccount createdAccount = bankAccountRepository.save(account);
                    return responseHandler.buildSuccessAccount("Account created successfully", createdAccount);
                })
                .orElseGet(() -> responseHandler.buildErrorAccount("User with ID: " + idUser + " not found.", HttpStatus.NOT_FOUND))
        );
    }

    @Override
    public BankAccountResponseDTO updateAccount(long idAccount, BankAccount account) {
        return responseHandler.executeSafelyAccount(() ->
            bankAccountRepository.findById(idAccount)
                .map(accountDB -> {
                    accountDB.setNumberAccount(account.getNumberAccount());
                    accountDB.setType(account.getType());
                    BankAccount updatedAccount = bankAccountRepository.save(accountDB);
                    return responseHandler.buildSuccessAccount("Account updated successfully", updatedAccount);
                })
                .orElseGet(() -> responseHandler.buildErrorAccount("The account with ID: " + idAccount + " does not exist.", HttpStatus.NOT_FOUND))
        );
    }

    @Override
    public BankAccountResponseDTO deleteAccount(long idAccount) {
        return responseHandler.executeSafelyAccount(() ->
            bankAccountRepository.findById(idAccount)
                .map(accountDB -> {
                    bankAccountRepository.deleteById(idAccount);
                    return responseHandler.buildSuccessAccount("Account deleted successfully", null);
                })
                .orElseGet(() -> responseHandler.buildErrorAccount("The account with ID: " + idAccount + " does not exist.", HttpStatus.NOT_FOUND))
        );
    }

    @Override
    public BankAccountResponseDTO depositMoney(long idAccount, double amount) {
        return responseHandler.executeSafelyAccount(() -> {
            if(amount <= 0) return responseHandler.buildErrorAccount("Invalid amount.", HttpStatus.BAD_REQUEST);

            return bankAccountRepository.findById(idAccount)
                    .map(accountDB -> {
                        double newBalance = accountDB.getBalance() + amount;
                        accountDB.setBalance(newBalance);
                        bankAccountRepository.save(accountDB);

                        transactionService.addTransaction(
                                "Successful deposit to account No. " + accountDB.getNumberAccount() + " for an amount of: " + amount + " . Current balance: " + accountDB.getBalance(), amount
                        );

                        return responseHandler.buildSuccessAccount("Transaction type: " + accountDB.getType() + ". Amount: " + amount + " . Current Balance: " + accountDB.getBalance(), accountDB);
                    })
                    .orElseGet(() -> responseHandler.buildErrorAccount("The account with ID: " + idAccount + " does not exist.", HttpStatus.NOT_FOUND));
        });
    }

    @Override
    public BankAccountResponseDTO withdrawMoney(long idAccount, double amount) {
        return responseHandler.executeSafelyAccount(() -> {
            if(amount <= 0) return responseHandler.buildErrorAccount("Invalid amount.", HttpStatus.BAD_REQUEST);

            return bankAccountRepository.findById(idAccount)
                .map(accountDB -> {
                    if(amount > accountDB.getBalance()) return responseHandler.buildErrorAccount("Insufficient funds.", HttpStatus.BAD_REQUEST);

                    double newBalance = accountDB.getBalance() - amount;
                    accountDB.setBalance(newBalance);
                    bankAccountRepository.save(accountDB);

                    transactionService.addTransaction(
                            "Successful withdrawal to account No. " + accountDB.getNumberAccount() + " for an amount of: " + amount + " . Current balance: " + accountDB.getBalance(), amount
                    );

                    return responseHandler.buildSuccessAccount("Transaction type: " + accountDB.getType() + ". Amount: " + amount + " . Current Balance: " + accountDB.getBalance(), null);
                })
                .orElseGet(() -> responseHandler.buildErrorAccount("The account with ID: " + idAccount + " does not exist.", HttpStatus.NOT_FOUND));
        });
    }
}
