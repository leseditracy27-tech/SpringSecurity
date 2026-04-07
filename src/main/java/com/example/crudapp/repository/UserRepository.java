package com.example.crudapp.repository;

import com.example.crudapp.model.User;
import org.springframework.data.domain.Page;        // ✅ ADD
import org.springframework.data.domain.Pageable;    // ✅ ADD
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   // ✅ Existing
   Optional<User> findByEmail(String email);

   // ✅ ADD THIS (search + pagination)
   Page<User> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
           String firstName,
           String email,
           Pageable pageable
   );
   // ✅ ADD THIS HERE (dashboard stats)
   long countByRoles_Name(String roleName);
}