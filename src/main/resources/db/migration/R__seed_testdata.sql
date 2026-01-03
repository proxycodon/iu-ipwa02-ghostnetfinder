-- Ghost nets seed data
-- IDs are generated automatically (IDENTITY starting at 1001).
-- MERGE is used to keep the seed idempotent.
-- The combination (latitude, longitude) is used as a logical key
-- for test data only.

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
  'admin',
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
  'salv',
  'rep',
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
  'salv',
  'admin',
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
  'admin',
  TIMESTAMP '2025-12-30 07:22:39',
  TIMESTAMP '2025-12-30 07:22:39'
);
