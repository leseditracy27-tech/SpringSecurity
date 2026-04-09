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

            Role adminRole = roleRepository.findAll()
                    .stream().filter(r -> r.getName().equals("ROLE_ADMIN"))
                    .findFirst().orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            Role userRole = roleRepository.findAll()
                    .stream().filter(r -> r.getName().equals("ROLE_USER"))
                    .findFirst().orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            if (userRepository.findByEmail("admin@mail.com").isEmpty()) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@mail.com");
                admin.setAge(30);
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);
            }
        };
    }
}