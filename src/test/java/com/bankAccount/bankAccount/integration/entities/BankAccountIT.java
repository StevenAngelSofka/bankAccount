package com.bankAccount.bankAccount.integration.entities;

import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.BankAccountRepository;
import com.bankAccount.bankAccount.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AllArgsConstructor
class BankAccountIT {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository; // Repositorio de User, para probar la relación ManyToOne

    @Test
    public void testBankAccountPersistence() {
        // Crear un objeto User para asociarlo a BankAccount
        User user = new User();
        user.setIdentificationNumber("12345");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securePassword123");
        userRepository.save(user);

        // Crear un objeto BankAccount
        BankAccount bankAccount = BankAccount.builder()
                .numberAccount("ACC123456789")
                .balance(1000.00)
                .type("Savings")
                .user(user) // Asociar al usuario creado
                .build();

        // Guardar el BankAccount
        bankAccount = bankAccountRepository.save(bankAccount);

        // Verificar que el objeto BankAccount se haya guardado correctamente
        assertNotNull(bankAccount);
        assertEquals("ACC123456789", bankAccount.getNumberAccount());
        assertEquals(1000.00, bankAccount.getBalance());
        assertEquals("Savings", bankAccount.getType());
        assertEquals(user.getIdUser(), bankAccount.getUser().getIdUser()); // Verificar relación con el usuario
    }

    @Test
    public void testBankAccountWithoutUser() {
        // Crear un objeto BankAccount sin usuario
        BankAccount bankAccount = BankAccount.builder()
                .numberAccount("ACC987654321")
                .balance(500.00)
                .type("Checking")
                .build();

        // Guardar el BankAccount
        bankAccount = bankAccountRepository.save(bankAccount);

        // Verificar que el objeto BankAccount se haya guardado correctamente
        assertNotNull(bankAccount);
        assertEquals("ACC987654321", bankAccount.getNumberAccount());
        assertEquals(500.00, bankAccount.getBalance());
        assertNull(bankAccount.getUser()); // No debe haber un usuario asociado
    }
}