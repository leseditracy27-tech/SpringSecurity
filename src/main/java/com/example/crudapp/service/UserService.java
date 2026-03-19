package com.example.crudapp.service;

import com.example.crudapp.model.User;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<User> findAll();

    void save (User user);

    User getUserById(Long id);

    void deleteUser(Long id);
}