package org.tunes.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tunes.components.TokenStore;
import org.tunes.dto.PlaylistInfo;
import org.tunes.dto.SongInfo;
import org.tunes.services.SongMapper;
import org.tunes.services.SpotifySearch;

import java.util.*;

@RestController
@RequestMapping("/search")
public class SpotifySearchController {
    @Autowired
    private SpotifySearch spotifySearch;

    @Autowired
    private SongMapper mapper;

    @Autowired
    private SpotifySearch search;

    @Autowired
    private TokenStore tokenStore;


    @GetMapping("/song")
    public ResponseEntity SearchSong(@RequestParam String query, @RequestParam Integer userId,@RequestParam(defaultValue = "20") String limit,
                                     @RequestParam(defaultValue = "0") String offset){
        String accessToken = tokenStore.getValidAccessToken(userId);

    Map<String,Object> SongResponse = search.RequestSong(accessToken,query, new SpotifySearch.SearchSong(),limit,offset);
        List<SongInfo> songs = mapper.toSongInfoList(SongResponse);

            System.out.println("\n");
//            System.out.println(Song);
//            System.out.println("Song Name:"+ Song.getSongName()+"\n"+"Artist:"+ Song.getArtistName());
            return ResponseEntity.ok().body(songs);
    }

    @GetMapping("/playlist")
    public ResponseEntity SearchPlaylist(@RequestParam String query, @RequestParam(defaultValue = "20") String limit,
    @RequestParam(defaultValue = "0") String offset, @RequestParam Integer userId) {
        String accessToken = tokenStore.getValidAccessToken(userId);
        PlaylistInfo Playlist = search.RequestPlaylist(accessToken,query,limit,offset);
        System.out.println(Playlist);
        return ResponseEntity.ok().body(Playlist);
    }
}
