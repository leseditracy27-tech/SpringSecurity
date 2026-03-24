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

@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name="first_name")
    @NotEmpty(message = "First name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must not contain digits")
    private String firstName;

    @Getter
    @Column(name="last_name")
    @NotEmpty(message = "Last name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must not contain digits")
    private String lastName;

    @Getter
    @Column(name="email", unique = true)
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @Getter
    @Column(name="age")
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be between 1 and 120")
    @Max(value = 120, message = "Age must be between 1 and 120")
    private Integer age;

    // 🔐 Password for login
    @Column(name = "password")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    // 🔐 Roles (ADMIN / USER)
    @Getter
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

    // ===== Getters & Setters =====

    // ===== Spring Security Methods =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // login with email
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