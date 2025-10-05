package org.tunes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.tunes.components.TokenStore;
import org.tunes.dto.SongInfo;
import org.tunes.services.SongMapper;
import org.tunes.services.SpotifySearch;
import reactor.core.publisher.Mono;
import org.tunes.dto.songMetaDTO;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

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
        String access = tokenStore.getValidAccessToken(1);   // demo uid = 5

        // 2️⃣ Get song info
        Map<String, Object> songResponse = spotifySearch.RequestSong(
                access,
                "Saiyaara",
                new SpotifySearch.SearchSong(),"1","0");

        SongInfo song = mapper.toSongInfo(songResponse);

        // 3️⃣ Build JSON payload for n8n
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("songName", song.getSongName());
        requestBody.put("artistName", song.getArtistName());
        requestBody.put("songYear", song.getReleaseDate());

        // 4️⃣ Send POST to n8n webhook (wait max 60s)
        return webClient.post()
                .uri("/webhook-test/songmeta")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))  // ⏱ Wait max 1 minute
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
                })
                .onErrorResume(TimeoutException.class, e -> {
                    String msg = "⏳ n8n did not respond within 60 seconds.";
                    log.warn(msg);
                    return Mono.just(msg);
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    String msg = "❌ n8n returned an error: " + e.getStatusCode() + " " + e.getResponseBodyAsString();
                    log.error(msg);
                    return Mono.just(msg);
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