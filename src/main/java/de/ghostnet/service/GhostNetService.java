package de.ghostnet.service;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service-Schicht für Geschäftslogik rund um Geisternetze.
 * Kapselt Statuswechsel und Zugriffe auf das GhostNetRepository.
 */
@Service
@Transactional
public class GhostNetService {

    private final GhostNetRepository repo;
    private final UserService userService;

    public GhostNetService(GhostNetRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    /**
     * Legt ein neues Geisternetz an.
     * Falls kein Benutzer übergeben wird, wird der Report als "anonymous" gespeichert.
     */
    public GhostNet create(GhostNet net, String reporterUsername) {
        net.setStatus(Status.REPORTED);
        net.setAssignedSalvagerUsername(null);
        net.setReportedBy(reporterUsername != null ? reporterUsername : "anonymous");
        return repo.save(net);
    }

    /**
     * Markiert ein Netz als übernommen (PENDING) und trägt den zuständigen Salvager ein.
     * Beim ersten Claim wird ein reiner Reporter automatisch zur Rolle SALVAGER hochgestuft.
     */
    public void claim(Long id, String username) {
        GhostNet net = repo.findById(id).orElseThrow();

        if (net.getAssignedSalvagerUsername() != null) {
            throw new IllegalStateException("Net already claimed");
        }

        // Selbstservice: Rolle bei erstem Claim von REPORTER → SALVAGER anheben
        userService.promoteToSalvagerIfReporter(username);

        net.setAssignedSalvagerUsername(username);
        net.setStatus(Status.PENDING);
    }

    /**
     * Markiert ein Netz als geborgen.
     * Nur der zugeordnete Salvager darf den Status auf SALVAGED setzen.
     */
    public void markRetrieved(Long id, String username) {
        GhostNet net = repo.findById(id).orElseThrow();
        if (!username.equals(net.getAssignedSalvagerUsername())) {
            throw new AccessDeniedException("Not your assigned net.");
        }
        net.setStatus(Status.SALVAGED);
    }

    /**
     * Markiert ein Netz als verschollen, z.B. wenn es vor Ort nicht mehr auffindbar ist.
     */
    public void markLost(Long id) {
        GhostNet net = repo.findById(id).orElseThrow();
        net.setStatus(Status.LOST);
    }

    /**
     * Liefert alle Netze, die einem bestimmten Salvager zugeordnet sind.
     * Wird z.B. für die persönliche "My nets"-Ansicht verwendet.
     */
    public List<GhostNet> findAssignedTo(String username) {
        return repo.findAllByAssignedSalvagerUsername(username);
    }

    /**
     * Liefert alle Netze, absteigend nach Erstellungszeit sortiert.
     * Grundlage für die globale Gesamtansicht.
     */
    public List<GhostNet> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }
}
