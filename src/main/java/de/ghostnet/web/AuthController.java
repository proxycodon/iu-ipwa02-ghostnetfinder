package de.ghostnet.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller für Authentifizierungs-Views (Login-Seite).
 * Die eigentliche Login-Logik wird von Spring Security übernommen.
 */
@Controller
public class AuthController {

    /**
     * Rendert die Login-Seite.
     * Spring Security wertet hier u.a. Fehler-Parameter und Logout-Flags aus.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
