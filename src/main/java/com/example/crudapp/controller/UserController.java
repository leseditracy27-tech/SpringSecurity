package com.example.crudapp.controller;

import com.example.crudapp.model.User;
import com.example.crudapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String createUserForm(Model model){
        model.addAttribute("user", new User());
        return "form";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result){

        if(result.hasErrors()){
            return "form";
        }

        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/users";
    }
}