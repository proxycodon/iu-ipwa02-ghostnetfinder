package de.ghostnet.service;

import de.ghostnet.domain.model.Role;
import de.ghostnet.domain.model.User;
import de.ghostnet.domain.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service für Benutzerverwaltung und Rollenlogik.
 * Kapselt Registrierung und einfache Rollenwechsel.
 */
@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    /**
     * Registriert einen neuen Benutzer mit der Rolle REPORTER.
     * Wirft eine IllegalArgumentException, falls der Benutzername bereits vergeben ist.
     */
    @Transactional
    public void registerReporter(String username, String rawPassword) {
        if (users.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User u = new User(null, username, encoder.encode(rawPassword), Role.ROLE_REPORTER, true);
        users.save(u);
    }

    /**
     * Hebt die Rolle eines Benutzers von REPORTER auf SALVAGER an,
     * falls der Benutzer aktuell noch Reporter ist.
     * Wird u.a. beim ersten Claim eines Netzes verwendet.
     */
    @Transactional
    public void promoteToSalvagerIfReporter(String username) {
        users.findByUsername(username).ifPresent(u -> {
            if (u.getRole() == Role.ROLE_REPORTER) {
                u.setRole(Role.ROLE_SALVAGER);
            }
        });
    }
}
