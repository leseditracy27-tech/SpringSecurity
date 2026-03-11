package com.example.crudapp.controller;

import com.example.crudapp.model.Users;
import com.example.crudapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")  // base URL for all endpoints
public class UserController {

    private final UserService userService;

    // Constructor injection: Spring Boot provides UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users → get all users
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /users/{id} → get user by ID
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // POST /users → create a new user
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.saveUser(user);
    }

    // DELETE /users/{id} → delete a user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
