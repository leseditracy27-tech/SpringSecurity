package com.example.crudapp.service;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // ✅ Constructor Injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Get all users
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    // ✅ Save or update user
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    // ✅ Get user by ID
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ✅ Delete user
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}