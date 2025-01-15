package com.bankAccount.bankAccount.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    public void testUserConstructor() {
        // Crear un objeto User usando el constructor
        User user = new User(1L, "12345", "John Doe", "john.doe@example.com", "securePassword123");

        assertAll("Verify User constructor properties",
                () -> assertNotNull(user),
                () -> assertEquals(1L, user.getIdUser()),
                () -> assertEquals("12345", user.getIdentificationNumber()),
                () -> assertEquals("John Doe", user.getName()),
                () -> assertEquals("john.doe@example.com", user.getEmail())
        );
    }

    @Test
    public void testUserSettersAndGetters() {
        // Crear un objeto User
        User user = new User();
        user.setIdUser(1L);
        user.setIdentificationNumber("12345");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        // Verificar que los setters y getters funcionan correctamente
        assertAll("Verify User setters and getters",
                () -> assertEquals(1L, user.getIdUser()),
                () -> assertEquals("12345", user.getIdentificationNumber()),
                () -> assertEquals("John Doe", user.getName()),
                () -> assertEquals("john.doe@example.com", user.getEmail())
        );
    }
}