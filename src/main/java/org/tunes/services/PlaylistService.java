package org.tunes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tunes.dto.*;
import org.tunes.dto.SongInfo;
import org.tunes.models.Playlists;
import org.tunes.models.Songs;
import org.tunes.models.Users;
import org.tunes.repositories.PlaylistRepository;
import org.tunes.repositories.SongRepository;
import org.tunes.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    public Playlists createPlaylist(Integer userId, String name, String emotion, String coverUrl) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));
        Optional<Playlists> existingPlaylist = playlistRepository.findByNameAndUser(name,user);
        if (existingPlaylist.isPresent()) {

            Playlists alreadyExists = existingPlaylist.get();
            Playlists newPlaylist = new  Playlists();
            newPlaylist.setEmotion(emotion);
            boolean isExistingEmolist = alreadyExists.isEmoList() ;
            boolean isNewEmolist = emotion != null && !emotion.trim().isEmpty() && !"null".equalsIgnoreCase(emotion.trim());
            if(isExistingEmolist == isNewEmolist){
                String message = isNewEmolist
                        ? "Emolist with name '" + name + "' already exists"
                        : "Playlist with name '" + name + "' already exists";
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, message);
            }

        }
    Playlists playlist = new Playlists();

        playlist.setUser(user);
        playlist.setName(name);
        playlist.setCoverUrl(coverUrl);
        playlist.setEmotion(emotion);
        return playlistRepository.save(playlist);

    }

    public List<PlaylistFE> getPlaylistsByUser(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));
        List<Playlists> playlists = playlistRepository.findByUserAndEmotionIsNull(user);
        if (playlists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " You have not created any playlists yet");
        }


        return playlists.stream()
                .map(p -> PlaylistFE.builder()
                        .playlistId(p.getId())
                        .playlistName(p.getName())
                        .coverUrl(p.getCoverUrl())
                        .songs(p.getSongs() != null
                                ? p.getSongs().stream()
                                .map(song -> SongInfo.builder()
                                        .songID(song.getSpotifyId())
                                        .songName(song.getTitle())
                                        .artistName(song.getArtistName())
                                        .coverURL(song.getCoverUrl())
                                        .songURL(song.getSongUrl())
                                        .duration(song.getDurationMilliseconds())
                                        .releaseDate(String.valueOf(song.getReleaseDate()))
                                        .build())
                                .collect(Collectors.toList())   // ✅ Correct usage here
                                : List.of())
                        .build())
                .collect(Collectors.toList());

    }

    public List<Playlists> getEmolistsByUser(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));
        List<Playlists> playlists = playlistRepository.findByUserAndEmotionIsNotNull(user);
        if (playlists.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " You have not created any emolists yet");
        }
        return playlists;
    }

    public Playlists getPlaylistByName(String name, Integer userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));
        Playlists playlist = playlistRepository.findByNameAndUser(name,user).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Playlist not found with name: " + name
        ));
        return playlist ;
    }


    public Playlists addSongToPlaylist(Integer playlistId, SongInfo songInfo, Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));


        Playlists playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with ID: " + playlistId));

        // Check if song already exists in DB
        Songs song = songRepository.findBySpotifyId(songInfo.getSongID())
                .orElseGet(() -> {
                    // If not, create new song entity
                    Songs newSong = new Songs();
                    newSong.setTitle(songInfo.getSongName());
                    newSong.setArtistName(songInfo.getArtistName());
                    if (songInfo.getReleaseDate() != null && !songInfo.getReleaseDate().isEmpty()) {
                        newSong.setReleaseDate(LocalDate.parse(songInfo.getReleaseDate()));
                    }
                    newSong.setSpotifyId(songInfo.getSongID());
                    newSong.setCoverUrl(songInfo.getCoverURL());
                    newSong.setDurationMilliseconds(songInfo.getDuration());
                    newSong.setSongUrl(songInfo.getSongURL());
                    return songRepository.save(newSong);
                });
        // ✅ Check if the song already exists in the playlist
        boolean songAlreadyExists = playlist.getSongs().stream()
                .anyMatch(existingSong -> existingSong.getSpotifyId().equals(song.getSpotifyId()));

        if (songAlreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Song already exists in your playlist");
        }
        // Add song to playlist if not already added
        playlist.getSongs().add(song);

        // Save updated playlist
        return playlistRepository.save(playlist);
    }

    /**
     * Optional: Remove a song from a playlist
     */
    public Playlists removeSongFromPlaylist(Integer playlistId, String spotifyId,  Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));

        Playlists playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found with ID: " + playlistId));

        Songs song = songRepository.findBySpotifyId(spotifyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found with Spotify ID: " + spotifyId));

        playlist.getSongs().remove(song);
        return playlistRepository.save(playlist);
    }

    public List<SongInfo> getPlaylistSongs(Integer playlistId, Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with ID: " + userId));

        Playlists playlist = playlistRepository.findByIdAndUser(playlistId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist not found"));


        List<Songs> songInfos = playlist.getSongs();
        if (songInfos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No songs in this playlist, add songs to view them");
        }
        return songInfos.stream()
                .map(song -> SongInfo.builder()
                        .songID(song.getSpotifyId())
                        .songName(song.getTitle())
                        .artistName(song.getArtistName())
                        .coverURL(song.getCoverUrl())
                        .songURL(song.getSongUrl())
                        .duration(song.getDurationMilliseconds())
                        .releaseDate(String.valueOf(song.getReleaseDate()))
                        .build())
                .collect(Collectors.toList());



    }
}
