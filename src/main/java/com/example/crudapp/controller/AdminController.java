package com.example.crudapp.controller;

import jakarta.validation.Valid;
import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // ✅ List all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "listUsers";
    }

    // ✅ Show add/edit form
    @GetMapping("/form")
    public String userForm(@RequestParam(value = "id", required = false) Long id, Model model) {

        User user = (id != null)
                ? userService.getUserById(id)
                : new User();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());

        return "user-form";
    }

    // ✅ Save user
    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                           Model model) {

        Set<Long> safeRoleIds = (roleIds != null) ? roleIds : new HashSet<>();

        // ✅ Roles validation
        if (safeRoleIds.isEmpty()) {
            result.rejectValue("roles", "error.user", "At least one role is required");
        }

        // ✅ Password required ONLY for new users
        if (user.getId() == null &&
                (user.getPassword() == null || user.getPassword().isEmpty())) {
            result.rejectValue("password", "error.user", "Password is required");
        }

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        User existingUser;

        if (user.getId() != null) {
            // ✅ EDIT
            existingUser = userService.getUserById(user.getId());

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());

            // ✅ Only set password if provided (RAW, NOT encoded)
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }

        } else {
            // ✅ NEW USER (RAW password)
            existingUser = user;
        }

        // ✅ Assign roles
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(safeRoleIds));
        existingUser.setRoles(roles);

        // ✅ SAVE (encoding happens in service)
        try {
            userService.saveUser(existingUser);
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        return "redirect:/admin";
    }

    // ✅ Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}