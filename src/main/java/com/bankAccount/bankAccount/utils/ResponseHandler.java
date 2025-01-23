package com.bankAccount.bankAccount.utils;

import com.bankAccount.bankAccount.dto.auth.AuthResponseDTO;
import com.bankAccount.bankAccount.dto.bankAccount.BankAccountDTO;
import com.bankAccount.bankAccount.dto.bankAccount.BankAccountResponseDTO;
import com.bankAccount.bankAccount.dto.user.UserDTO;
import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.BankAccount;
import com.bankAccount.bankAccount.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class ResponseHandler {

    public <T> UserResponseDTO buildSuccessUser(String message, T data) {
        Object user = data;

        if (data instanceof User userData) {
            user = UserDTO.builder()
                    .idUser(userData.getIdUser())
                    .identificationNumber(userData.getIdentificationNumber())
                    .name(userData.getName())
                    .email(userData.getEmail())
                    .build();
        }

        return UserResponseDTO.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .data(user)
                .build();
    }

    public <T> BankAccountResponseDTO buildSuccessAccount(String message, T data) {
        Object account = data;

        if (data instanceof BankAccount accountData) {
                account = BankAccountDTO.builder()
                        .idAccount(accountData.getIdAccount())
                        .numberAccount(accountData.getNumberAccount())
                        .balance(accountData.getBalance())
                        .type(accountData.getType())
                        .build();
        }

        return BankAccountResponseDTO.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .data(account)
                .build();
    }

    public AuthResponseDTO buildSuccessAuth(String message, String token) {

        return AuthResponseDTO.builder()
                .message(message)
                .success(true)
                .httpStatus(HttpStatus.OK)
                .token(token)
                .build();
    }

    public UserResponseDTO buildErrorUser(String message, HttpStatus status) {
        return UserResponseDTO.builder()
                .message(message)
                .success(false)
                .httpStatus(status)
                .data(null)
                .build();
    }

    public BankAccountResponseDTO buildErrorAccount(String message, HttpStatus status) {
        return BankAccountResponseDTO.builder()
                .message(message)
                .success(false)
                .httpStatus(status)
                .data(null)
                .build();
    }

    public AuthResponseDTO buildErrorAuth(String message, HttpStatus status) {
        return AuthResponseDTO.builder()
                .message(message)
                .success(false)
                .httpStatus(status)
                .token(null)
                .build();
    }

    public UserResponseDTO executeSafelyUser(Supplier<UserResponseDTO> action) {
        try {
            return action.get();
        } catch (Exception e) {
            return buildErrorUser("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BankAccountResponseDTO executeSafelyAccount(Supplier<BankAccountResponseDTO> action) {
        try {
            return action.get();
        } catch (Exception e) {
            return buildErrorAccount("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AuthResponseDTO executeSafelyAuth(Supplier<AuthResponseDTO> action) {
        try {
            return action.get();
        } catch (Exception e) {
            return buildErrorAuth("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
