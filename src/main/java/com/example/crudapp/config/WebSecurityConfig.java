package com.example.crudapp.config;

import com.example.crudapp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 🔐 Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Authentication provider
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 🔐 Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC
                        .requestMatchers("/login", "/css/**").permitAll()

                        // ADMIN ONLY
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // USER + ADMIN
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // everything else
                        .anyRequest().authenticated()
                )

                // 🔐 LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email") // login with email
                        .defaultSuccessUrl("/admin", true) // you can change later
                        .permitAll()
                )

                // 🔐 LOGOUT
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}