package org.tunes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.tunes.components.TokenStore;
import org.tunes.dto.SongInfo;
import org.tunes.services.SongMapper;
import org.tunes.services.SpotifySearch;
import reactor.core.publisher.Mono;
import org.tunes.dto.songMetaDTO;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/songmeta")
@Slf4j
public class Song_meta {

    private final ObjectMapper prettyMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final WebClient webClient;
    private final SpotifySearch spotifySearch;
    private final SongMapper mapper;
    private final TokenStore tokenStore;

    // ----- constructor injection -----
    public Song_meta(WebClient.Builder webClientBuilder,
                     SpotifySearch spotifySearch,
                     SongMapper mapper,
                     TokenStore tokenStore) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
        this.spotifySearch = spotifySearch;
        this.mapper = mapper;
        this.tokenStore = tokenStore;
    }

    // ----- POST endpoint to send song info to n8n -----
    @PostMapping("/send")
    public Mono<String> sendSongMeta() {
        // 1️⃣ Get a valid access token
        String access = tokenStore.getValidAccessToken(5);   // demo uid = 5

        // 2️⃣ Get song info
        Map<String, Object> songResponse = spotifySearch.RequestSong(
                access,
                "Saiyaara",
                new SpotifySearch.SearchSong());

        SongInfo song = mapper.toSongInfo(songResponse);

        // 3️⃣ Build JSON payload for n8n
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("songName", song.getSongName());
        requestBody.put("artistName", song.getArtistName());
        requestBody.put("songYear", song.getReleaseDate());

        // 4️⃣ Send POST to n8n webhook and log response
        return webClient.post()
                .uri("/webhook-test/songmeta")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    try {
                        Object json = prettyMapper.readValue(response, Object.class);
                        String prettyJson = prettyMapper.writeValueAsString(json);
                        System.out.println("\n=== Response from n8n ===\n" + prettyJson + "\n=========================\n");
                        log.info("Response from n8n:\n{}", prettyJson);
                    } catch (Exception e) {
                        System.out.println("Raw n8n response: " + response);
                        log.warn("Could not pretty-print n8n response", e);
                    }
                });
    }

    // ----- POST endpoint to receive emotional analysis -----
    @PostMapping("/receive")
    public ResponseEntity<String> receiveEmotion(@RequestBody songMetaDTO dto) {

        try {
            String prettyJson = prettyMapper.writeValueAsString(dto);
            System.out.println("\n=== Received Emotion JSON ===\n" + prettyJson + "\n==============================\n");
            log.info("Received emotion payload:\n{}", prettyJson);
        } catch (Exception e) {
            log.warn("Could not pretty-print payload", e);
            log.info("Raw payload: {}", dto);
        }

        // Additional processing can go here (e.g., save to DB)
        return ResponseEntity.ok("Payload received");
    }
}

//@RestController
//@RequestMapping("/songmeta")
//@Slf4j
//public class Song_meta {
//    private final ObjectMapper prettyMapper = new ObjectMapper()
//            .enable(SerializationFeature.INDENT_OUTPUT);
//
//    private final WebClient webClient;
//    private final SpotifySearch spotifySearch;
//    private final SongMapper mapper;
//    private final TokenStore tokenStore;
//
//    // ----- constructor injection (Spring will supply the beans) -----
//    public Song_meta(WebClient.Builder webClientBuilder,
//                              SpotifySearch spotifySearch,
//                              SongMapper mapper,
//                              TokenStore tokenStore) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
//        this.spotifySearch = spotifySearch;
//        this.mapper = mapper;
//        this.tokenStore = tokenStore;
//    }
//
//    // ----- POST endpoint (the only HTTP method that matches) -----
//    @PostMapping("/send")
//    public Mono<String> sendSongMeta() {
//        // ----- 1️⃣  Get a valid access token (no NPE any more) -----
//        String access = tokenStore.getValidAccessToken(5);   // demo uid = 5
//
//        // ----- 2️⃣  Call Spotify (or whatever service) -----
//        Map<String, Object> songResponse = spotifySearch.RequestSong(
//                access,
//                "Saiyaara",
//                new SpotifySearch.SearchSong());
//
//        SongInfo song = mapper.toSongInfo(songResponse);
//
//        // ----- 3️⃣  Build JSON that n8n expects -----
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("songName", song.getSongName());
//        requestBody.put("artistName", song.getArtistName());
//        requestBody.put("songYear", song.getReleaseDate());
//
//        // ----- 4️⃣  Forward it to the n8n webhook (POST) -----
//        return webClient.post()
//                .uri("/webhook-test/songmeta")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class);
//    }
//    @PostMapping("/receive")
//    public ResponseEntity<String> receiveEmotion(@RequestBody songMetaDTO dto) {
//
//        // ----- 1️⃣  Pretty‑print the JSON to the terminal -----
//        try {
//            String prettyJson = prettyMapper.writeValueAsString(dto);
//            // System.out prints directly to the console (STDOUT)
//            System.out.println("\n=== Received Emotion JSON ===\n" + prettyJson + "\n==============================\n");
//            // Also log it – useful when you run the app with a logger framework
//            log.info("Received emotion payload:\n{}", prettyJson);
//        } catch (Exception e) {
//            // If something goes wrong with serialization just fall back to a simple log
//            log.warn("Could not pretty‑print payload", e);
//            log.info("Raw payload: {}", dto);
//        }
//
//        // ----- 2️⃣  (any other processing you need) ----------
//        // e.g. save to DB, call another service, etc.
//        // emotionService.save(dto);
//
//        // ----- 3️⃣  Return a tiny confirmation string ----------
//        return ResponseEntity.ok("Payload received");
//    }
//}





//package org.tunes.controllers;
//
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.tunes.components.TokenStore;
//import org.tunes.dto.SongInfo;
//import org.tunes.services.SongMapper;
//import org.tunes.services.SpotifySearch;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/songmeta")
//public class Song_meta {   // ✅ Class name updated
//
//    private final WebClient webClient;
//    public SpotifySearch search;
//    public SongMapper mapper;
//
//    // ✅ Proper constructor (no return type!)
//    public Song_meta(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
//    }
//
//    @PostMapping("/send")
//    public Mono<String> sendSongMeta() {
//        TokenStore tokenStore = null;
//        Integer uid =5;
//        String access = tokenStore.getValidAccessToken(uid);
//
//        // Prepare JSON payload
//
//        Map<String,Object> songResponse = search.RequestSong(access,"Saiyaara", new SpotifySearch.SearchSong());
//        SongInfo Song = mapper.toSongInfo(songResponse);
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("songName", Song.getSongName());
//        requestBody.put("artistName", Song.getArtistName());
//        requestBody.put("songYear", Song.getReleaseDate());
//
//        // Send POST request to n8n webhook
//        return webClient.post()
//                .uri("/webhook-test/songmeta")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class);
//    }
//}
//
