package org.tunes.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tunes.controllers.SpotifyCallbackController;
import org.tunes.dto.SongInfo;
import java.util.function.BiFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SongMapper {
    public Logger log = LoggerFactory.getLogger(SongMapper.class);

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
    public Map<String, Object> extractFirstTrack(Map<String, Object> response) {
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
    public SongInfo extractTrack(Map<String, Object> response) {
        log.debug("Received Spotify payload: {}", response);

        Map<String, Object> firstTrack = locateFirstTrack(response);
        if (firstTrack == null) {
            log.warn("No track could be extracted from the Spotify response");
            return null;                     // caller can decide what to do with a null
        }

        // --------------------------------------------------------------
        // 1️⃣  Extract the fields required by SongInfo
        // --------------------------------------------------------------
        String songId   = getString(firstTrack, "id");
        String songName = getString(firstTrack, "name");

        Integer durationMs = firstTrack.get("duration_ms") instanceof Number
                ? ((Number) firstTrack.get("duration_ms")).intValue()
                : null;
        Integer durationSec = (durationMs != null) ? durationMs / 1000 : null;

        // artist – take the first element of the artists array
        String artistName = Optional.ofNullable(getList(firstTrack, "artists"))
                .filter(l -> !l.isEmpty())
                .map(l -> getString(l.get(0), "name"))
                .orElse(null);

        // album data ----------------------------------------------------
        Map<String, Object> album = getMap(firstTrack, "album");
        String releaseDate = getString(album, "release_date");

        // cover image – first image in album.images
        String coverUrl = Optional.ofNullable(getList(album, "images"))
                .filter(l -> !l.isEmpty())
                .map(l -> getString(l.get(0), "url"))
                .orElse(null);

        // external Spotify URL
        Map<String, Object> externalUrls = getMap(firstTrack, "external_urls");
        String songUrl = getString(externalUrls, "spotify");

        log.info("Mapped track {} – {}", songId, songName);

        return SongInfo.builder()
                .songID(songId)
                .songName(songName)
                .artistName(artistName)
                .releaseDate(releaseDate)
                .coverURL(coverUrl)
                .duration(durationSec)
                .songURL(songUrl)
                .build();
    }

    // -----------------------------------------------------------------
    // 2️⃣  Helper that finds the *first* track, handling every known shape
    // -----------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private Map<String, Object> locateFirstTrack(Map<String, Object> response) {
        if (response == null) {
            return null;
        }

        // ----------- a) search‑tracks response : tracks → items ---------
        Object tracksObj = response.get("tracks");
        if (tracksObj instanceof Map) {
            Map<String, Object> tracksMap = (Map<String, Object>) tracksObj;
            Object itemsObj = tracksMap.get("items");
            if (itemsObj instanceof List) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObj;
                if (!items.isEmpty()) {
                    return items.get(0);
                }
            }
        }

        // ----------- b) your current payload : tracks is a List ----------
        if (tracksObj instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) tracksObj;
            if (!list.isEmpty()) {
                return list.get(0);          // first element *is* the track
            }
        }

        // ----------- c) playlist‑items response : items → track ----------
        Object itemsObj = response.get("items");
        if (itemsObj instanceof List) {
            List<Map<String, Object>> wrappers = (List<Map<String, Object>>) itemsObj;
            for (Map<String, Object> wrapper : wrappers) {
                Object inner = wrapper.get("track");
                if (inner instanceof Map) {
                    return (Map<String, Object>) inner;   // first non‑null track
                }
            }
        }

        // ----------- d) single‑track payload (already a track) ----------
        if (response.containsKey("id") && response.containsKey("name")) {
            return response;                 // the whole map is the track
        }

        // nothing matched
        return null;
    }

    // -----------------------------------------------------------------
    // 3️⃣  Tiny “safe‑cast” helpers – keep the main code tidy
    // -----------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private static String getString(Map<String, Object> map, String key) {
        return (map != null && map.get(key) instanceof String) ? (String) map.get(key) : null;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Map<String, Object> map, String key) {
        return (map != null && map.get(key) instanceof Map) ? (Map<String, Object>) map.get(key) : null;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> getList(Map<String, Object> map, String key) {
        return (map != null && map.get(key) instanceof List) ? (List<Map<String, Object>>) map.get(key) : null;
    }
}

