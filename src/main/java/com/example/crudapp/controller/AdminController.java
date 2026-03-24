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

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // List all users
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users"; // Thymeleaf template
    }

    // Show add/edit form
    @GetMapping("/form")
    public String userForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        User user = (id != null) ? userService.getUserById(id) : new User();
        model.addAttribute("user", user);

        // send all roles to the form
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);

        return "user-form";
    }

    // Save user
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, @RequestParam("roleIds") Set<Long> roleIds) {
        Set<Role> roles = roleRepository.findAllById(roleIds).stream().collect(java.util.stream.Collectors.toSet());
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    // Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}