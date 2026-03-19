package com.example.crudapp.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ROLE_ADMIN or ROLE_USER

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    // 🔐 REQUIRED by Spring Security
    @Override
    public String getAuthority() {
        return name;
    }

    // ✅ Getters & Setters (IMPORTANT)

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}