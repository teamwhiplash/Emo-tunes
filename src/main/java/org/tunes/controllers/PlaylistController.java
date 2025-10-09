package org.tunes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunes.dto.PlaylistRequest;
import org.tunes.dto.SongInfo;
import org.tunes.models.Playlists;
import org.tunes.services.PlaylistService;
import org.tunes.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserService userService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    // ✅ Create a playlist
    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(
            @RequestBody PlaylistRequest request,
            @RequestParam Integer userId) {

        Playlists playlist = playlistService.createPlaylist(
                userId,
                request.getName(),
                request.getEmotion(),
                request.getCoverUrl()
        );
        return ResponseEntity.ok(playlist);
    }

    // ✅ Create an emotion-based playlist


    // ✅ Get all playlists of a user
    @GetMapping("/user")
    public ResponseEntity<?> getPlaylistsByUser(@RequestParam Integer userId) {
        return ResponseEntity.ok(playlistService.getPlaylistsByUser(userId));
    }
    @GetMapping("/user/emolist")
    public ResponseEntity<?> getEmolistsByUser(@RequestParam Integer userId) {
        return ResponseEntity.ok(playlistService.getEmolistsByUser(userId));
    }

    // ✅ Add a song to a playlist
    @PostMapping("/{playlistId}/add-song")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Integer playlistId,
            @RequestParam Integer userId,
            @RequestBody SongInfo songInfo) {

        Playlists updatedPlaylist = playlistService.addSongToPlaylist(playlistId, songInfo,userId);

        return ResponseEntity.ok(updatedPlaylist);
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<SongInfo>> getSongsInPlaylist(
            @PathVariable Integer playlistId,
            @RequestParam Integer userId) {   // Pass userId as request param

        List<SongInfo> songs = playlistService.getPlaylistSongs(playlistId, userId);
        return ResponseEntity.ok(songs);
    }
}
