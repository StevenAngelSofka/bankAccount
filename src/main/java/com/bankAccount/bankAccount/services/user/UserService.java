package com.bankAccount.bankAccount.services.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserResponseDTO registerUser(User user);

    UserResponseDTO updateUser(long idUser, User user);

    UserResponseDTO deleteUser(long idUser);

    UserResponseDTO getUserById(long id);
}
