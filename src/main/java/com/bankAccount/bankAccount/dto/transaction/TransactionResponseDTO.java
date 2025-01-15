package com.bankAccount.bankAccount.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
    private String message;
    private boolean success;
    private Double amount;
    private LocalDateTime date;
    private HttpStatus httpStatus;
}
