package org.tunes.repositories;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tunes.models.Songs;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Songs, Integer> {
    Optional<Songs>findById(Integer id); // optional, if you want to find by Spotify ID

    Optional<Songs> findBySpotifyId(String songID);

    @org.springframework.data.jpa.repository.Query(
            value = "SELECT * FROM songs s WHERE s.emotion = :emotion LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<Songs> findSongsByEmotion(String emotion, Integer limit, Integer offset);
}
