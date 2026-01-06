package de.ghostnet.service;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for GhostNet business logic.
 * Owns status transitions and repository access.
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
     * Creates a new ghost net report.
     * If no reporter username is provided, the report is stored as "anonymous".
     */
    public GhostNet create(GhostNet net, String reporterUsername) {
        net.setStatus(Status.REPORTED);
        net.setAssignedSalvagerUsername(null);
        net.setReportedBy(reporterUsername != null ? reporterUsername : "anonymous");
        return repo.save(net);
    }

    /**
     * Claims a net (sets status to PENDING and assigns the salvager).
     * On first claim, a REPORTER is auto-promoted to SALVAGER (self-service).
     */
    public void claim(Long id, String username) {
        GhostNet net = repo.findById(id).orElseThrow();

        // Only REPORTED nets can be claimed, and only if still unassigned
        if (net.getStatus() != Status.REPORTED) {
            throw new IllegalStateException("Only REPORTED nets can be claimed");
        }
        if (net.getAssignedSalvagerUsername() != null) {
            throw new IllegalStateException("Net already claimed");
        }

        userService.promoteToSalvagerIfReporter(username);

        net.setAssignedSalvagerUsername(username);
        net.setStatus(Status.PENDING);
    }

    /**
     * Marks a net as salvaged (SALVAGED).
     * Only the assigned salvager may complete the retrieval.
     */
    public void markRetrieved(Long id, String username) {
        GhostNet net = repo.findById(id).orElseThrow();

        if (!username.equals(net.getAssignedSalvagerUsername())) {
            throw new AccessDeniedException("Not your assigned net.");
        }

        // Only a currently assigned (PENDING) net can be marked as salvaged
        if (net.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only PENDING nets can be marked as salvaged");
        }

        net.setStatus(Status.SALVAGED);
    }

    //Marks a net as lost (LOST) if it cannot be found on site.
    public void markLost(Long id) {
        GhostNet net = repo.findById(id).orElseThrow();

        // Only "open" nets may become LOST
        if (net.getStatus() != Status.REPORTED && net.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only REPORTED or PENDING nets can be marked as lost");
        }

        net.setStatus(Status.LOST);
        // Intentionally keep assignedSalvagerUsername for audit purposes.
    }

    //Returns all nets assigned to a given salvager (any status).
    public List<GhostNet> findAssignedTo(String username) {
        return repo.findAllByAssignedSalvagerUsername(username);
    }

    
    // Returns only "active" assigned nets for a salvager.
    public List<GhostNet> findActiveAssignedTo(String username) {
        return repo.findAllByAssignedSalvagerUsernameAndStatusIn(
                username,
                List.of(Status.PENDING)
        );
    }

    /**
     * Returns all nets sorted by creation date (descending).
     */
    public List<GhostNet> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }
}
