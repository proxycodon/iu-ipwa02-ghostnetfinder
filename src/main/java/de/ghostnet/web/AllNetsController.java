package de.ghostnet.web;

import de.ghostnet.service.GhostNetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller für die öffentliche Gesamtansicht aller Netze
 * (unabhängig vom aktuellen Status).
 */
@Controller
public class AllNetsController {

    private final GhostNetService service;

    public AllNetsController(GhostNetService service) {
        this.service = service;
    }

    /**
     * Zeigt alle bekannten Netze, sortiert nach Erstellungsdatum.
     */
    @GetMapping("/nets/all")
    public String allNets(Model model) {
        model.addAttribute("nets", service.findAll());
        return "nets/all";
    }
}
