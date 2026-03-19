package com.example.crudapp.controller;

import com.example.crudapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // 👇 THIS FIXES 404
    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users"; // your users.html
    }
}