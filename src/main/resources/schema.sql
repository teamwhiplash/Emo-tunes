
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
    duration_milliseconds INT,
    song_url VARCHAR(500),
    emotion VARCHAR(10) DEFAULT 'neutral'
            CHECK (emotion IN ('happy', 'love', 'sad', 'uplift', 'rage')),
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

INSERT IGNORE INTO songs (title, release_date, artist_name, spotify_id, cover_url, duration_milliseconds, song_url, emotion)
VALUES
('Ghungroo (From "War")', '2019-09-30', 'Vishal-Shekhar', '0WdbnNKO0Jt4BZACSDQh44', 'https://i.scdn.co/image/ab67616d0000b273281650a8e8c5d04658d31ac1', 302935, 'https://open.spotify.com/track/0WdbnNKO0Jt4BZACSDQh44', 'happy'),
('Subha Hone Na De', '2011-10-21', 'Pritam', '6WediUhXDfm8FR3wlM0r3G', 'https://i.scdn.co/image/ab67616d0000b273daa89593cc2cde9651665d03', 288696, 'https://open.spotify.com/track/6WediUhXDfm8FR3wlM0r3G', 'happy'),
('Tum Hi Ho Bandhu (From "Cocktail")', '2012-06-12', 'Neeraj Shridhar', '4pE05HNzmVFGpCOfNKE3w6', 'https://i.scdn.co/image/ab67616d0000b273f5587675f199fe0997c0525b', 282012, 'https://open.spotify.com/track/4pE05HNzmVFGpCOfNKE3w6', 'happy'),
('It''s the Time to Disco', '2003-09-20', 'Shankar-Ehsaan-Loy', '3hCUkos0NxuSFl73oOHJzb', 'https://i.scdn.co/image/ab67616d0000b273a2055e0b847ff66fb5206099', 333093, 'https://open.spotify.com/track/3hCUkos0NxuSFl73oOHJzb', 'happy'),
('Saturday Saturday', '2014-06-19', 'Shaarib Toshi', '7MvQQGqYE4r0ucTarmI2Gr', 'https://i.scdn.co/image/ab67616d0000b273cf1f4aba2ac8c829cee87bcc', 210799, 'https://open.spotify.com/track/7MvQQGqYE4r0ucTarmI2Gr', 'happy'),
('Balam Pichkari', '2013-03-30', 'Pritam', '18e3XXYCv4Tx8uUl1mP3CN', 'https://i.scdn.co/image/ab67616d0000b273707ea5b8023ac77d31756ed4', 288902, 'https://open.spotify.com/track/18e3XXYCv4Tx8uUl1mP3CN', 'happy'),
('Nashe Si Chadh Gayi', '2016-12-01', 'Vishal-Shekhar', '0biCSADTAblvLTLtJz4pXO', 'https://i.scdn.co/image/ab67616d0000b273f5dc36d5000145375a41c3b8', 237875, 'https://open.spotify.com/track/0biCSADTAblvLTLtJz4pXO', 'happy'),
('The Breakup Song', '2016-10-26', 'Pritam', '5FihcmME7NlwW8KbYOaHVH', 'https://i.scdn.co/image/ab67616d0000b273bca30634e6cad10b97c03d21', 252631, 'https://open.spotify.com/track/5FihcmME7NlwW8KbYOaHVH', 'happy'),
('Bom Diggy Diggy', '2018-02-08', 'Zack Knight', '6qCNaRRr5xJALfNJvh0NAw', 'https://i.scdn.co/image/ab67616d0000b27382f0b09ca518a1563175ed85', 238530, 'https://open.spotify.com/track/6qCNaRRr5xJALfNJvh0NAw', 'happy'),
('Jai Jai Shivshankar (From "War")', '2019-09-21', 'Vishal Dadlani', '6lfSsCL894Qw15xbt7cSUy', 'https://i.scdn.co/image/ab67616d0000b2738874d42c6591770e15618d13', 230557, 'https://open.spotify.com/track/6lfSsCL894Qw15xbt7cSUy', 'happy'),
('Haan Main Galat', '2020-02-14', 'Pritam', '0MLZAgKQKHbPsJ12qHS860', 'https://i.scdn.co/image/ab67616d0000b273095191f6b96fd9eff585befc', 218644, 'https://open.spotify.com/track/0MLZAgKQKHbPsJ12qHS860', 'happy'),
('Bandook Meri Laila (From "A Gentleman") (feat. Raftaar, Sidharth Malhotra)', '2017-08-16', 'Ash King', '3eDsxtliwduBeC3UAdOMG5', 'https://i.scdn.co/image/ab67616d0000b27310f6e20e16cfb965735480bc', 214510, 'https://open.spotify.com/track/3eDsxtliwduBeC3UAdOMG5', 'happy'),
('Hookah Bar', '2012-10-30', 'Himesh Reshammiya', '4AoQVhME8Ko6LNm4lV2wwQ', 'https://i.scdn.co/image/ab67616d0000b27333f89bc08b2d9cda09a857a1', 254351, 'https://open.spotify.com/track/4AoQVhME8Ko6LNm4lV2wwQ', 'happy'),
('Kamariya', '2018-08-22', 'Aastha Gill', '5cjVsWqIkBQC7acTRhL0RO', 'https://i.scdn.co/image/ab67616d0000b273757e3e10c59c6e71affce6d6', 187982, 'https://open.spotify.com/track/5cjVsWqIkBQC7acTRhL0RO', 'happy'),
('Afghan Jalebi (Film Version)', '2015-07-31', 'Pritam', '1rEVydQSe04NJUqyyEyeEq', 'https://i.scdn.co/image/ab67616d0000b2737037561f1e7a2c5ff3cd0a38', 224206, 'https://open.spotify.com/track/1rEVydQSe04NJUqyyEyeEq', 'happy'),
('Kar Gayi Chull', '2017-12-15', 'Badshah', '3fPgIknlkDWXs1l2noKZbp', 'https://i.scdn.co/image/ab67616d0000b27310c94c7cd311738c527ae46f', 187710, 'https://open.spotify.com/track/3fPgIknlkDWXs1l2noKZbp', 'happy');
