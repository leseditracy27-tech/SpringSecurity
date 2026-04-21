package com.example.crudapp.dto;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;

    private List<Long> roleIds;
    private List<String> roles; // raw roles (e.g. ROLE_ADMIN)

    public UserResponseDto(Long id, String firstName, String lastName,
                           String email, Integer age,
                           List<Long> roleIds,
                           List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.roleIds = roleIds;
        this.roles = roles;
    }

    // ================= GETTERS =================

    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public Integer getAge() { return age; }

    public List<Long> getRoleIds() { return roleIds; }

    public List<String> getRoles() { return roles; }

    // ================= DISPLAY FIELD (KEY FIX) =================

    public String getSimpleRoles() {
        if (roles == null) return "";

        return roles.stream()
                .map(r -> r.replace("ROLE_", ""))
                .collect(Collectors.joining(" "));
    }
}