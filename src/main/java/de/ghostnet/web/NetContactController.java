package de.ghostnet.web;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.repo.GhostNetRepository;
import de.ghostnet.domain.repo.UserRepository;
import de.ghostnet.web.dto.ContactDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/nets")
public class NetContactController {

    private final GhostNetRepository nets;
    private final UserRepository users;

    public NetContactController(GhostNetRepository nets, UserRepository users) {
        this.nets = nets;
        this.users = users;
    }

    /**
     * Liefert Kontaktdaten (username + phoneNumber) für ein Netz.
     * Priorität: assigned salvager (falls gesetzt), sonst reporter.
     * Nur für eingeloggte User.
     */
    @GetMapping("/{id}/contact")
    @PreAuthorize("isAuthenticated()")
    public ContactDto getContact(@PathVariable Long id) {

        GhostNet net = nets.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Net not found"));

        String contactUsername =
                (net.getAssignedSalvagerUsername() != null && !net.getAssignedSalvagerUsername().isBlank())
                        ? net.getAssignedSalvagerUsername()
                        : net.getReportedBy();

        // Optional: falls "anonymous" → keine Kontaktdaten verfügbar
        if (contactUsername == null || contactUsername.isBlank() || "anonymous".equalsIgnoreCase(contactUsername)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No contact available for anonymous report");
        }

        return users.findByUsername(contactUsername)
                .map(u -> new ContactDto(u.getUsername(), u.getPhoneNumber()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
