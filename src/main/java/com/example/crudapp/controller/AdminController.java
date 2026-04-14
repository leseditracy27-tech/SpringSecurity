package com.example.crudapp.controller;

import jakarta.validation.Valid;
import com.example.crudapp.model.Role;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.service.UserService;
import org.springframework.data.domain.Page;
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

    // ✅ LIST USERS (FIXED)
    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String keyword) {

        Page<User> userPage;

        if (keyword != null && !keyword.isEmpty()) {
            userPage = userService.searchUsersPaginated(keyword, page);
        } else {
            userPage = userService.getUsersPaginated(page);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalAdmins", userService.countAdmins());
        model.addAttribute("allRoles", roleRepository.findAll());

        // 🔥 IMPORTANT FIX
        model.addAttribute("user", new User());

        return "listUsers";
    }

    // ✅ SHOW FORM (optional page, still works)
    @GetMapping("/form")
    public String userForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        User user = (id != null)
                ? userService.getUserById(id)
                : new User();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user-form";
    }

    // ✅ SAVE USER (FULLY FIXED)
    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                           Model model) {

        Set<Long> safeRoleIds = (roleIds != null) ? new HashSet<>(roleIds) : new HashSet<>();

        // ✅ Roles validation
        if (safeRoleIds.isEmpty()) {
            result.rejectValue("roles", "error.user", "At least one role is required");
        }

        // ✅ Password validation (only for NEW user)
        if (user.getId() == null && (user.getPassword() == null || user.getPassword().isEmpty())) {
            result.rejectValue("password", "error.user", "Password is required");
        }

        // ❌ VALIDATION FAILED → RETURN SAME PAGE (IMPORTANT)
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getUsersPaginated(0).getContent());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("user", user);
            return "listUsers"; // 🔥 FIXED
        }

        // ✅ Set roles
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(safeRoleIds));
        user.setRoles(roles);
        System.out.println("SAVING USER: " + user.getEmail());


        try {
            userService.saveUser(user);
        } catch (RuntimeException e) {

            // ✅ Email already exists
            if ("Email already exists".equals(e.getMessage())) {
                result.rejectValue("email", "error.user", "Email already exists");
            }

            model.addAttribute("users", userService.getUsersPaginated(0).getContent());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("user", user);

            return "listUsers"; // 🔥 FIXED
        }

        return "redirect:/admin";
    }

    // ✅ DELETE USER
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}