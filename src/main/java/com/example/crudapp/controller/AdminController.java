package com.example.crudapp.controller;

import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    // ✅ Save user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(value = "roleIds", required = false) Set<Long> roleIds) {

        if (roleIds != null) {
            Set<Role> roles = roleRepository.findAllById(roleIds)
                    .stream()
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }

    // ✅ Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}