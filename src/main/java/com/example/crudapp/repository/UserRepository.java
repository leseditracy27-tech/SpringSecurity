package com.example.crudapp.repository;

import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   // ✅ For login & email validation
   Optional<User> findByEmail(String email);

   // ✅ SEARCH (without pagination)
   List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
           String firstName,
           String lastName
   );

   // ✅ SEARCH (with pagination)
   Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
           String firstName,
           String lastName,
           Pageable pageable
   );

   long countByRoles_Name(String roleAdmin);
}