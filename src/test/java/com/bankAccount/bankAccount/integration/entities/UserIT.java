package com.bankAccount.bankAccount.integration.entities;

import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AllArgsConstructor
public class UserIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserPersistence() {
        // Crear un objeto User
        User user = User.builder()
                .identificationNumber("12345")
                .name("John Doe")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        // Guardar el usuario en la base de datos
        user = userRepository.save(user);

        // Verificar que el usuario se haya guardado correctamente
        assertNotNull(user);
        assertEquals("12345", user.getIdentificationNumber());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testUserWithoutId() {
        // Crear un objeto User sin ID
        User user = User.builder()
                .identificationNumber("67890")
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .password("securePassword123")
                .build();

        // Guardar el usuario en la base de datos
        user = userRepository.save(user);

        // Verificar que el ID se haya generado automáticamente
        assertEquals("67890", user.getIdentificationNumber());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
    }
}
