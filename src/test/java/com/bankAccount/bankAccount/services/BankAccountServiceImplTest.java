package com.bankAccount.bankAccount.services;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.BankAccountRepository;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.bankAccount.BankAccountServiceImpl;
import com.bankAccount.bankAccount.services.transaction.TransactionService;
import com.bankAccount.bankAccount.utils.ResponseHandler;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ResponseHandler responseHandler;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private BankAccount account;

    @BeforeEach
    public void setUp() {
        // Se crea una cuenta para las pruebas
        User user = new User(1L, "123456789", "John Doe", "john.doe@example.com", "securePassword123");
        account = new BankAccount(1L, "123456", 1000.00, "SAVINGS", user);
    }

    /* getAllAccountsByUser */

    @Test
    void testGetAllAccountsByUser_Success() {
        // Configuración del mock
        User user = new User(1L, "123456", "John Doe", "john.doe@example.com", "securePassword123");
        BankAccount account1 = new BankAccount(1L, "12345", 1000.0, "Savings", user);
        BankAccount account2 = new BankAccount(2L, "67890", 500.0, "Checking", user);
        List<BankAccount> accounts = Arrays.asList(account1, account2);

        when(bankAccountRepository.findAll()).thenReturn(accounts);

        // Llamada al método
        List<BankAccount> result = bankAccountService.getAllAccountsByUser(1L);

        // Verificación del resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(account -> "12345".equals(account.getNumberAccount())));
        assertTrue(result.stream().anyMatch(account -> "67890".equals(account.getNumberAccount())));
    }

    /* getBalanceByAccount */

    @Test
    public void testGetBalanceByAccount_Success() {
        User user = new User(1, "54321", "Alice Johnson", "alice@example.com", "securePassword123");
        BankAccount account = new BankAccount(1L, "123456", 1000.00, "checking", user);

        BankAccountResponseDTO successResponse = new BankAccountResponseDTO("The balance for account : 123456 is: 1000.0", true, 1000.00, HttpStatus.OK);

        // Simulamos que la cuenta existe en el repositorio
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyAccount(any());

        // Mock del responseHandler.buildSuccessBankAccount para devolver un BankAccountResponseDTO simulado
        when(responseHandler.buildSuccessAccount("The balance for account : 123456 is: 1000.0", 1000.00))
                .thenReturn(successResponse);

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

        // Mock del responseHandler para manejar el escenario de error
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "The account with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND
        );
        when(responseHandler.buildErrorAccount("The account with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.getBalanceByAccount(1L);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
        assertNull(response.getData());
    }

    /* createAccount */

    @Test
    public void testCreateAccount_Success() {
        User user = new User(1L, "123456789", "John Doe", "john.doe@example.com", "securePassword123");
        BankAccount newAccount = new BankAccount(0L, "654321", 500.00, "CHECKING", user);

        // Simulamos que el usuario existe
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Simulamos el guardado de la cuenta
        when(bankAccountRepository.save(newAccount)).thenReturn(newAccount);

        // Mock para la creación exitosa de la cuenta
        BankAccountResponseDTO successResponse = new BankAccountResponseDTO(
                "Account created successfully", true, newAccount, HttpStatus.CREATED
        );

        // Usamos lenient para permitir stubbings no utilizados
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta exitosa
        when(responseHandler.buildSuccessAccount("Account created successfully", newAccount))
                .thenReturn(successResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.createAccount(1L, newAccount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertTrue(response.isSuccess());
        assertEquals("Account created successfully", response.getMessage());
        assertEquals(newAccount, response.getData());
    }

    @Test
    public void testCreateAccount_UserNotFound() {
        BankAccount newAccount = new BankAccount(0L, "654321", 500.00, "CHECKING", null);

        // Simulamos que el usuario no existe
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Simulamos la respuesta de error cuando el usuario no se encuentra
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "User with ID: 1 not found.", false, null, HttpStatus.NOT_FOUND
        );

        // Usamos lenient para permitir stubbings no utilizados
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de error
        when(responseHandler.buildErrorAccount("User with ID: 1 not found.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.createAccount(1L, newAccount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess());
        assertEquals("User with ID: 1 not found.", response.getMessage());
    }

    /* updateAccount */

    @Test
    void testUpdateAccount_Success() {
        // Arrange
        BankAccount existingAccount = new BankAccount(1L, "123456", 500.00, "CHECKING", null);
        BankAccount updatedAccount = new BankAccount(1L, "654321", 600.00, "SAVINGS", null);

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        // Simulamos la actualización de la cuenta
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(updatedAccount);

        // Simulamos la respuesta de éxito
        BankAccountResponseDTO successResponse = new BankAccountResponseDTO(
                "Account updated successfully", true, updatedAccount, HttpStatus.OK
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de éxito
        when(responseHandler.buildSuccessAccount("Account updated successfully", updatedAccount))
                .thenReturn(successResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.updateAccount(1L, updatedAccount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertTrue(response.isSuccess());
        assertEquals("Account updated successfully", response.getMessage());
        assertEquals(updatedAccount.getNumberAccount(), ((BankAccount) response.getData()).getNumberAccount());
        assertEquals(updatedAccount.getType(), ((BankAccount) response.getData()).getType());
    }

    @Test
    void testUpdateAccount_NotFound() {
        // Arrange
        BankAccount updatedAccount = new BankAccount(1L, "654321", 600.00, "SAVINGS", null);

        // Simulamos que la cuenta no existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Simulamos la respuesta de error
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "The account with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de error
        when(responseHandler.buildErrorAccount("The account with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.updateAccount(1L, updatedAccount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
    }

    /* deleteAccount */

    @Test
    void testDeleteAccount_Success() {
        // Arrange
        BankAccount existingAccount = new BankAccount(1L, "123456", 500.00, "CHECKING", null);

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        // Simulamos que la cuenta se elimina
        doNothing().when(bankAccountRepository).deleteById(1L);

        // Simulamos la respuesta de éxito
        BankAccountResponseDTO successResponse = new BankAccountResponseDTO(
                "Account deleted successfully", true, null, HttpStatus.OK
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de éxito
        when(responseHandler.buildSuccessAccount("Account deleted successfully", null))
                .thenReturn(successResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.deleteAccount(1L);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertTrue(response.isSuccess());
        assertEquals("Account deleted successfully", response.getMessage());
        verify(bankAccountRepository).deleteById(1L);
    }

    @Test
    void testDeleteAccount_NotFound() {
        // Arrange
        // Simulamos que la cuenta no existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Simulamos la respuesta de error
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "The account with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de error
        when(responseHandler.buildErrorAccount("The account with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.deleteAccount(1L);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
    }


    /* depositMoney */

    @Test
    public void testDepositMoney_Success() {
        // Arrange
        BankAccount existingAccount = new BankAccount(1L, "123456", 1000.00, "CHECKING", null);
        double depositAmount = 500.00;

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        // Simulamos la actualización de la cuenta
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(existingAccount);

        // Simulamos el servicio de transacciones
        doNothing().when(transactionService).addTransaction(anyString(), eq(depositAmount));

        // Simulamos la respuesta de éxito
        BankAccountResponseDTO successResponse = new BankAccountResponseDTO(
                "Transaction type: CHECKING. Amount: 500.0 . Current Balance: 1500.0", true, existingAccount, HttpStatus.OK
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de éxito
        when(responseHandler.buildSuccessAccount(any(), any())).thenReturn(successResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Transaction type: CHECKING. Amount: 500.0 . Current Balance: 1500.0", response.getMessage());
        verify(bankAccountRepository).save(existingAccount); // Verificamos que se haya guardado la cuenta
        verify(transactionService).addTransaction(anyString(), eq(depositAmount));
    }

    @Test
    public void testDepositMoney_InvalidAmount() {
        // Arrange
        double depositAmount = -500.00; // Monto inválido

        // Simulamos la respuesta de error directamente en el responseHandler
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "Invalid amount.", false, null, HttpStatus.BAD_REQUEST
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver la respuesta de error
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve la respuesta esperada
        });

        // Simulamos que el monto no es válido, y devolvemos el error predefinido
        when(responseHandler.buildErrorAccount("Invalid amount.", HttpStatus.BAD_REQUEST))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess()); // Debe fallar
        assertEquals("Invalid amount.", response.getMessage());
    }

    @Test
    public void testDepositMoney_AccountNotFound() {
        // Arrange
        double depositAmount = 500.00;

        // Simulamos que la cuenta no existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        // Simulamos la respuesta de error
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "The account with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de error
        when(responseHandler.buildErrorAccount("The account with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.depositMoney(1L, depositAmount);

        // Assert
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("The account with ID: 1 does not exist.", response.getMessage());
    }

    /* withdrawMoney */

    @Test
    public void testWithdrawMoney_Success() {
        // Arrange
        BankAccount existingAccount = new BankAccount(1L, "123456", 1000.00, "CHECKING", null);
        double withdrawalAmount = 500.00;

        // Simulamos que la cuenta existe
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        // Simulamos la actualización de la cuenta
        double updatedBalance = existingAccount.getBalance() - withdrawalAmount;
        existingAccount.setBalance(updatedBalance);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(existingAccount);

        // Simulamos el servicio de transacciones
        doNothing().when(transactionService).addTransaction(anyString(), eq(withdrawalAmount));

        // Simulamos la respuesta de éxito
        BankAccountResponseDTO successResponse = new BankAccountResponseDTO(
                "Transaction type: CHECKING. Amount: 500.0 . Current Balance: 500.0",
                true,
                existingAccount,
                HttpStatus.OK
        );

        // Simulamos el responseHandler para ejecutar el Supplier y devolver el resultado esperado
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        });

        // Simulamos la construcción de la respuesta de éxito
        when(responseHandler.buildSuccessAccount(any(), any())).thenReturn(successResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.withdrawMoney(1L, withdrawalAmount);

        // Assert
        assertNotNull(response); // Verifica que la respuesta no es nula
        assertTrue(response.isSuccess()); // Verifica que la operación fue exitosa
        assertEquals("Transaction type: CHECKING. Amount: 500.0 . Current Balance: 500.0", response.getMessage());
        verify(bankAccountRepository).save(existingAccount); // Verificamos que la cuenta haya sido guardada con el nuevo balance
        verify(transactionService).addTransaction(anyString(), eq(withdrawalAmount));
    }

    @Test
    public void testWithdrawMoney_InvalidAmount() {
        // Arrange
        double invalidAmount = -500.00; // Monto inválido
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "Invalid amount.", false, null, HttpStatus.BAD_REQUEST
        );

        // Simulamos que el responseHandler maneja el error de monto inválido
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el error esperado
        });

        // Simulamos que el responseHandler devuelve el error por monto inválido
        when(responseHandler.buildErrorAccount("Invalid amount.", HttpStatus.BAD_REQUEST))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.withdrawMoney(1L, invalidAmount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess()); // Debe fallar
        assertEquals("Invalid amount.", response.getMessage()); // El mensaje debe coincidir
    }

    @Test
    public void testWithdrawMoney_InsufficientFunds() {
        // Arrange
        double withdrawalAmount = 2000.00; // Monto que excede el saldo
        BankAccount existingAccount = new BankAccount(1L, "123456", 1000.00, "CHECKING", null); // Saldo insuficiente
        BankAccountResponseDTO errorResponse = new BankAccountResponseDTO(
                "Insufficient funds.", false, null, HttpStatus.BAD_REQUEST
        );

        // Simulamos que la cuenta existe en el repositorio
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));

        // Simulamos que el responseHandler maneja el error de fondos insuficientes
        when(responseHandler.executeSafelyAccount(any())).thenAnswer(invocation -> {
            Supplier<BankAccountResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el error esperado
        });

        // Simulamos que el responseHandler devuelve el error por fondos insuficientes
        when(responseHandler.buildErrorAccount("Insufficient funds.", HttpStatus.BAD_REQUEST))
                .thenReturn(errorResponse);

        // Act
        BankAccountResponseDTO response = bankAccountService.withdrawMoney(1L, withdrawalAmount);

        // Assert
        assertNotNull(response); // Aseguramos que la respuesta no sea null
        assertFalse(response.isSuccess()); // Debe fallar
        assertEquals("Insufficient funds.", response.getMessage());
    }
}