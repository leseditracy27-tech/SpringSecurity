package com.example.crudapp.service;

import com.example.crudapp.dto.UserCreateDto;
import com.example.crudapp.dto.UserEditDto;
import com.example.crudapp.dto.UserResponseDto;
import com.example.crudapp.exception.BadRequestException;
import com.example.crudapp.exception.ResourceNotFoundException;
import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // =========================
    // BASIC USERS
    // =========================

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));
    }

    // =========================
    // CREATE USER
    // =========================

    @Override
    public User saveNewUser(UserCreateDto dto) {

        String email = dto.getEmail().toLowerCase();

        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new BadRequestException("Email already exists");
        });

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(email);
        user.setAge(dto.getAge());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // ✅ Default role = ROLE_USER only
        Set<Role> roles;

        if (dto.getRoleIds() == null || dto.getRoleIds().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() ->
                            new RuntimeException("Default role ROLE_USER not found"));

            roles = Set.of(userRole);
        } else {
            roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

            if (roles.size() != dto.getRoleIds().size()) {
                throw new BadRequestException("Invalid role IDs provided");
            }
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    // =========================
    // UPDATE USER
    // =========================

    @Override
    public User updateUser(Long id, UserEditDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        String email = dto.getEmail().toLowerCase();

        userRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("Email already exists");
            }
        });

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(email);
        user.setAge(dto.getAge());

        // ✅ Validate roles
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new BadRequestException("Invalid role IDs provided");
        }

        user.setRoles(roles);

        // ✅ Update password only if provided
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(user);
    }

    // =========================
    // DELETE USER
    // =========================

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        userRepository.delete(user);
    }

    // =========================
    // PAGINATION
    // =========================

    @Override
    public Page<User> getUsersPaginated(int page) {
        return userRepository.findAll(PageRequest.of(page, 10));
    }

    @Override
    public Page<User> searchUsersPaginated(String keyword, int page) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        keyword, keyword, PageRequest.of(page, 10)
                );
    }

    // =========================
    // STATS
    // =========================

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public long countAdmins() {
        return userRepository.countByRoles_Name("ROLE_ADMIN");
    }

    // =========================
    // AUTH SUPPORT
    // =========================

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email " + email));
    }

    @Override
    public UserResponseDto mapToDto(User user) {
        return null;
    }
}