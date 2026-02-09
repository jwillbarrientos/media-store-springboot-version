-- =====================
-- CLIENTS
-- =====================
INSERT INTO CLIENT (ID, CREATION_TIMESTAMP, EMAIL, PASSWORD) VALUES
(1, CURRENT_TIMESTAMP, 'alice@example.com', 'pass123'),
(2, CURRENT_TIMESTAMP, 'bob@example.com', 'secret456');

-- =====================
-- TAGS
-- =====================
INSERT INTO TAG (ID, CREATION_TIMESTAMP, NAME, CLIENT_ID) VALUES
(1, CURRENT_TIMESTAMP, 'Fitness', 1),
(2, CURRENT_TIMESTAMP, 'Cooking', 1),
(3, CURRENT_TIMESTAMP, 'Gaming', 2);

-- =====================
-- VIDEOS
-- =====================
INSERT INTO VIDEO (ID, CREATION_TIMESTAMP, DURATION_SECONDS, FILE_SIZE, LINK, NAME, PATH, STATE, CLIENT_ID) VALUES
(1, CURRENT_TIMESTAMP, 300, 5000000, 'https://example.com/video1', 'Workout Tips', '/videos/workout.mp4', 'ACTIVE', 1),
(2, CURRENT_TIMESTAMP, 600, 12000000, 'https://example.com/video2', 'Cooking Pasta', '/videos/pasta.mp4', 'ACTIVE', 1),
(3, CURRENT_TIMESTAMP, 450, 8000000, 'https://example.com/video3', 'Gameplay Highlights', '/videos/gameplay.mp4', 'ACTIVE', 2);

-- =====================
-- CLIENT_TAGS (relaciona clientes y tags)
-- =====================
INSERT INTO CLIENT_TAGS (CLIENT_ID, TAGS_ID) VALUES
(1, 1),
(1, 2),
(2, 3);

-- =====================
-- CLIENT_VIDEOS (relaciona clientes y videos)
-- =====================
INSERT INTO CLIENT_VIDEOS (CLIENT_ID, VIDEOS_ID) VALUES
(1, 1),
(1, 2),
(2, 3);

-- =====================
-- VIDEO_TAGS (relaciona videos y tags)
-- =====================
INSERT INTO VIDEO_TAGS (VIDEOS_ID, TAGS_ID) VALUES
(1, 1),
(2, 2),
(3, 3);
