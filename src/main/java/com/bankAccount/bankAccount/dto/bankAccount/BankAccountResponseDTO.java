package com.bankAccount.bankAccount.dto.bankAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BankAccountResponseDTO {
    private String message;
    private boolean success;
    private Object data;
}
