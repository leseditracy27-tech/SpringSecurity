package com.example.crudapp.dto;

import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

public class UserEditDto {

    @NotNull
    private Long id;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters allowed")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters allowed")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Minimum age is 1")
    @Max(value = 120, message = "Maximum age is 120")
    private Integer age;

    // ✅ IMPORTANT FIX: allow null password on edit
    private String password;

    // ❗ IMPORTANT FIX: avoid validation crash when JS sends empty array
    private Set<Long> roleIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName != null ? firstName.trim() : null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName != null ? lastName.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // allow blank password on edit (DO NOT force validation)
        this.password = (password == null || password.isBlank()) ? null : password;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = (roleIds != null) ? roleIds : new HashSet<>();
    }
}