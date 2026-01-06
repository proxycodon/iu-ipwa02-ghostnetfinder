package de.ghostnet.web;

import de.ghostnet.service.GhostNetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the personal overview of a salvager.
 * Shows all nets assigned to the currently logged-in user.
 */
@Controller
public class MyNetsController {

    private final GhostNetService service;

    public MyNetsController(GhostNetService service) {
        this.service = service;
    }

    /**
     * Lists all nets assigned to the currently logged-in user.
     */
    @GetMapping("/my-nets")
    public String myNets(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("assignedNets", service.findActiveAssignedTo(username));
        return "nets/my-nets";
    }
}
