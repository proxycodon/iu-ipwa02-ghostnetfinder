package de.ghostnet.web;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nets")
public class NetController {

    private final GhostNetService service;

    public NetController(GhostNetService service) {
        this.service = service;
    }

    /**
     * List ALL nets (any status), newest first.
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("nets", service.findAll());
        return "nets/list";
    }

    /**
     * World map with all ghost nets as markers.
     */
    @GetMapping("/map")
    public String map(Model model) {
        model.addAttribute("nets", service.findAll());
        return "nets/map"; // -> templates/nets/map.html
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("net", new GhostNet());
        return "nets/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("net") GhostNet net,
                         BindingResult br,
                         Authentication auth) {
        if (br.hasErrors()) {
            return "nets/form";
        }

        String reporterUsername = null;
        if (auth != null && auth.isAuthenticated()) {
            reporterUsername = auth.getName();
        }

        service.create(net, reporterUsername);
        return "redirect:/";
    }

    @PostMapping("/claim/{id}")
    @PreAuthorize("isAuthenticated()")
    public String claim(@PathVariable Long id, Authentication auth) {
        service.claim(id, auth.getName());
        return "redirect:/my-nets";
    }

    @PostMapping("/mark-retrieved/{id}")
    @PreAuthorize("isAuthenticated()")
    public String retrieved(@PathVariable Long id, Authentication auth) {
        service.markRetrieved(id, auth.getName());
        return "redirect:/my-nets";
    }

    @PostMapping("/mark-lost/{id}")
    @PreAuthorize("isAuthenticated()")
    public String lost(@PathVariable Long id) {
        service.markLost(id);
        return "redirect:/nets";
    }
}
