package com.bankAccount.bankAccount.services;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.user.UserServiceImpl;
import com.bankAccount.bankAccount.utils.ResponseHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ResponseHandler responseHandler;

    /* getAllUsers */
    @Test
    public void testGetAllUsers() {
        // Arrange
        // Arrange
        List<User> mockUsers = List.of(
                new User(1, "12345", "John Doe", "johndoe@example.com", "securePassword123"),
                new User(2, "67890", "Jane Smith", "janesmith@example.com", "securePassword123")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Smith", users.get(1).getName());
    }

    /* RegisterUser */
    @Test
    public void testRegisterUser_Success() {
        // Arrange
        String rawPassword = "securePassword123"; // Contraseña en texto claro
        String encodedPassword = "hashedPassword123";

        User newUser = new User(0, "54321", "Alice Johnson", "alice@example.com", rawPassword);
        User savedUser = new User(1, "54321", "Alice Johnson", "alice@example.com", encodedPassword);

        UserResponseDTO successResponse = new UserResponseDTO("User created successfully", true, savedUser, HttpStatus.OK);

        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false); // Email no en uso
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildSuccessUser para devolver un UserResponseDTO simulado
        when(responseHandler.buildSuccessUser("User created successfully", savedUser))
                .thenReturn(successResponse);

        // Act
        UserResponseDTO response = userService.registerUser(newUser);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("User created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(savedUser.getName(), ((User) response.getData()).getName());
    }

    @Test
    public void testRegisterUser_Failure_EmailAlreadyInUse() {
        // Arrange
        User newUser = new User(0, "54321", "Alice Johnson", "alice@example.com", "securePassword123");

        UserResponseDTO errorResponse = new UserResponseDTO("Email already in use", false, null, HttpStatus.BAD_REQUEST);

        // Simulación de las dependencias
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true); // Simula que el email ya está en uso

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildErrorUser para devolver un UserResponseDTO simulado
        when(responseHandler.buildErrorUser("Email already in use", HttpStatus.BAD_REQUEST))
                .thenReturn(errorResponse);

        // Act
        UserResponseDTO response = userService.registerUser(newUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Email already in use", response.getMessage());
    }

    /* updateUser */
    @Test
    public void testUpdateUser_Success() {
        // Arrange
        User existingUser = new User(1, "54321", "Alice Johnson", "alice@example.com", "securePassword123");
        User updatedUser = new User(1, "54321", "Alice Smith", "alice.smith@example.com", "securePassword123");

        UserResponseDTO successResponse = new UserResponseDTO("User updated successfully", true, updatedUser, HttpStatus.OK);

        // Simulación de las dependencias
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));  // Encuentra el usuario existente
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);  // Simula la actualización del usuario

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildSuccessUser para devolver un UserResponseDTO simulado
        when(responseHandler.buildSuccessUser("User updated successfully", updatedUser))
                .thenReturn(successResponse);
        // Act
        UserResponseDTO response = userService.updateUser(1L, updatedUser);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("Alice Smith", ((User) response.getData()).getName());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Arrange
        User updatedUser = new User(1, "54321", "Alice Smith", "alice.smith@example.com", "securePassword123");

        UserResponseDTO errorResponse = new UserResponseDTO("The user with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND);

        // Simulación de las dependencias
        when(userRepository.findById(1L)).thenReturn(Optional.empty());  // No se encuentra el usuario

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildErrorUser para devolver un UserResponseDTO simulado con error
        when(responseHandler.buildErrorUser("The user with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);
        // Act
        UserResponseDTO response = userService.updateUser(1L, updatedUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("The user with ID: 1 does not exist.", response.getMessage());
    }

    /* deleteUser */
    @Test
    public void testDeleteUser_Success() {
        // Arrange
        User existingUser = new User(1, "54321", "Alice Johnson", "alice@example.com", "securePassword123");
        UserResponseDTO successResponse = new UserResponseDTO("User deleted successfully", true, null, HttpStatus.OK);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildSuccessUser para devolver un UserResponseDTO simulado
        when(responseHandler.buildSuccessUser("User deleted successfully", null))
                .thenReturn(successResponse);

        // Act
        UserResponseDTO response = userService.deleteUser(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("User deleted successfully", response.getMessage());
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        UserResponseDTO errorResponse = new UserResponseDTO("The user with ID: 1 does not exist.", false, null, HttpStatus.NOT_FOUND);

        // Simulación de las dependencias
        when(userRepository.findById(1L)).thenReturn(Optional.empty());  // No se encuentra el usuario

        // Mock del responseHandler para ejecutar el Supplier y devolver el resultado esperado
        doAnswer(invocation -> {
            Supplier<UserResponseDTO> supplier = invocation.getArgument(0);
            return supplier.get(); // Ejecuta el Supplier y devuelve el resultado esperado
        }).when(responseHandler).executeSafelyUser(any());

        // Mock del responseHandler.buildErrorUser para devolver un UserResponseDTO simulado con error
        when(responseHandler.buildErrorUser("The user with ID: 1 does not exist.", HttpStatus.NOT_FOUND))
                .thenReturn(errorResponse);

        // Act
        UserResponseDTO response = userService.deleteUser(1L);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("The user with ID: 1 does not exist.", response.getMessage());
    }
}