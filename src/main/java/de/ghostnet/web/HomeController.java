package de.ghostnet.web;

import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import de.ghostnet.web.dto.GhostNetViewDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// Homepage controller (map + table for "open" nets)
@Controller
public class HomeController {

    private final GhostNetRepository repo;

    public HomeController(GhostNetRepository repo) {
        this.repo = repo;
    }

    // Homepage: show only "open" nets (REPORTED + PENDING) on map and in table
    @GetMapping("/")
    public String home(Model model) {
        var openNets = repo.findAllByStatusIn(List.of(Status.REPORTED, Status.PENDING));

        // Convert entities to view DTOs so Thymeleaf can use precomputed labels/flags
        var openNetDtos = openNets.stream()
                .map(GhostNetViewDto::new)
                .toList();

        model.addAttribute("openNets", openNetDtos);
        return "index";
    }
}
