package de.ghostnet.config;

import de.ghostnet.domain.model.User;
import de.ghostnet.domain.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * Central Security configuration of the application.
 * Manages access to URLs, login/logout, and user resolution.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Defines access rules for HTTP endpoints and configures
     * login page, logout behavior, and CSRF protection.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Always allow static content
                .requestMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico").permitAll()

                // Public pages (landing page, lists, registration, login, map)
                .requestMatchers("/", "/nets", "/nets/new", "/nets/all", "/nets/map", "/register", "/login").permitAll()

                // New nets can be reported anonymously
                .requestMatchers(HttpMethod.POST, "/nets").permitAll()

                // Actions that change the status of a net require login
                .requestMatchers("/nets/claim/**",
                                "/nets/mark-retrieved/**",
                                "/nets/mark-lost/**").authenticated()

                .requestMatchers(HttpMethod.GET, "/nets/{id}/contact").authenticated()

                // Personal overview of claimed nets
                .requestMatchers("/my-nets").authenticated()

                // Public overall view of all nets (if used)
                .requestMatchers("/nets/all").permitAll()

                // Everything else is forbidden by default (secure by default)
                .anyRequest().denyAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(Customizer.withDefaults())
            .csrf(Customizer.withDefaults());

        return http.build();
    }


    /**
     * Loads users from the database and maps them to Spring Security's
     * UserDetails model.
     */
    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
        return username -> {
            User u = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.isEnabled(),
                true,   // accountNonExpired
                true,   // credentialsNonExpired
                true,   // accountNonLocked
                List.of(new SimpleGrantedAuthority(u.getRole().name()))
            );
        };
    }

    /**
     * PasswordEncoder for hashing and verifying passwords.
     * Uses BCrypt with strength 12 as a pragmatic default.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Potential future switch to Argon2
    }
}
