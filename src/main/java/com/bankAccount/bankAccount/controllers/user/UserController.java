package com.bankAccount.bankAccount.controllers.user;

import com.bankAccount.bankAccount.dto.user.UserResponseDTO;
import com.bankAccount.bankAccount.entities.User;
import com.bankAccount.bankAccount.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        UserResponseDTO response =  userService.getUserById(id);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user) {
        UserResponseDTO response =  userService.registerUser(user);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // 200 OK con el DTO
        } else {
            return ResponseEntity.badRequest().body(response); // 400 Bad Request con el DTO
        }
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
