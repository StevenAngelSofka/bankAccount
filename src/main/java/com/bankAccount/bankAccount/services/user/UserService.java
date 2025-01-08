package com.bankAccount.bankAccount.services.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserResponseDTO createUser(User user);

    UserResponseDTO updateUser(long idUser, User user);

    UserResponseDTO deleteUser(long idUser);
}
