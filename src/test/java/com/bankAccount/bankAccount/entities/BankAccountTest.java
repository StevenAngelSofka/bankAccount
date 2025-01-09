package com.bankAccount.bankAccount.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    public void testBankAccountConstructor() {
        User user = new User();
        user.setIdUser(1L);

        BankAccount bankAccount = new BankAccount(1L, "ACC12345", 1000.00, "Savings", user);

        assertNotNull(bankAccount);
        assertEquals(1L, bankAccount.getIdAccount());
        assertEquals("ACC12345", bankAccount.getNumberAccount());
        assertEquals(1000.00, bankAccount.getBalance());
        assertEquals("Savings", bankAccount.getType());
        assertEquals(1L, bankAccount.getUser().getIdUser());
    }

    @Test
    public void testBankAccountSettersAndGetters() {
        User user = new User();
        user.setIdUser(1L);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdAccount(1L);
        bankAccount.setNumberAccount("ACC67890");
        bankAccount.setBalance(500.00);
        bankAccount.setType("Checking");
        bankAccount.setUser(user);

        assertEquals(1L, bankAccount.getIdAccount());
        assertEquals("ACC67890", bankAccount.getNumberAccount());
        assertEquals(500.00, bankAccount.getBalance());
        assertEquals("Checking", bankAccount.getType());
        assertEquals(1L, bankAccount.getUser().getIdUser());
    }
}