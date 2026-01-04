package de.ghostnet.domain.repo;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {

    List<GhostNet> findAllByAssignedSalvagerUsername(String username);

    List<GhostNet> findAllByOrderByCreatedAtDesc();

    // For Homepage: all nets with certain status
    List<GhostNet> findAllByStatusIn(List<Status> statuses);

    // Optional better: deliver sorted
    List<GhostNet> findAllByStatusOrderByCreatedAtDesc(Status status);
}
