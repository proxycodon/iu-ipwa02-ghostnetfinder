package de.ghostnet.domain.model;

/**
 * Role model for user accounts.
 * Controls which actions are allowed in the application.
 */
public enum Role {

    /** May report nets, but not manage salvage operations. */
    ROLE_REPORTER,

    /** May claim nets and mark them as salvaged. */
    ROLE_SALVAGER,

    /** Admin access, later extensions possible. */
    ROLE_ADMIN
}
