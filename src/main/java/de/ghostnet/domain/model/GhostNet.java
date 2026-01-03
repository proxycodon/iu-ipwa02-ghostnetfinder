package de.ghostnet.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * JPA-Entität für ein gemeldetes Geisternetz.
 * Hält Positionsdaten, Status und Metadaten zur Nachverfolgung.
 */
@Entity
@Table(name = "ghost_net")
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Geographische Breite des Fundortes. */
    @NotNull
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    /** Geographische Länge des Fundortes. */
    @NotNull
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    /** (Optionale) grobe Flächenangabe des Netzes in Quadratmetern. */
    @Column(name = "estimated_size_m2", precision = 10, scale = 2)
    private BigDecimal estimatedSizeM2;

    /** Aktueller Bearbeitungsstatus des Netzes (REPORTED, PENDING, SALVAGED, LOST). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.REPORTED;

    /** Benutzername der Person/Organisation, die das Netz übernommen hat (optional). */
    @Column(name = "assigned_salvager_username", length = 100)
    private String assignedSalvagerUsername;

    /** Benutzername der meldenden Person oder "anonymous" für anonyme Meldungen. */
    @Column(name = "reported_by_username", nullable = false, length = 100)
    private String reportedBy = "anonymous";

    /** Optimistische Sperre, um parallele Updates aufzulösen. */
    @Version
    private long version;

    /** Zeitstempel der Erstellung (UTC). */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /** Zeitstempel der letzten Änderung (UTC). */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;

        if (reportedBy == null || reportedBy.isBlank()) {
            reportedBy = "anonymous";
        }
        if (status == null) {
            status = Status.REPORTED;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();

        if (reportedBy == null || reportedBy.isBlank()) {
            reportedBy = "anonymous";
        }
        if (status == null) {
            status = Status.REPORTED;
        }
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getEstimatedSizeM2() {
        return estimatedSizeM2;
    }

    public void setEstimatedSizeM2(BigDecimal estimatedSizeM2) {
        this.estimatedSizeM2 = estimatedSizeM2;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssignedSalvagerUsername() {
        return assignedSalvagerUsername;
    }

    public void setAssignedSalvagerUsername(String assignedSalvagerUsername) {
        this.assignedSalvagerUsername = assignedSalvagerUsername;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
