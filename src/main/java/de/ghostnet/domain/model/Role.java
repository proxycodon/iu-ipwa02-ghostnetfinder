package de.ghostnet.domain.model;

/**
 * Rollenmodell für Benutzerkonten.
 * Steuert, welche Aktionen in der Anwendung erlaubt sind.
 */
public enum Role {

    /** Darf Netze melden, aber keine Bergeinsätze verwalten. */
    ROLE_REPORTER,

    /** Darf Netze übernehmen (claimen) und als geborgen melden. */
    ROLE_SALVAGER,

    /** Administrativer Zugriff, spätere Erweiterungen möglich. */
    ROLE_ADMIN
}
