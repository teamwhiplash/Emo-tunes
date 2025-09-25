package org.tunes.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SongHandle {
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractSongInfo(Map<String, Object> response) {
        Map<String, Object> songInfo = new HashMap<>();

        if (response == null) {
            songInfo.put("error", "No response received");
            return songInfo;
        }

        // "tracks" object
        Map<String, Object> tracks = (Map<String, Object>) response.get("tracks");
        if (tracks == null) {
            songInfo.put("error", "No tracks found");
            return songInfo;
        }

        // "items" is a list of songs
        List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
        if (items == null || items.isEmpty()) {
            songInfo.put("error", "No items found");
            return songInfo;
        }

        // Get the first track
        Map<String, Object> firstTrack = items.get(0);

        // Song name
        String songName = (String) firstTrack.get("name");

        // Artist name (take first artist)
        List<Map<String, Object>> artists = (List<Map<String, Object>>) firstTrack.get("artists");
        String artistName = artists != null && !artists.isEmpty()
                ? (String) artists.get(0).get("name")
                : "Unknown Artist";

        // Album -> release_date
        Map<String, Object> album = (Map<String, Object>) firstTrack.get("album");
        String releaseDate = album != null ? (String) album.get("release_date") : "Unknown Year";

        String preview = firstTrack.get("preview_url")!= null ? firstTrack.get("preview_url").toString() : "No preview Available";

        // Build the return map
        songInfo.put("songName", songName);
        songInfo.put("artistName", artistName);
        songInfo.put("releaseDate", releaseDate);
        songInfo.put("preview_url",preview);

        return songInfo;
    }

}
