package com.bankAccount.bankAccount.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "secret_training_key_5T3V3N";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(String email, long id) {
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withIssuer("javaSS")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public boolean isTokenValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getEmailByUser(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }

    public Long getIdByToken(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getClaim("id").asLong();
    }

}
