package de.ghostnet.web;

import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the Homepage ("Ghost Net Finder").
 * Shows all currently uncollected nets on the map and in a list.
 */
@Controller
public class HomeController {

    private final GhostNetRepository repo;

    public HomeController(GhostNetRepository repo) {
        this.repo = repo;
    }

    /**
     * Homepage: loads all nets with status REPORTED and PENDING
     * as a basis for the map view and table.
     */
    @GetMapping("/")
    String home(Model model) {
        model.addAttribute(
            "openNets",
            repo.findAllByStatusIn(List.of(Status.REPORTED, Status.PENDING))
        );
        return "index";
    }
}
