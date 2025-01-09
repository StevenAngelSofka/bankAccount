package com.bankAccount.bankAccount.services;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.services.user.UserServiceImpl;
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
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /* getAllUsers */
    @Test
    public void testGetAllUsers() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User(1, "12345", "John Doe", "johndoe@example.com"),
                new User(2, "67890", "Jane Smith", "janesmith@example.com")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Smith", users.get(1).getName());
    }

    /* createUser */
    @Test
    public void testCreateUser_Success() {
        // Arrange
        User newUser = new User(0, "54321", "Alice Johnson", "alice@example.com");
        User savedUser = new User(1, "54321", "Alice Johnson", "alice@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponseDTO response = userService.createUser(newUser);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(savedUser.getName(), ((User) response.getData()).getName());
    }

    @Test
    public void testCreateUser_Failure() {
        // Arrange
        User newUser = new User(0, "54321", "Alice Johnson", "alice@example.com");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        UserResponseDTO response = userService.createUser(newUser);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Error creating user: Database error", response.getMessage());
    }

    /* updateUser */
    @Test
    public void testUpdateUser_Success() {
        // Arrange
        User existingUser = new User(1, "54321", "Alice Johnson", "alice@example.com");
        User updatedUser = new User(1, "54321", "Alice Smith", "alice.smith@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

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
        User updatedUser = new User(1, "54321", "Alice Smith", "alice.smith@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

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
        User existingUser = new User(1, "54321", "Alice Johnson", "alice@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act
        UserResponseDTO response = userService.deleteUser(1L);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("User deleted successfully", response.getMessage());
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        UserResponseDTO response = userService.deleteUser(1L);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("The user with ID: 1 does not exist.", response.getMessage());
    }
}