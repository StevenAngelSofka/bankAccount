package com.bankAccount.bankAccount.controllers.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create")
    public UserResponseDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/update/{id}")
    public UserResponseDTO updateUser(@PathVariable long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public UserResponseDTO deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);
    }
}
