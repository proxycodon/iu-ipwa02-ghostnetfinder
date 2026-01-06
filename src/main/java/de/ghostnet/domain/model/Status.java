package de.ghostnet.domain.model;

/**
 * Lifecycle of a ghost net in the system.
 */
public enum Status {

    /** Net was reported but not yet assigned to a salvage team. */
    REPORTED,

    /** Net was claimed by a salvager, salvage is planned or in progress. */
    PENDING,

    /** Net was successfully salvaged. */
    SALVAGED,

    /** Net is considered lost or no longer findable on site. */
    LOST
}
