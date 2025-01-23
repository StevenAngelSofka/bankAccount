package com.bankAccount.bankAccount.controllers.auth;

import com.bankAccount.bankAccount.dto.auth.AuthRequestDTO;
import com.bankAccount.bankAccount.dto.auth.AuthResponseDTO;
import com.bankAccount.bankAccount.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.authenticate(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // 200 OK con el DTO
        } else {
            return ResponseEntity.badRequest().body(response); // 400 Bad Request con el DTO
        }
    }

}
