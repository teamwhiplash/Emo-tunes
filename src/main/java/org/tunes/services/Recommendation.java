package org.tunes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tunes.dto.SongInfo;
import org.tunes.models.Songs;
import org.tunes.models.Users;
import org.tunes.repositories.SongRepository;
import org.tunes.repositories.UserRepository;

import java.util.List;

@Service
public class Recommendation {

    @Autowired private UserRepository userRepository;
    @Autowired private SongRepository songRepository;

    public List<SongInfo> findAllSongs(Integer userId, String emotion, Integer limit, Integer offset) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        List<Songs> songs = songRepository.findSongsByEmotion(emotion, limit, offset);


        if (songs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No songs found");
        }

        // Map Songs entities to SongInfo DTOs
        return songs.stream()
                .map(song -> SongInfo.builder()
                        .songID(song.getSpotifyId())
                        .songName(song.getTitle())
                        .artistName(song.getArtistName())
                        .coverURL(song.getCoverUrl())
                        .songURL(song.getSongUrl())
                        .duration(song.getDurationMilliseconds())
                        .releaseDate(song.getReleaseDate() != null ? song.getReleaseDate().toString() : null)
                        .build())
                .toList();
    }

}

