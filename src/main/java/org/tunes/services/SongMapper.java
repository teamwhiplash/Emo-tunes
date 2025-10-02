package org.tunes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.tunes.dto.SongInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SongMapper {

    public SongInfo toSongInfo(Map<String, Object> response) {
        if (response == null || response.isEmpty()) {
            return emptySongInfo("No response received");
        }

        Map<String, Object> trackData = extractFirstTrack(response);

        String songID = Optional.ofNullable((String) trackData.get("id"))
                .orElse("Unknown ID");

        String songName = Optional.ofNullable((String) trackData.get("name"))
                .orElse("Unknown Song");

        String artistName = extractFirstArtistName(trackData)
                .orElse("Unknown Artist");

        String releaseDate = extractReleaseDate(trackData)
                .orElse("Unknown Year");

        return SongInfo.builder()
                .songID(songID)
                .songName(songName)
                .artistName(artistName)
                .releaseDate(releaseDate)
                .build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractFirstTrack(Map<String, Object> response) {
        if (response.containsKey("tracks")) {
            Map<String, Object> tracks = (Map<String, Object>) response.get("tracks");
            if (tracks != null) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
                if (items != null && !items.isEmpty()) {
                    return items.get(0);
                }
            }
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private Optional<String> extractFirstArtistName(Map<String, Object> track) {
        List<Map<String, Object>> artists = (List<Map<String, Object>>) track.get("artists");
        if (artists != null && !artists.isEmpty()) {
            return Optional.ofNullable((String) artists.get(0).get("name"));
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private Optional<String> extractReleaseDate(Map<String, Object> track) {
        Map<String, Object> album = (Map<String, Object>) track.get("album");
        if (album != null) {
            return Optional.ofNullable((String) album.get("release_date"));
        }
        return Optional.empty();
    }

    private SongInfo emptySongInfo(String errorMessage) {
        return SongInfo.builder()
                .songID("N/A")
                .songName(errorMessage)
                .artistName("N/A")
                .releaseDate("N/A")
                .build();
    }
}