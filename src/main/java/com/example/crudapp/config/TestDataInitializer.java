package com.example.crudapp.config;

import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class TestDataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            // ✅ Create roles if not exist
            Role adminRole = roleRepository.findAll()
                    .stream()
                    .filter(r -> r.getAuthority().equals("ROLE_ADMIN"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_ADMIN");
                        return roleRepository.save(r);
                    });

            Role userRole = roleRepository.findAll()
                    .stream()
                    .filter(r -> r.getAuthority().equals("ROLE_USER"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role r = new Role();
                        r.setName("ROLE_USER");
                        return roleRepository.save(r);
                    });

            // ✅ Create ADMIN user
            if (userRepository.findByEmail("admin@mail.com") == null) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@mail.com");
                admin.setAge(30);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
            }

            // ✅ Create NORMAL USER
            if (userRepository.findByEmail("user@mail.com") == null) {
                User user = new User();
                user.setFirstName("Normal");
                user.setLastName("User");
                user.setEmail("user@mail.com");
                user.setAge(25);
                user.setPassword(passwordEncoder.encode("user"));
                user.setRoles(Set.of(userRole));

                userRepository.save(user);
            }
        };
    }
}