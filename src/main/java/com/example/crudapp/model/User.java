package com.example.crudapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;

    @Column(name="last_name")
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;

    @Column(name="email", unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @Column(name="age")
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be between 1 and 120")
    @Max(value = 120, message = "Age must be between 1 and 120")
    private Integer age;

    // 🔐 PASSWORD (FIXED)
    @Column(name = "password")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    // 🔐 ROLES
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String firstName, String lastName, String email, Integer age, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    // ===== SPRING SECURITY =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles; // roles already implement GrantedAuthority
    }

    @Override
    public String getUsername() {
        return email; // login uses email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}