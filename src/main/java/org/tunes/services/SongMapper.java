package org.tunes.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tunes.dto.SongInfo;

import java.util.ArrayList;
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
        System.out.println(trackData);


        String songID = Optional.ofNullable((String) trackData.get("id"))
                .orElse("Unknown ID");

        String songName = Optional.ofNullable((String) trackData.get("name"))
                .orElse("Unknown Song");

        String artistName = extractFirstArtistName(trackData)
                .orElse("Unknown Artist");
        String coverUrl = extractCoverUrl(trackData).orElse("No Cover URL");
        String releaseDate = extractReleaseDate(trackData)
                .orElse("Unknown Year");
        Integer duration =  extractDuration(trackData).orElse(null);
        String songUrl = extractSongUrl(trackData).orElse("Unknown Song");

        return SongInfo.builder()
                .songID(songID)
                .songName(songName)
                .artistName(artistName)
                .coverURL(coverUrl)
                .songURL(songUrl)
                .duration(duration)
                .releaseDate(releaseDate)
                .build();
    }
    @SuppressWarnings("unchecked")
    public List<SongInfo> toSongInfoList(Map<String, Object> response) {
        List<SongInfo> songList = new ArrayList<>();

        if (response == null || response.isEmpty()) return songList;

        // The "tracks" key might contain either a List<Map> directly or Map with "items"
        Object tracksObj = response.get("tracks");
        List<Map<String, Object>> items;

        if (tracksObj instanceof List) {
            items = (List<Map<String, Object>>) tracksObj;
        } else if (tracksObj instanceof Map) {
            Map<String,Object> tracksMap = (Map<String, Object>) tracksObj;
            items = (List<Map<String, Object>>) tracksMap.get("items");
        } else {
            return songList;
        }

        if (items == null) return songList;

        for (Map<String,Object> trackData : items) {
            // if trackData contains "track" key (like in playlists), unwrap it
            if (trackData.containsKey("track")) {
                trackData = (Map<String,Object>) trackData.get("track");
            }
            SongInfo song = SongInfo.builder()
                    .songID(Optional.ofNullable((String) trackData.get("id")).orElse("Unknown ID"))
                    .songName(Optional.ofNullable((String) trackData.get("name")).orElse("Unknown Song"))
                    .artistName(extractFirstArtistName(trackData).orElse("Unknown Artist"))
                    .coverURL(extractCoverUrl(trackData).orElse("No Cover URL"))
                    .releaseDate(extractReleaseDate(trackData).orElse("Unknown Year"))
                    .duration(extractDuration(trackData).orElse(null))
                    .songURL(extractSongUrl(trackData).orElse("Unknown Song"))
                    .build();

            songList.add(song);
        }

        return songList;
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
    @SuppressWarnings("unchecked")
    private Optional<String> extractCoverUrl(Map<String, Object> track) {
        if (track == null) {
            return Optional.empty();
        }

        Object albumObj = track.get("album");
        if (!(albumObj instanceof Map)) {
            return Optional.empty();
        }
        Map<String, Object> album = (Map<String, Object>) albumObj;

        Object imagesObj = album.get("images");
        if (!(imagesObj instanceof List)) {
            return Optional.empty();
        }
        List<?> imagesList = (List<?>) imagesObj;
        if (imagesList.isEmpty()) {
            return Optional.empty();
        }

        // Option: you might want to pick the “largest” image rather than just the first
        // For simplicity, let's just take the first element
        Object firstImageObj = imagesList.get(0);
        if (!(firstImageObj instanceof Map)) {
            return Optional.empty();
        }
        Map<String, Object> firstImage = (Map<String, Object>) firstImageObj;

        Object urlObj = firstImage.get("url");
        if (urlObj instanceof String) {
            return Optional.of((String) urlObj);
        } else {
            return Optional.empty();
        }
    }
    private Optional<Integer> extractDuration(Map<String, Object> track) {
            Object durationObj =  track.get("duration_ms");
            if (durationObj instanceof Number) {
                return Optional.of((Integer) durationObj);
            }
            return Optional.empty();
    }

    private Optional<String> extractSongUrl(Map<String, Object> track) {
        Map<String, Object> externalUrls = (Map<String, Object>) track.get("external_urls");
        if (externalUrls != null) {
            return Optional.ofNullable((String) externalUrls.get("spotify"));
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