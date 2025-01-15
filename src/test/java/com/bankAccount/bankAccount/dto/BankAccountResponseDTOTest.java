package com.bankAccount.bankAccount.dto;

import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountResponseDTOTest {

    @Test
    public void testBankAccountResponseDTOBuilder() {
        // Crear el DTO usando el builder
        BankAccountResponseDTO dto = BankAccountResponseDTO.builder()
                .message("Account created successfully")
                .success(true)
                .data("Account details here")
                .build();

        // Verificar que los valores se asignaron correctamente
        assertAll("Verify BankAccountResponseDTO properties",
                () -> assertNotNull(dto),
                () -> assertEquals("Account created successfully", dto.getMessage()),
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Account details here", dto.getData())
        );
    }

    @Test
    public void testBankAccountResponseDTOConstructor() {
        // Crear el DTO usando el constructor
        BankAccountResponseDTO dto = new BankAccountResponseDTO("Account created successfully", true, "Account details here", HttpStatus.OK);

        // Verificar que los valores se asignaron correctamente
        assertAll("Verify BankAccountResponseDTO constructor properties",
                () -> assertNotNull(dto),
                () -> assertEquals("Account created successfully", dto.getMessage()),
                () -> assertTrue(dto.isSuccess()),
                () -> assertEquals("Account details here", dto.getData())
        );
    }
}