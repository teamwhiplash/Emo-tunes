package org.tunes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tunes.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findBySpotifyId(String spotifyId);
    User findByEmail(String email);
    User findByUsername(String username);
}
