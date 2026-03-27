package com.example.crudapp.controller;

import jakarta.validation.Valid;
import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ List all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "listUsers"; // ✅ IMPORTANT (match folder)
    }

    // ✅ Show add/edit form
    @GetMapping("/form")
    public String userForm(@RequestParam(value = "id", required = false) Long id, Model model) {

        User user = (id != null)
                ? userService.getUserById(id)
                : new User();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());

        return "user-form"; // ✅ IMPORTANT (match folder)
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                           Model model) {

        // ✅ Make roleIds safe (never null)
        Set<Long> safeRoleIds = (roleIds != null) ? roleIds : new HashSet<>();

        // ✅ Roles validation
        if (safeRoleIds.isEmpty()) {
            result.rejectValue("roles", "error.user", "At least one role is required");
        }

        // ❌ If validation fails → return form
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        User existingUser;

        // ✅ EDIT USER
        if (user.getId() != null) {

            existingUser = userService.getUserById(user.getId());

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());

            // 🔐 Password logic
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

        } else {
            // ✅ NEW USER
            existingUser = user;
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // ✅ Use safeRoleIds here
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(safeRoleIds));
        existingUser.setRoles(roles);

        userService.saveUser(existingUser);

        return "redirect:/admin";
    }


    // ✅ Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}