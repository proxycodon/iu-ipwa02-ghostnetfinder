package de.ghostnet.web;

import de.ghostnet.service.GhostNetService;
import de.ghostnet.web.dto.GhostNetViewDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;


// Controller for the public overview of all nets (any status)
@Controller
public class AllNetsController {

    private final GhostNetService service;

    public AllNetsController(GhostNetService service) {
        this.service = service;
    }

    // Shows all known nets, sorted by creation date (newest first)
    @GetMapping("/nets/all")
    public String allNets(Model model) {
        model.addAttribute("nets",
                service.findAll().stream()
                        .map(GhostNetViewDto::new)
                        .collect(Collectors.toList())
        );
        return "nets/all";
    }
}
