package de.ghostnet.domain.model;

/**
 * Lebenszyklus eines Geisternetzes im System.
 */
public enum Status {

    /** Netz wurde gemeldet, aber noch keinem Bergungsteam zugeordnet. */
    REPORTED,

    /** Netz wurde von einem Salvager übernommen, Bergung ist geplant oder läuft. */
    PENDING,

    /** Netz wurde erfolgreich geborgen. */
    SALVAGED,

    /** Netz gilt als verschollen bzw. vor Ort nicht mehr auffindbar. */
    LOST
}
