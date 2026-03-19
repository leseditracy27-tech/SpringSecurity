package com.example.crudapp.repository;

import com.example.crudapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


   Optional< User> findByEmail(String email);

}