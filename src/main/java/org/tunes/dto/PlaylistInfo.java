package org.tunes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



public class PlaylistInfo {
    private String playlistId;
    private String playlistName;
    private String coverUrl;
    private List<SongInfo> songs;

    public PlaylistInfo() {
    }

    public PlaylistInfo(String playlistId, String playlistName, String coverUrl, List<SongInfo> songs) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.coverUrl = coverUrl;
        this.songs = songs;
    }

    // Getters and setters
    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<SongInfo> getSongs() {
        return songs;
    }

    public void setSongs(List<SongInfo> songs) {
        this.songs = songs;
    }

}
