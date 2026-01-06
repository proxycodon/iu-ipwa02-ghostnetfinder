package de.ghostnet.domain.repo;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {

    // Global views
    List<GhostNet> findAllByOrderByCreatedAtDesc();

    List<GhostNet> findAllByStatusIn(List<Status> statuses);

    // Salvager-related views
    List<GhostNet> findAllByAssignedSalvagerUsername(String username);

    List<GhostNet> findAllByAssignedSalvagerUsernameAndStatusIn(
            String username,
            List<Status> statuses
    );
}
