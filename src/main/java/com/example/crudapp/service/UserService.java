package com.example.crudapp.service;

import com.example.crudapp.model.Users;
import com.example.crudapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection: Spring Boot automatically provides UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Save or update a user
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    // Find a user by ID
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}