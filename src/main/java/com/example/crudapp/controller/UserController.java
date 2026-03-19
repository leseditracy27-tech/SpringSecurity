package com.example.crudapp.controller;

import com.example.crudapp.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserController {

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/save")
    public String saveuser(@ModelAttribute("user") User user) {
        // userService.save(user); ← add later
        return "redirect:/admin";
    }
}