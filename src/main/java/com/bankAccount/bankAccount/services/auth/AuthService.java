package com.bankAccount.bankAccount.services.auth;

import com.bankAccount.bankAccount.config.JwtUtil;
import com.bankAccount.bankAccount.dto.auth.AuthRequestDTO;
import com.bankAccount.bankAccount.dto.auth.AuthResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        try {
            // Autenticar las credenciales del usuario
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(loginToken);

            if(!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Incorrect credentials");
            }

            // Generar el token JWT
            String jwt = jwtUtil.generateToken(request.getEmail());

            // Construir la respuesta
            return new AuthResponseDTO(jwt);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
}
