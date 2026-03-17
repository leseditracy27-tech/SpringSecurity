package com.example.crudapp.controller;

import com.example.crudapp.model.User;
import com.example.crudapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // List all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", service.getAllUsers());
        return "users";
    }

    // Show form (Add or Edit)
    @GetMapping({"/form", "/form/{id}"})
    public String showForm(@PathVariable(required = false) Long id, Model model) {
        User user = (id != null) ? service.getUserById(id) : new User();
        model.addAttribute("user", user);
        return "user-form";  // only one HTML form for both Add & Edit
    }

    // Save or update user
    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-form";  // if errors, stay on same form
        }
        service.saveUser(user);
        return "redirect:/admin";
    }

    // Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }
}