package de.ghostnet.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * JPA-Entität für ein gemeldetes Geisternetz.
 * Hält Positionsdaten, Status und einfache Metadaten zur Nachverfolgung.
 */
@Entity
@Table(name = "ghost_net")
public class GhostNet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Kurzer Titel zur Identifikation des Netzes. */
    @NotBlank
    @Column(nullable = false, length = 140)
    private String title;

    /** Freitext-Beschreibung, z.B. Lagebeschreibung oder Besonderheiten. */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Geographische Breite des Fundortes. */
    @NotNull
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    /** Geographische Länge des Fundortes. */
    @NotNull
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    /** (Optionale) grobe Flächenangabe des Netzes in Quadratmetern. */
    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedSizeM2;

    /** Aktueller Bearbeitungsstatus des Netzes (gemeldet, übernommen, geborgen, verloren). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.REPORTED;

    /** Benutzername der Person/Organisation, die das Netz übernommen hat (optional). */
    @Column(length = 100)
    private String assignedSalvagerUsername;

    /** Benutzername der meldenden Person oder der String "anonymous" für anonyme Meldungen. */
    @Column(length = 100)
    private String reportedBy;

    /** Optimistische Sperre, um parallele Updates aufzulösen. */
    @Version
    private long version;

    /** Zeitstempel der Erstellung (UTC). */
    @Column(nullable = false)
    private Instant createdAt;

    /** Zeitstempel der letzten Änderung (UTC). */
    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * Wird von JPA vor dem ersten Persistieren gesetzt.
     * Initialisiert createdAt und updatedAt.
     */
    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * Wird von JPA vor jedem Update aufgerufen.
     * Aktualisiert updatedAt auf den aktuellen Zeitpunkt.
     */
    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    // --- Getter & Setter ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setVersion(long version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
