package com.bankAccount.bankAccount.controllers.bankAccount;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.services.bankAccount.BankAccountService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("/get-accounts-by-user/{id}")
    private List<BankAccount> getAllAccountsByUser(@PathVariable long id) {
        return bankAccountService.getAllAccountsByUser(id);
    }

    @GetMapping("/get-balance-by-account/{id}")
    private BankAccountResponseDTO getBalanceByAccount(@PathVariable long id) {
        return bankAccountService.getBalanceByAccount(id);
    }

    @PostMapping("/create/{idUser}")
    private BankAccountResponseDTO createAccount(@PathVariable long idUser, @RequestBody BankAccount account) {
        return bankAccountService.createAccount(idUser, account);
    }

    @PostMapping("/deposit/{id}")
    private BankAccountResponseDTO depositMoney(@PathVariable long id, @RequestBody JsonNode body) {
        double amount = body.get("amount").asDouble();
        return bankAccountService.depositMoney(id, amount);
    }

    @PostMapping("/withdraw/{id}")
    private BankAccountResponseDTO withdrawMoney(@PathVariable long id, @RequestBody JsonNode body) {
        double amount = body.get("amount").asDouble();
        return bankAccountService.withdrawMoney(id, amount);
    }

    @PutMapping("/update/{id}")
    private BankAccountResponseDTO updateAccount(@PathVariable long id, @RequestBody BankAccount account) {
        return bankAccountService.updateAccount(id, account);
    }

    @DeleteMapping("/delete/{id}")
    private BankAccountResponseDTO deleteAccount(@PathVariable long id) {
        return  bankAccountService.deleteAccount(id);
    }

}
