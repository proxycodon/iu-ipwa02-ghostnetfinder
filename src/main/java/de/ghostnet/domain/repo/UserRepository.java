package de.ghostnet.domain.repo;

import de.ghostnet.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository für Benutzerkonten.
 * Wird primär für Login und User-Verwaltung genutzt.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Sucht einen Benutzer anhand seines Benutzernamens.
     */
    Optional<User> findByUsername(String username);
}
