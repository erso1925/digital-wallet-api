package com.ersenpamuk.walletapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Enables use of @PreAuthorize for role-based method access control
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                // This user has EMPLOYEE role and can manage wallets of all customers
                User.withUsername("employee999")
                        .password("{noop}password") // {noop} = plain text password (for demo only)
                        .roles("EMPLOYEE")
                        .build(),

                // These users have CUSTOMER role and can only manage their own wallets
                User.withUsername("customer999")
                        .password("{noop}password")
                        .roles("CUSTOMER")
                        .build(),

                User.withUsername("customer1000")
                        .password("{noop}password2")
                        .roles("CUSTOMER")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF disabled for simplicity (not recommended for production)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // All endpoints require authentication
                )
                .httpBasic(httpBasic -> {}); // Use basic authentication for demo

        return http.build();
    }
}
