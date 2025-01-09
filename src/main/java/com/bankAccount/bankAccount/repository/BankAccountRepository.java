package com.bankAccount.bankAccount.repository;

import com.bankAccount.bankAccount.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
