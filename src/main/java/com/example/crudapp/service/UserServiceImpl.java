package com.example.crudapp.service;

import com.example.crudapp.model.User;
import com.example.crudapp.repository.UserRepository;
import org.springframework.data.domain.Page;              // ✅ NEW
import org.springframework.data.domain.PageRequest;       // ✅ NEW
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
                user.setPassword(dbUser.getPassword()); // keep old password
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

    // ✅ PAGINATION
    @Override
    public Page<User> getUsersPaginated(int page) {
        return userRepository.findAll(PageRequest.of(page, 5));
    }

    // ✅ SEARCH + PAGINATION
    @Override
    public Page<User> searchUsersPaginated(String keyword, int page) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, PageRequest.of(page, 5)
                );
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public long countAdmins() {
        return userRepository.countByRoles_Name("ROLE_ADMIN");
    }
}