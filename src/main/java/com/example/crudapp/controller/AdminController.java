package com.example.crudapp.controller;

import com.example.crudapp.dto.UserCreateDto;
import com.example.crudapp.dto.UserEditDto;
import com.example.crudapp.model.User;
import com.example.crudapp.repository.RoleRepository;
import com.example.crudapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

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

        model.addAttribute("user", new UserCreateDto());
        model.addAttribute("editUser", new UserEditDto());

        return "listUsers";
    }

    @GetMapping("/form")
    public String userForm(Model model) {
        model.addAttribute("user", new UserCreateDto());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user-form";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserCreateDto userDto,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        try {
            userService.saveNewUser(userDto);
        } catch (RuntimeException e) {
            if ("Email already exists".equals(e.getMessage())) {
                result.rejectValue("email", "error.user", "Email already exists");
            }
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("editUser") UserEditDto userDto,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("users", userService.getUsersPaginated(0).getContent());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("user", new UserCreateDto());
            model.addAttribute("editUser", userDto);
            model.addAttribute("showEditModal", true);
            return "listUsers";
        }

        try {
            userService.updateUser(userDto);
        } catch (RuntimeException e) {
            if ("Email already exists".equals(e.getMessage())) {
                result.rejectValue("email", "error.user", "Email already exists");
            }

            model.addAttribute("users", userService.getUsersPaginated(0).getContent());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("user", new UserCreateDto());
            model.addAttribute("editUser", userDto);
            model.addAttribute("showEditModal", true);

            return "listUsers";
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}


