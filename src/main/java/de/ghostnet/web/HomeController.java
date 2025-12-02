package de.ghostnet.web;

import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller für die Startseite ("Ghost Net Finder").
 * Zeigt aktuell alle offenen Netze auf Karte und in einer Liste.
 */
@Controller
public class HomeController {

    private final GhostNetRepository repo;

    public HomeController(GhostNetRepository repo) {
        this.repo = repo;
    }

    /**
     * Startseite: lädt alle Netze mit Status REPORTED
     * als Basis für Map-Ansicht und Tabelle.
     */
    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("openNets", repo.findAllByStatus(Status.REPORTED));
        return "index";
    }
}
