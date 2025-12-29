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
 * Zentrale Security-Konfiguration der Anwendung.
 * Regelt Zugriff auf URLs, Login/Logout und Benutzerauflösung.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Definiert Zugriffregeln für HTTP-Endpunkte und konfiguriert
     * Login-Seite, Logout-Verhalten und CSRF-Schutz.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Statische Inhalte immer erlauben
                .requestMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico").permitAll()

                // Öffentliche Seiten (Landing Page, Listen, Registrierung, Login, Map)
                .requestMatchers("/", "/nets", "/nets/new", "/nets/all", "/nets/map", "/register", "/login").permitAll()

                // Neue Netze dürfen anonym gemeldet werden
                .requestMatchers(HttpMethod.POST, "/nets").permitAll()

                // Aktionen, die den Status eines Netzes ändern, erfordern Login
                .requestMatchers("/nets/claim/**",
                                "/nets/mark-retrieved/**",
                                "/nets/mark-lost/**").authenticated()

                // Persönliche Übersicht der übernommenen Netze
                .requestMatchers("/my-nets").authenticated()

                // Öffentliche Gesamtansicht aller Netze (falls genutzt)
                .requestMatchers("/nets/all").permitAll()

                // Alles andere ist standardmäßig verboten (secure by default)
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
     * Lädt Benutzer aus der Datenbank und mappt sie auf das
     * UserDetails-Modell von Spring Security.
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
     * PasswordEncoder für Hashing und Prüfung von Passwörtern.
     * Verwendet BCrypt mit Stärke 12 als pragmatischen Standard.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Perspektivisch ggf. Umstieg auf Argon2
    }
}
