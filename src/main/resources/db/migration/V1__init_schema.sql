-- Initiales Schema für Benutzerkonten und Geisternetze.

CREATE TABLE app_user (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  -- Rollenwert, z.B. ROLE_REPORTER / ROLE_SALVAGER / ROLE_ADMIN
  role VARCHAR(30) NOT NULL,
  -- Soft-Flag zur (De-)Aktivierung von Accounts
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE ghost_net (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(140) NOT NULL,
  description TEXT,
  -- Geokoordinaten des Netzes
  latitude NUMERIC(9,6) NOT NULL,
  longitude NUMERIC(9,6) NOT NULL,
  -- Optionale Flächenschätzung in Quadratmetern
  estimated_size_m2 NUMERIC(10,2),
  -- Status im Lebenszyklus des Netzes:
  -- REPORTED, PENDING, SALVAGED, LOST
  status VARCHAR(20) NOT NULL,
  -- Benutzername des zugeordneten Salvagers (optional)
  assigned_salvager_username VARCHAR(100),
  -- Zeitstempel für einfache Nachvollziehbarkeit von Änderungen
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now(),
  -- Optimistische Sperre für konkurrierende Updates
  version BIGINT NOT NULL DEFAULT 0
);

-- Index zur schnellen Filterung nach Status (z.B. für offene Netze)
CREATE INDEX idx_ghost_net_status ON ghost_net(status);
