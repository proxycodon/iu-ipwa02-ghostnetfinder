package de.ghostnet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    /** Telefonnummer zur Kontaktaufnahme zwischen Bergenden. */
    @NotBlank
    @Size(max = 30)
    @Pattern(
        regexp = "^[0-9+()\\s\\-/.]{6,30}$",
        message = "{phone.invalid}"
    )
    private String phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
