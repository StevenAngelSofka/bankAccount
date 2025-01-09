package com.bankAccount.bankAccount.dto;

import com.bankAccount.bankAccount.dto.transaction.TransactionResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseDTOTest {

    @Test
    public void testTransactionResponseDTOBuilder() {
        // Crear el DTO usando el builder
        LocalDateTime currentDate = LocalDateTime.now();
        TransactionResponseDTO dto = TransactionResponseDTO.builder()
                .message("Transaction successful")
                .success(true)
                .amount(100.50)
                .date(currentDate)
                .build();

        // Verificar que los valores se asignaron correctamente
        assertNotNull(dto);
        assertEquals("Transaction successful", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals(100.50, dto.getAmount());
        assertEquals(currentDate, dto.getDate());
    }

    @Test
    public void testTransactionResponseDTOConstructor() {
        // Crear el DTO usando el constructor
        LocalDateTime currentDate = LocalDateTime.now();
        TransactionResponseDTO dto = new TransactionResponseDTO("Transaction successful", true, 100.50, currentDate);

        // Verificar que los valores se asignaron correctamente
        assertNotNull(dto);
        assertEquals("Transaction successful", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals(100.50, dto.getAmount());
        assertEquals(currentDate, dto.getDate());
    }

}