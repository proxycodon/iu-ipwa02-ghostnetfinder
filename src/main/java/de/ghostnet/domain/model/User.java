package de.ghostnet.domain.model;

import jakarta.persistence.*;

/**
 * JPA-Entity for user accounts of the application.
 * Used for login, role assignment, and simple activation/deactivation.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username, serves as login identity. */
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    /** Password hash (e.g., BCrypt/Argon2id), never store in plain text. */
    @Column(nullable = false, length = 255)
    private String password;

    /** Phone number for contact between salvagers. */
    @Column(name = "phone_number", nullable = false, length = 30)
    private String phoneNumber;

    /** Central role of the user, controls permissions. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    /** Marks whether the account is active (soft-disable possible). */
    @Column(nullable = false)
    private boolean enabled = true;

    public User() {
        // for JPA
    }

    public User(Long id, String username, String password, String phoneNumber, Role role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
 
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
