package de.ghostnet.web;

import de.ghostnet.service.UserService;
import de.ghostnet.web.dto.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller für die Registrierung neuer Benutzerkonten.
 * Kapselt Formularanzeige, Validierung und Delegation an den UserService.
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Zeigt das Registrierungsformular an.
     */
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";
    }

    /**
     * Verarbeitet das Registrierungsformular.
     * Prüft Formularvalidierung, Passwortbestätigung und Weiterleitung an den UserService.
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form,
                           BindingResult br,
                           Model model) {

        // Einfache Plausibilitätsprüfung: beide Passwortfelder müssen übereinstimmen
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }

        // Bei Validierungsfehlern Formular erneut anzeigen
        if (br.hasErrors()) {
            return "register";
        }

        try {
            userService.registerReporter(form.getUsername(), form.getPassword());
        } catch (IllegalArgumentException e) {
            // Benutzername bereits vergeben → Fehler am Feld anzeigen
            br.rejectValue("username", "exists", e.getMessage());
            return "register";
        }

        // Nach erfolgreicher Registrierung zur Login-Seite umleiten
        model.addAttribute("registered", true);
        return "redirect:/login";
    }
}
