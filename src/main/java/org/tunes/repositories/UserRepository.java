package org.tunes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tunes.models.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findBySpotifyId(String spotifyId);
    Users findByEmail(String email);
    Users findByUsername(String username);

}
