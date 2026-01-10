package de.ghostnet.web.dto;

import de.ghostnet.domain.model.GhostNet;
import de.ghostnet.domain.model.Status;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

// View DTO for rendering ghost nets in Thymeleaf templates
public class GhostNetViewDto {

    private final Long id;

    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final BigDecimal estimatedSizeM2;

    private final Status status;
    private final String reportedBy;
    private final String assignedSalvagerUsername;

    private final LocalDateTime createdAt;

    // Labels / flags for consistent UI rendering
    private final String salvagerLabel;
    private final String coordinatesLabel;
    private final String estimatedSizeLabel;
    private final boolean hasSalvager;
    private final boolean claimable;
    private final boolean markLostAllowed;

    public GhostNetViewDto(GhostNet net) {
        this.id = net.getId();

        this.latitude = net.getLatitude();
        this.longitude = net.getLongitude();
        this.estimatedSizeM2 = net.getEstimatedSizeM2();

        this.status = net.getStatus();
        this.reportedBy = net.getReportedBy();
        this.assignedSalvagerUsername = net.getAssignedSalvagerUsername();

        Instant createdAtInstant = net.getCreatedAt();
        this.createdAt = (createdAtInstant != null)
                ? LocalDateTime.ofInstant(createdAtInstant, ZoneId.systemDefault())
                : null;

        this.hasSalvager = (this.assignedSalvagerUsername != null && !this.assignedSalvagerUsername.isBlank());

        this.salvagerLabel = this.hasSalvager ? this.assignedSalvagerUsername : "Unassigned";

        // Keep formatting consistent across index/list/all
        String lat = (this.latitude != null) ? this.latitude.toPlainString() : "-";
        String lon = (this.longitude != null) ? this.longitude.toPlainString() : "-";
        this.coordinatesLabel = lat + ", " + lon;

        this.estimatedSizeLabel = (this.estimatedSizeM2 != null)
                ? this.estimatedSizeM2.toPlainString()
                : "-";

        // UI actions: keep aligned with service transition rules
        this.claimable = (this.status == Status.REPORTED) && !this.hasSalvager;
        this.markLostAllowed = (this.status == Status.REPORTED || this.status == Status.PENDING);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getEstimatedSizeM2() {
        return estimatedSizeM2;
    }

    public Status getStatus() {
        return status;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getAssignedSalvagerUsername() {
        return assignedSalvagerUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSalvagerLabel() {
        return salvagerLabel;
    }

    public String getCoordinatesLabel() {
        return coordinatesLabel;
    }

    public String getEstimatedSizeLabel() {
        return estimatedSizeLabel;
    }

    public boolean isHasSalvager() {
        return hasSalvager;
    }

    public boolean isClaimable() {
        return claimable;
    }

    public boolean isMarkLostAllowed() {
        return markLostAllowed;
    }
}
