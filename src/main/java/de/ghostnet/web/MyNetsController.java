package de.ghostnet.web;

import de.ghostnet.service.GhostNetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller für die persönliche Übersicht eines Salvagers.
 * Zeigt alle Netze, die dem aktuell eingeloggten Benutzer zugeordnet sind.
 */
@Controller
public class MyNetsController {

    private final GhostNetService service;

    public MyNetsController(GhostNetService service) {
        this.service = service;
    }

    /**
     * Liste aller Netze, die vom aktuellen Benutzer übernommen wurden.
     */
    @GetMapping("/my-nets")
    public String myNets(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("assignedNets", service.findAssignedTo(username));
        return "nets/my-nets";
    }
}
