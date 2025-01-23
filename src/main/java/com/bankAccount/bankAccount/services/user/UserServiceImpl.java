package com.bankAccount.bankAccount.services.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.repository.UserRepository;
import com.bankAccount.bankAccount.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    ResponseHandler responseHandler;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDTO registerUser(User user) {
        return responseHandler.executeSafelyUser(() ->
                Optional.of(user)
                    .filter(u -> !userRepository.existsByEmail(u.getEmail()))
                    .map( u -> {
                        u.setPassword(passwordEncoder.encode(u.getPassword()));
                        User createdUser = userRepository.save(u);
                        return responseHandler.buildSuccessUser("User created successfully", createdUser);
                    })
                    .orElseGet(() -> responseHandler.buildErrorUser("Email already in use", HttpStatus.BAD_REQUEST))
        );
    }


    @Override
    public UserResponseDTO updateUser(long idUser, User user) {
        return responseHandler.executeSafelyUser(() ->
                userRepository.findById(idUser)
                    .map(userDB -> {
                        userDB.setIdentificationNumber(user.getIdentificationNumber());
                        userDB.setName(user.getName());
                        userDB.setEmail(user.getEmail());

                        User updatedUser = userRepository.save(userDB);
                        return responseHandler.buildSuccessUser("User updated successfully", updatedUser);
                    })
                    .orElseGet(() -> responseHandler.buildErrorUser("The user with ID: " + idUser + " does not exist.", HttpStatus.NOT_FOUND))
        );
    }

    @Override
    public UserResponseDTO deleteUser(long idUser) {
        return responseHandler.executeSafelyUser(() ->
                userRepository.findById(idUser)
                        .map(userDB -> {
                            userRepository.deleteById(idUser);
                            return responseHandler.buildSuccessUser("User deleted successfully", null);
                        })
                        .orElseGet(() -> responseHandler.buildErrorUser("The user with ID: " + idUser + " does not exist.", HttpStatus.NOT_FOUND))
        );
    }

    @Override
    public UserResponseDTO getUserById(long id) {
        return responseHandler.executeSafelyUser(() ->
                userRepository.findById(id)
                        .map(userDB -> responseHandler.buildSuccessUser("User found successfully", userDB))
                        .orElseGet(() -> responseHandler.buildErrorUser("The user with ID: " + id + " does not exist.", HttpStatus.NOT_FOUND))
        );
    }
}
