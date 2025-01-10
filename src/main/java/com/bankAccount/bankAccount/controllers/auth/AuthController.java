package com.bankAccount.bankAccount.controllers.auth;

import com.bankAccount.bankAccount.dto.auth.AuthRequestDTO;
import com.bankAccount.bankAccount.dto.auth.AuthResponseDTO;
import com.bankAccount.bankAccount.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {
        return authService.authenticate(request);
    }

}
