package com.bankAccount.bankAccount.controllers;

import com.bankAccount.bankAccount.controllers.user.UserController;
import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AllArgsConstructor
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        List<User> mockUsers = List.of(
                User.builder()
                        .idUser(1L)
                        .name("John")
                        .email("john.doe@example.com")
                        .build(),
                User.builder()
                        .idUser(2L)
                        .name("Jane")
                        .email("jane.smith@example.com")
                        .build()
        );

        // Simulamos la llamada al servicio
        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idUser").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"));
    }


    @Test
    void testCreateUser() throws Exception {
        // Arrange
        long userId = 1L;

        User user = User.builder()
                .identificationNumber("1.033.763.458")
                .name("Steven Angel")
                .email("steven.angel@example.com")
                .build();

        // DTO esperado
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .message("User created successfully")
                .success(true)
                .data(user)
                .build();

        // Simulamos la llamada al servicio
        when(userService.createUser(user)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.identificationNumber").value("1.033.763.458"))
                .andExpect(jsonPath("$.data.name").value("Steven Angel"))
                .andExpect(jsonPath("$.data.email").value("steven.angel@example.com"));
    }


    @Test
    void testUpdateUser() throws Exception {
        // Arrange
        long userId = 1L;
        User updatedUser = User.builder()
                .identificationNumber("1.033.789.987")
                .name("Updated Name")
                .email("updated.email@example.com")
                .build();

        // DTO esperado
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .message("User updated successfully")
                .success(true)
                .data(updatedUser)
                .build();

        // Simulamos la llamada al servicio
        when(userService.updateUser(userId, updatedUser)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/users/update/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.identificationNumber").value("1.033.789.987"))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.email").value("updated.email@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        long userId = 1L;

        // DTO esperado
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .message("User deleted successfully")
                .success(true)
                .data(null)
                .build();

        // Simulamos la llamada al servicio
        when(userService.deleteUser(userId)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/users/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.success").value(true));
    }
}