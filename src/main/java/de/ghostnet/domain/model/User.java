package de.ghostnet.domain.model;

import jakarta.persistence.*;

/**
 * JPA-Entität für Benutzerkonten der Anwendung.
 * Wird für Login, Rollenvergabe und einfache Aktivierung/Deaktivierung verwendet.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Eindeutiger Benutzername, dient als Login-Identität. */
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    /** Passwort-Hash (z.B. BCrypt/Argon2id), niemals im Klartext speichern. */
    @Column(nullable = false, length = 255)
    private String password;

    /** Zentrale Rolle des Benutzers, steuert Berechtigungen. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    /** Markiert, ob der Account aktiv ist (Soft-Disable möglich). */
    @Column(nullable = false)
    private boolean enabled = true;

    public User() {
        // für JPA
    }

    public User(Long id, String username, String password, Role role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

    // --- Getter & Setter ---

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public Role getRole() { 
        return role; 
    }

    public void setRole(Role role) { 
        this.role = role; 
    }

    public boolean isEnabled() { 
        return enabled; 
    }

    public void setEnabled(boolean enabled) { 
        this.enabled = enabled; 
    }
}
