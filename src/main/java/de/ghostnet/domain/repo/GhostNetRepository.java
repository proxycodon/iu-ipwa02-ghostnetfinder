package de.ghostnet.domain.repo;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository für Geisternetze.
 * Stellt einfache Lesezugriffe nach Status, Zeit und verantwortlichem Salvager bereit.
 */
public interface GhostNetRepository extends JpaRepository<GhostNet, Long> {

    /**
     * Liefert alle Netze mit einem bestimmten Status.
     */
    List<GhostNet> findAllByStatus(Status status);

    /**
     * Liefert alle Netze, die einem bestimmten Salvager zugeordnet sind.
     */
    List<GhostNet> findAllByAssignedSalvagerUsername(String username);

    /**
     * Liefert alle Netze, absteigend nach Erstellungszeit sortiert (neueste zuerst).
     */
    List<GhostNet> findAllByOrderByCreatedAtDesc();
}
