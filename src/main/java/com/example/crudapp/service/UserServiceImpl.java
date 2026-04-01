package com.example.crudapp.service;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public void saveUser(User user) {

        // ✅ Normalize email
        user.setEmail(user.getEmail().toLowerCase());

        // ✅ Email uniqueness check
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(user.getId())) {
                throw new RuntimeException("Email already exists");
            }
        });

        if (user.getId() != null) {
            // 🔄 EDIT USER
            User dbUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(dbUser.getPassword()); // keep old
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

        } else {
            // 🆕 NEW USER
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
            userRepository.save(user);
    }



    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}