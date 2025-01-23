package com.bankAccount.bankAccount.dto;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

    @Test
    public void testUserResponseDTOBuilder() {
        // Crear el DTO usando el builder
        UserResponseDTO dto = UserResponseDTO.builder()
                .message("Success")
                .success(true)
                .data("Some data")
                .build();

        // Verificar que los valores se asignaron correctamente
        assertAll("Verify UserResponseDTO builder properties",
                () -> assertNotNull(dto),
                () -> assertEquals("Success", dto.getMessage()),
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Some data", dto.getData())
        );
    }

    @Test
    public void testUserResponseDTOConstructor() {
        // Crear el DTO usando el constructor
        UserResponseDTO dto = new UserResponseDTO("Success", true, "Some data", HttpStatus.OK);

        // Verificar que los valores se asignaron correctamente
        assertAll("Verify UserResponseDTO constructor properties",
                () -> assertNotNull(dto),
                () -> assertEquals("Success", dto.getMessage()),
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Some data", dto.getData())
        );
    }
}