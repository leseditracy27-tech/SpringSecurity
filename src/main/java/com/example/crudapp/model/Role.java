package com.example.crudapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Getter
@Setter // ✅ add this to avoid partial setters later
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // ✅ BEST PRACTICE
    private String name; // ROLE_ADMIN or ROLE_USER

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    // 🔐 Spring Security uses this
    @Override
    public String getAuthority() {
        return name;
    }

    // ✅ Important for Sets (you are using Set<Role>)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}