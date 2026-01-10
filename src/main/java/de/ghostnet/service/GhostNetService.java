package de.ghostnet.service;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import de.ghostnet.domain.repo.GhostNetRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service layer for GhostNet business logic
@Service
@Transactional
public class GhostNetService {

    private final GhostNetRepository repo;
    private final UserService userService;

    public GhostNetService(GhostNetRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    // Creates a new ghost net entry. Always starts as REPORTED and unassigned
    public GhostNet create(GhostNet net, String reporterUsername) {
        net.setStatus(Status.REPORTED);
        net.setAssignedSalvagerUsername(null);
        net.setReportedBy((reporterUsername != null && !reporterUsername.isBlank()) ? reporterUsername : "anonymous");
        return repo.save(net);
    }

    // Claims a net: REPORTED -> PENDING
    public void claim(Long id, String username) {
        GhostNet net = load(id);

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

    // Marks a net as salvaged: PENDING -> SALVAGED
    public void markRetrieved(Long id, String username) {
        GhostNet net = load(id);

        if (net.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only PENDING nets can be marked as salvaged");
        }
        if (net.getAssignedSalvagerUsername() == null || !username.equals(net.getAssignedSalvagerUsername())) {
            throw new AccessDeniedException("Only the assigned salvager can mark this net as salvaged");
        }

        net.setStatus(Status.SALVAGED);
    }

    // Marks a net as lost: REPORTED|PENDING -> LOST
    public void markLost(Long id) {
        GhostNet net = load(id);

        if (net.getStatus() != Status.REPORTED && net.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only REPORTED or PENDING nets can be marked as lost");
        }

        net.setStatus(Status.LOST);
    }

    // Returns all nets assigned to a given salvager (all statuses)
    @Transactional(readOnly = true)
    public List<GhostNet> findAssignedTo(String username) {
        return repo.findAllByAssignedSalvagerUsername(username);
    }

    // Returns only active assigned (PENDING) nets for a salvager
    @Transactional(readOnly = true)
    public List<GhostNet> findActiveAssignedTo(String username) {
        return repo.findAllByAssignedSalvagerUsernameAndStatusIn(
                username,
                List.of(Status.PENDING)
        );
    }

    // Returns all nets sorted by creation date (descending)
    @Transactional(readOnly = true)
    public List<GhostNet> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    private GhostNet load(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
