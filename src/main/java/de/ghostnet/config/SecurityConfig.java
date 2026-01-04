package de.ghostnet.config;

import de.ghostnet.domain.model.User;
import de.ghostnet.domain.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .requestMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/", "/nets", "/nets/new", "/nets/all", "/nets/map",
                             "/register", "/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/nets").permitAll()
            .requestMatchers("/nets/claim/**",
                             "/nets/mark-retrieved/**",
                             "/nets/mark-lost/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/nets/{id}/contact").authenticated()
            .requestMatchers("/my-nets").authenticated()
            .anyRequest().denyAll()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .permitAll()
        )
        .logout(logout -> logout.permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
        )
        .headers(headers -> headers
            .frameOptions(frame -> frame.disable())
        );

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
