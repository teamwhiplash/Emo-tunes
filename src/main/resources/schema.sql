
-- ======================
-- USERS TABLE
-- ======================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    spotify_id VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    refresh_token VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======================
-- SONGS TABLE
-- ======================
CREATE TABLE IF NOT EXISTS songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    release_date DATE,
    artist_name VARCHAR(150),
    spotify_id VARCHAR(100) UNIQUE,
    cover_url VARCHAR(500),
    duration_seconds INT,
    song_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ======================
-- PLAYLISTS TABLE
-- ======================
CREATE TABLE IF NOT EXISTS playlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    cover_url VARCHAR(500),
    emotion VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_playlist_user FOREIGN KEY (user_id) REFERENCES emotunes.users(id) ON DELETE CASCADE
);

-- ======================
-- PLAYLIST_SONGS TABLE (MANY-TO-MANY)
-- ======================
CREATE TABLE IF NOT EXISTS playlist_songs (
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (playlist_id, song_id),

    CONSTRAINT fk_ps_playlist FOREIGN KEY (playlist_id) REFERENCES emotunes.playlists(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_song FOREIGN KEY (song_id) REFERENCES emotunes.songs(id) ON DELETE CASCADE
);
