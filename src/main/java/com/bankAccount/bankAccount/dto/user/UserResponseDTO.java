package com.bankAccount.bankAccount.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String message;
    private boolean success;
    private Object data;
}
