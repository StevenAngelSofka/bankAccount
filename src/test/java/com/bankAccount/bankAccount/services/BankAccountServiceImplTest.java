package com.bankAccount.bankAccount.services;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.BankAccountRepository;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.bankAccount.BankAccountServiceImpl;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private BankAccount account;

    @BeforeEach
    public void setUp() {
        // Se crea una cuenta para las pruebas
        User user = new User(1L, "123456789", "John Doe", "john.doe@example.com");
        account = new BankAccount(1L, "123456", 1000.00, "SAVINGS", user);
    }

    /* getAllAccountsByUser */

    @Test
    void testGetAllAccountsByUser_Success() {
        // Configuración del mock
        User user = new User(1L, "123456", "John Doe", "john.doe@example.com");
        BankAccount account1 = new BankAccount(1L, "12345", 1000.0, "Savings", user);
        BankAccount account2 = new BankAccount(2L, "67890", 500.0, "Checking", user);
        List<BankAccount> accounts = Arrays.asList(account1, account2);

        when(bankAccountRepository.findAll()).thenReturn(accounts);

        // Llamada al método
        List<BankAccount> result = bankAccountService.getAllAccountsByUser(1L);

        // Verificación del resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("12345", result.get(0).getNumberAccount());
        assertEquals("67890", result.get(1).getNumberAccount());
    }

    /* getBalanceByAccount */

    @Test
    public void testGetBalanceByAccount_Success() {
        // Simulamos que la cuenta existe en el repositorio
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.getBalanceByAccount(1L);

        // Validamos la respuesta
        assertEquals("The balance for account : 123456 is: 1000.0", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(1000.00, response.getData());
    }

    @Test
    public void testGetBalanceByAccount_AccountNotFound() {
        // Simulamos que la cuenta no existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.getBalanceByAccount(1L);

        // Validamos la respuesta
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
        assertFalse(response.isSuccess());
    }

    /* createAccount */

    @Test
    public void testCreateAccount_Success() {
        User user = new User(1L, "123456789", "John Doe", "john.doe@example.com");
        BankAccount newAccount = new BankAccount(0L, "654321", 500.00, "CHECKING", user);

        // Simulamos que el usuario existe
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Simulamos el guardado de la cuenta
        when(bankAccountRepository.save(newAccount)).thenReturn(newAccount);

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.createAccount(1L, newAccount);

        // Validamos la respuesta
        assertEquals("Account created successfully", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(newAccount, response.getData());
    }

    @Test
    public void testCreateAccount_UserNotFound() {
        BankAccount newAccount = new BankAccount(0L, "654321", 500.00, "CHECKING", null);

        // Simulamos que el usuario no existe
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.createAccount(1L, newAccount);

        // Validamos la respuesta
        assertEquals("User with ID: 1 not found.", response.getMessage());
        assertFalse(response.isSuccess());
    }

    /* updateAccount */

    @Test
    void testUpdateAccount_Success() {
        // Configuración del mock
        BankAccount accountToUpdate = new BankAccount(1L, "12345", 1000.0, "Savings", null);
        BankAccount updatedAccount = new BankAccount(1L, "12345", 1500.0, "Checking", null);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(accountToUpdate));
        when(bankAccountRepository.save(accountToUpdate)).thenReturn(updatedAccount);

        // Llamada al método
        BankAccountResponseDTO response = bankAccountService.updateAccount(1L, updatedAccount);

        // Verificación del resultado
        assertTrue(response.isSuccess());
        assertEquals("Account updated successfully", response.getMessage());
        assertEquals(1500.0, ((BankAccount) response.getData()).getBalance());
    }

    @Test
    void testUpdateAccount_NotFound() {
        // Configuración del mock para que la cuenta no exista
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamada al método
        BankAccountResponseDTO response = bankAccountService.updateAccount(1L, new BankAccount());

        // Verificación del resultado
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
    }

    /* deleteAccount */

    @Test
    void testDeleteAccount_Success() {
        // Configuración del mock
        BankAccount accountToDelete = new BankAccount(1L, "12345", 1000.0, "Savings", null);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(accountToDelete));
        doNothing().when(bankAccountRepository).deleteById(1L);

        // Llamada al método
        BankAccountResponseDTO response = bankAccountService.deleteAccount(1L);

        // Verificación del resultado
        assertTrue(response.isSuccess());
        assertEquals("Account deleted successfully", response.getMessage());
    }

    @Test
    void testDeleteAccount_NotFound() {
        // Configuración del mock para que la cuenta no exista
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamada al método
        BankAccountResponseDTO response = bankAccountService.deleteAccount(1L);

        // Verificación del resultado
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
    }


    /* depositMoney */

    @Test
    public void testDepositMoney_Success() {
        double depositAmount = 500.00;

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Validamos la respuesta
        assertEquals("Transaction type: SAVINGS. Amount: 500.0 . Current Balance: 1500.0", response.getMessage());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testDepositMoney_InvalidAmount() {
        double depositAmount = -100.00; // Monto inválido

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Validamos la respuesta
        assertEquals("Invalid amount.", response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    public void testDepositMoney_AccountNotFound() {
        double depositAmount = 500.00;

        // Simulamos que la cuenta no existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Validamos la respuesta
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
        assertFalse(response.isSuccess());
    }

    /* withdrawMoney */

    @Test
    public void testWithdrawMoney_Success() {
        double withdrawAmount = 200.00;

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.withdrawMoney(1L, withdrawAmount);

        // Validamos la respuesta
        assertEquals("Transaction type: SAVINGS. Amount: 200.0 . Current Balance: 800.0", response.getMessage());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testWithdrawMoney_InsufficientFunds() {
        double withdrawAmount = 2000.00; // Monto superior al saldo

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Llamamos al método
        BankAccountResponseDTO response = bankAccountService.withdrawMoney(1L, withdrawAmount);

        // Validamos la respuesta
        assertEquals("Insufficient funds.", response.getMessage());
        assertFalse(response.isSuccess());
    }
}