package com.example.crudapp.service;

import com.example.crudapp.dto.UserCreateDto;
import com.example.crudapp.dto.UserEditDto;
import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;
import com.example.crudapp.dto.UserResponseDto;
import java.util.List;

public interface UserService {

    // =======================
    // CORE CRUD (REST STYLE)
    // =======================

    List<User> getAllUsers();

    User getUserById(Long id);

    User saveNewUser(UserCreateDto dto);

    User updateUser(Long id, UserEditDto dto);

    void deleteUser(Long id);

    // =======================
    // PAGINATION + SEARCH
    // =======================

    Page<User> getUsersPaginated(int page);

    Page<User> searchUsersPaginated(String keyword, int page);

    // =======================
    // STATS
    // =======================

    long countUsers();

    long countAdmins();

    // =======================
    // AUTH SUPPORT
    // =======================

    User findByEmail(String email);

    UserResponseDto mapToDto(User user);
}