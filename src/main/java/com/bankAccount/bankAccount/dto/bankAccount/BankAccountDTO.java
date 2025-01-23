package com.bankAccount.bankAccount.dto.bankAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BankAccountDTO {
    private long idAccount;
    private String numberAccount;
    private double balance;
    private String type;
}
