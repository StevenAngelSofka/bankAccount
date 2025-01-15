package com.bankAccount.bankAccount.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long idUser;
    private String identificationNumber;
    private String name;
    private String email;
}
