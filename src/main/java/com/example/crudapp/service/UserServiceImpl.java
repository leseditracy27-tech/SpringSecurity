package com.example.crudapp.service;

import com.example.crudapp.dto.UserCreateDto;
import com.example.crudapp.dto.UserEditDto;
import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;




@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        System.out.println("SERVICE SAVE CALLED: " + user.getEmail());

        // ✅ normalize email
        user.setEmail(user.getEmail().toLowerCase());

        // ✅ check duplicate email
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            if (user.getId() == null || !existing.getId().equals(user.getId())) {
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
                user.setPassword(passwordEncoder.encode(user.getPassword())); // encode new
            }

        } else {
            // 🆕 NEW USER
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public void saveNewUser(UserCreateDto dto) {
        String email = dto.getEmail().toLowerCase();

        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new RuntimeException("Email already exists");
        });

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(email);
        user.setAge(dto.getAge());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public void updateUser(UserEditDto dto) {
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String email = dto.getEmail().toLowerCase();

        userRepository.findByEmail(email).ifPresent(user -> {
            if (!user.getId().equals(dto.getId())) {
                throw new RuntimeException("Email already exists");
            }
        });

        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(email);
        existingUser.setAge(dto.getAge());

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        existingUser.setRoles(roles);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(existingUser);
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

    @Override
    public Page<User> getUsersPaginated(int page) {
        return userRepository.findAll(PageRequest.of(page, 100));
    }

    @Override
    public Page<User> searchUsersPaginated(String keyword, int page) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        keyword, keyword,
                        PageRequest.of(page, 5)
                );
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public long countAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(r -> r.getName().equals("ROLE_ADMIN")))
                .count();
    }

    // ✅ FINAL ADDITION (REQUIRED FOR USER PAGE)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}