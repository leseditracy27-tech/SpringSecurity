package com.example.crudapp.controller;

import com.example.crudapp.repository.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoleRepository roleRepository;

    public AdminController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String adminPage(Model model) {

        // Only keep what frontend still needs
        model.addAttribute("allRoles", roleRepository.findAll());

        return "listUsers";
    }
}