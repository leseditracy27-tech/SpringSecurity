package com.example.crudapp.controller;

import com.example.crudapp.model.User;
import com.example.crudapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userHome(Model model,
                           @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

        String email = userDetails.getUsername().toLowerCase(); // 🔥 FIX

        User user = userService.findByEmail(email);

        model.addAttribute("user", user);

        return "user";
    }
}