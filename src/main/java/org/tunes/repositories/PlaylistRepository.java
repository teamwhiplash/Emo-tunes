package org.tunes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tunes.models.Playlists;
import org.tunes.models.Songs;
import org.tunes.models.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlists, Integer> {
    List<Playlists> findByUser(Users user);
    List<Playlists> findByEmotion(String emotion);
    Optional<Playlists> findByName(String name);
    Optional<Playlists> findById(Integer id);
    Optional<Playlists> findByNameAndUser(String name, Users user);
    Optional<Playlists> findByIdAndUser(Integer id, Users user);
    // All playlists (normal, emotion = null or empty)
    List<Playlists> findByUserAndEmotionIsNull(Users user);

    // All emo-lists (emotion not null and not empty)
    List<Playlists> findByUserAndEmotionIsNotNull(Users user);
}

