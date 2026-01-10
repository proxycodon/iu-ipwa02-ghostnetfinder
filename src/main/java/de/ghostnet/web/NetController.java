package de.ghostnet.web;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.service.GhostNetService;
import de.ghostnet.web.dto.GhostNetViewDto;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

// Web controller for ghost net CRUD/actions
@Controller
@RequestMapping("/nets")
public class NetController {

    private final GhostNetService service;

    public NetController(GhostNetService service) {
        this.service = service;
    }

    // List all nets (any status), newest first
    @GetMapping
    public String list(Model model) {
        model.addAttribute("nets",
                service.findAll().stream()
                        .map(GhostNetViewDto::new)
                        .collect(Collectors.toList())
        );
        return "nets/list";
    }

    // Show the "report new net" form
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("net", new GhostNet());
        return "nets/form";
    }

    // Create a new net (anonymous allowed)
    @PostMapping
    public String create(@Valid @ModelAttribute("net") GhostNet net,
                         BindingResult br,
                         Authentication auth) {
        if (br.hasErrors()) {
            return "nets/form";
        }

        // If logged in, link report to the current account; otherwise store as "anonymous"
        String reporterUsername = (auth != null && auth.isAuthenticated()) ? auth.getName() : null;

        service.create(net, reporterUsername);
        return "redirect:/";
    }

    // Claim a net (REPORTED -> PENDING) for the current user
    @PostMapping("/claim/{id}")
    @PreAuthorize("isAuthenticated()")
    public String claim(@PathVariable Long id, Authentication auth) {
        try {
            service.claim(id, auth.getName());
            return "redirect:/my-nets";
        } catch (IllegalStateException e) {
            // Defensive: handle direct POSTs / race conditions
            return "redirect:/?error=invalidTransition";
        }
    }

    // Mark a net as salvaged (PENDING -> SALVAGED) by the assigned salvager only
    @PostMapping("/mark-retrieved/{id}")
    @PreAuthorize("isAuthenticated()")
    public String retrieved(@PathVariable Long id, Authentication auth) {
        try {
            service.markRetrieved(id, auth.getName());
            return "redirect:/my-nets";
        } catch (IllegalStateException e) {
            // Defensive: invalid status transition
            return "redirect:/my-nets?error=invalidTransition";
        } catch (AccessDeniedException e) {
            // Defensive: user is authenticated but not allowed to complete this net
            return "redirect:/my-nets?error=forbidden";
        }
    }

    // Mark a net as lost (REPORTED/PENDING -> LOST)
    @PostMapping("/mark-lost/{id}")
    @PreAuthorize("isAuthenticated()")
    public String lost(@PathVariable Long id) {
        try {
            service.markLost(id);
            return "redirect:/nets";
        } catch (IllegalStateException e) {
            // Defensive: invalid status transition
            return "redirect:/nets?error=invalidTransition";
        }
    }
}
