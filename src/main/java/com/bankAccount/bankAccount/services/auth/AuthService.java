package com.bankAccount.bankAccount.services.auth;

import com.bankAccount.bankAccount.config.JwtUtil;
import com.bankAccount.bankAccount.dto.auth.AuthRequestDTO;
import com.bankAccount.bankAccount.dto.auth.AuthResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.utils.ResponseHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResponseHandler responseHandler;

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        return responseHandler.executeSafelyAuth(() -> {
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(loginToken);

            if (!authentication.isAuthenticated()) {
                return responseHandler.buildErrorAuth("Incorrect credentials", HttpStatus.UNAUTHORIZED);
            }

            Optional<User> userDB = userRepository.findByEmail(request.getEmail());

            if (userDB.isEmpty()) {
                return responseHandler.buildErrorAuth("User not found with email: " + request.getEmail(), HttpStatus.NOT_FOUND);
            }

            User user = userDB.get();

            // Generar el token JWT
            String jwt = jwtUtil.generateToken(user.getEmail(), user.getIdUser());

            // Construir la respuesta exitosa
            return responseHandler.buildSuccessAuth("Authentication successful", jwt);
        });
    }
}
