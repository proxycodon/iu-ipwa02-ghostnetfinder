package de.ghostnet.domain.repo;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {

    List<GhostNet> findAllByAssignedSalvagerUsername(String username);

    List<GhostNet> findAllByOrderByCreatedAtDesc();

    // Für Startseite: alle Netze mit bestimmtem Status
    List<GhostNet> findAllByStatus(Status status);

    // Optional besser: sortiert ausliefern
    List<GhostNet> findAllByStatusOrderByCreatedAtDesc(Status status);
}
