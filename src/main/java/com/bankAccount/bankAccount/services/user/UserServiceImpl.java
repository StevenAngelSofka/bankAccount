package com.bankAccount.bankAccount.services.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDTO registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return UserResponseDTO.builder()
                    .message("Email already in use")
                    .success(false)
                    .build();
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User createdUser = userRepository.save(user);

            return UserResponseDTO.builder()
                    .message("User created successfully")
                    .success(true)
                    .data(createdUser)
                    .build();
        } catch (Exception e) {
            return UserResponseDTO.builder()
                    .message("Error creating user: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @Override
    public UserResponseDTO updateUser(long idUser, User user) {
        try{
            Optional<User> userDB = userRepository.findById(idUser);

            if(userDB.isEmpty()) {
                return UserResponseDTO.builder()
                        .message("The user with ID: " + idUser + " does not exist.")
                        .success(false)
                        .build();
            }

            User userToUpdate = userDB.get();
            userToUpdate.setIdentificationNumber(user.getIdentificationNumber());
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());

            User updatedUser = userRepository.save(userToUpdate);

            return UserResponseDTO.builder()
                    .message("User updated successfully")
                    .success(true)
                    .data(updatedUser)
                    .build();
        } catch (Exception e) {
            return UserResponseDTO.builder()
                    .message("Error updating user: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @Override
    public UserResponseDTO deleteUser(long idUser) {

        Optional<User> userDB = userRepository.findById(idUser);

        if(userDB.isEmpty()) {
            return UserResponseDTO.builder()
                    .message("The user with ID: " + idUser + " does not exist.")
                    .success(false)
                    .build();
        }

        try {
            userRepository.deleteById(idUser);

            return UserResponseDTO.builder()
                    .message("User deleted successfully")
                    .success(true)
                    .data(null)
                    .build();
        } catch (Exception e) {
            return UserResponseDTO.builder()
                    .message("Error deleting user: " + e.getMessage())
                    .success(false)
                    .build();
        }
    }
}
