package com.example.crudapp.controller;

import com.example.crudapp.dto.*;
import com.example.crudapp.exception.BadRequestException;
import com.example.crudapp.exception.ResourceNotFoundException;
import com.example.crudapp.model.User;
import com.example.crudapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // ✅ MAPPER
    // =========================
    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                user.getRoles().stream().map(r -> r.getId()).toList(),
                user.getRoles().stream().map(r -> r.getName()).toList()
        );
    }

    // =========================
    // ✅ GET USERS
    // =========================
    @GetMapping
    public PageResponse<UserResponseDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword) {

        Page<User> users = keyword.isBlank()
                ? userService.getUsersPaginated(page)
                : userService.searchUsersPaginated(keyword, page);

        Page<UserResponseDto> result = users.map(this::mapToDto);

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getTotalPages(),
                result.getTotalElements()
        );
    }

    // =========================
    // ✅ GET USER
    // =========================
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return mapToDto(userService.getUserById(id));
    }

    // =========================
    // ✅ CREATE
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserCreateDto dto) {
        return mapToDto(userService.saveNewUser(dto));
    }

    // =========================
    // ✅ UPDATE
    // =========================
    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody @Valid UserEditDto dto) {
        return mapToDto(userService.updateUser(id, dto));
    }

    // =========================
    // ✅ DELETE
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // =========================
    // ✅ VALIDATION (400)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    // =========================
    // ✅ BAD REQUEST (400)
    // =========================
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(BadRequestException ex) {
        return Map.of("error", ex.getMessage());
    }

    // =========================
    // ✅ NOT FOUND (404)
    // =========================
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    // =========================
    // ✅ GENERAL (500)
    // =========================
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneral(Exception ex) {
        return Map.of("error", "Something went wrong");
    }
}