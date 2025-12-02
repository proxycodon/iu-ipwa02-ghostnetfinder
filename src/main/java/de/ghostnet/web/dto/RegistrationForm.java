package de.ghostnet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO für das Registrierungsformular.
 * Wird von Spring Validation geprüft, bevor ein Benutzerkonto angelegt wird.
 */
public class RegistrationForm {

    /** Gewünschter Benutzername für den Login. */
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    /** Erstes Passwortfeld (Klartext im Formular, später gehasht gespeichert). */
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    /** Wiederholung des Passworts zur einfachen Plausibilitätsprüfung. */
    @NotBlank
    @Size(min = 6, max = 100)
    private String confirmPassword;

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

    public String getConfirmPassword() { 
        return confirmPassword; 
    }

    public void setConfirmPassword(String confirmPassword) { 
        this.confirmPassword = confirmPassword; 
    }
}
