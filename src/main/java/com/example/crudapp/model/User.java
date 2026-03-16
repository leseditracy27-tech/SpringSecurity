package com.example.crudapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    @NotEmpty(message = "First name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must not contain digits")
    private String firstName;

    @Column(name="last_name")
    @NotEmpty(message = "Last name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must not contain digits")
    private String lastName;

    @Column(name="email")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    private String email;

    @Column(name="age")
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be between 1 and 120")
    @Max(value = 120, message = "Age must be between 1 and 120")
    private Integer age;

    public User(){}

    public User(String firstName,String lastName,String email,Integer age){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.age=age;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id=id; }

    public String getFirstName(){ return firstName; }
    public void setFirstName(String firstName){ this.firstName=firstName; }

    public String getLastName(){ return lastName; }
    public void setLastName(String lastName){ this.lastName=lastName; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email=email; }

    public Integer getAge(){ return age; }
    public void setAge(Integer age){ this.age=age; }
}