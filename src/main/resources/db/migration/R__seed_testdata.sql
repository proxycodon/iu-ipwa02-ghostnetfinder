-- ------------------------------------------------------------
-- USERS (reference users, NOT for login)
-- ------------------------------------------------------------
MERGE INTO app_user (username, password, phone_number, role, enabled) KEY(username)
VALUES
  ('tina.tester', 'DISABLED', '+49 151 99990001', 'ROLE_REPORTER', FALSE),
  ('tom.test',    'DISABLED', '+49 151 99990002', 'ROLE_SALVAGER', FALSE),
  ('edgar.j.ava', 'DISABLED', '+49 151 99990003', 'ROLE_ADMIN',    FALSE);


-- ------------------------------------------------------------
-- Ghost net seed data
-- ------------------------------------------------------------

MERGE INTO ghost_net (
  latitude,
  longitude,
  estimated_size_m2,
  status,
  assigned_salvager_username,
  reported_by_username,
  created_at,
  updated_at
) KEY(latitude, longitude)
VALUES (
  53.821776,
  6.962561,
  25.0,
  'REPORTED',
  NULL,
  'tina.tester',
  TIMESTAMP '2025-12-10 09:15:00',
  TIMESTAMP '2025-12-10 09:15:00'
);

MERGE INTO ghost_net (
  latitude,
  longitude,
  estimated_size_m2,
  status,
  assigned_salvager_username,
  reported_by_username,
  created_at,
  updated_at
) KEY(latitude, longitude)
VALUES (
  44.024148,
  -2.650701,
  40.0,
  'PENDING',
  'edgar.j.ava',
  'tina.tester',
  TIMESTAMP '2025-12-12 14:40:00',
  TIMESTAMP '2025-12-12 15:05:00'
);

MERGE INTO ghost_net (
  latitude,
  longitude,
  estimated_size_m2,
  status,
  assigned_salvager_username,
  reported_by_username,
  created_at,
  updated_at
) KEY(latitude, longitude)
VALUES (
  40.304382,
  14.439096,
  15.0,
  'SALVAGED',
  'tom.test',
  'tom.test',
  TIMESTAMP '2025-12-05 08:10:00',
  TIMESTAMP '2025-12-06 11:30:00'
);

MERGE INTO ghost_net (
  latitude,
  longitude,
  estimated_size_m2,
  status,
  assigned_salvager_username,
  reported_by_username,
  created_at,
  updated_at
) KEY(latitude, longitude)
VALUES (
  35.775048,
  14.990874,
  25.0,
  'REPORTED',
  NULL,
  'edgar.j.ava',
  TIMESTAMP '2025-12-30 07:22:39',
  TIMESTAMP '2025-12-30 07:22:39'
);
