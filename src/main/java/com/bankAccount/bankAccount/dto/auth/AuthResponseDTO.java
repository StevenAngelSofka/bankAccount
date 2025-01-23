package com.bankAccount.bankAccount.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String message;
    private boolean success;
    private String token;
    private HttpStatus httpStatus;
}
