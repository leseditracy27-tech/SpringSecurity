package com.example.crudapp.dao;

import com.example.crudapp.model.User;
import java.util.List;

public interface UserDao {

    <User> List<User> findAll();

    <User> User findId(Long id);

    void save(User user);

    void delete(Long id);
}