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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                // Static assets
                .requestMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico").permitAll()

                // Dev-only console (ok for prototype)
                .requestMatchers("/h2-console/**").permitAll()

                // Public pages (robust: include /** to avoid trailing-slash issues)
                .requestMatchers("/", "/login", "/login/**", "/register", "/register/**").permitAll()

                // Public nets pages
                .requestMatchers(HttpMethod.GET,
                        "/nets", "/nets/",
                        "/nets/new",
                        "/nets/all"
                ).permitAll()

                // Reporting a new net (anonymous allowed)
                .requestMatchers(HttpMethod.POST, "/nets").permitAll()

                // Authenticated actions
                .requestMatchers(HttpMethod.POST,
                        "/nets/claim/**",
                        "/nets/mark-retrieved/**",
                        "/nets/mark-lost/**"
                ).authenticated()

                // Contact endpoint: Spring Security patterns do NOT support {id}
                .requestMatchers(HttpMethod.GET, "/nets/*/contact").authenticated()

                // Personal page
                .requestMatchers("/my-nets").authenticated()

                // Error page
                .requestMatchers("/error").permitAll()

                // Everything else locked down
                .anyRequest().denyAll()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout(logout -> logout.permitAll());

        http.csrf(csrf -> csrf
                // Allow H2 console frames + posts
                .ignoringRequestMatchers("/h2-console/**")
        );

        http.headers(headers -> headers
                .frameOptions(frame -> frame.disable())
        );

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
        return username -> {
            User u = users.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    u.isEnabled(),
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority(u.getRole().name()))
            );
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
