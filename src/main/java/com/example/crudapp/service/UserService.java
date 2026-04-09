package com.example.crudapp.service;

import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(Long id);

    void deleteUser(Long id);

    // Pagination
    Page<User> getUsersPaginated(int page);

    // Search + Pagination
    Page<User> searchUsersPaginated(String keyword, int page);

    long countUsers();

    long countAdmins();
    User findByEmail(String email);
}