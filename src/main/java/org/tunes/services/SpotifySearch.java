package org.tunes.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


interface SpotifySearchBuilder{
   public String BuildUrl(String Query);
}

class SearchID implements SpotifySearchBuilder{
    private final String BaseURL="https://api.spotify.com/v1/tracks/";
@Override
public String BuildUrl(String Query){
    String ID_URL;
    ID_URL = BaseURL+ Query;

    return ID_URL;
}
}

//public class SearchSong implements SpotifySearchBuilder{
//    private final String baseURL="https://api.spotify.com/v1/search";
//    @Override
//    public String BuildUrl(String Query){
//        String encoded = URLEncoder.encode(Query,StandardCharsets.UTF_8);
//        String Name_URL;
//        Name_URL= baseURL+ "?q=" + encoded + "&type=track&limit=1";
//
//        return Name_URL;
//    }
//}
//
@Service
public class SpotifySearch {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public interface SpotifySearchBuilder {
        String BuildUrl(String query);
    }

    public static class SearchSong implements SpotifySearchBuilder {
        private final String baseURL = "https://api.spotify.com/v1/search";

        @Override
        public String BuildUrl(String query) {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            return baseURL + "?q=" + encoded + "&type=track&limit=1";
        }
    }

    /**
     * Requests a song from Spotify and returns the parsed response body as a Map.
     * Handles nested JSON safely and returns the first track if available.
     */
    @NonNull
    public Map<String, Object> RequestSong(String accessToken, String query, SpotifySearchBuilder builder) {
        String url = builder.BuildUrl(query);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Step 1: Get raw JSON string from Spotify
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String rawJson = rawResponse.getBody();
            System.out.println("Raw JSON from Spotify: " + rawJson);

            if (rawJson == null || rawJson.isEmpty()) {
                throw new RuntimeException("Spotify returned empty response body");
            }

            // Step 2: Parse JSON into Map
            Map<String, Object> responseMap = objectMapper.readValue(rawJson, Map.class);

            // Step 3: If tracks → items exists, return first track
            if (responseMap.containsKey("tracks")) {
                Map<String, Object> tracks = (Map<String, Object>) responseMap.get("tracks");
                if (tracks != null) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) tracks.get("items");
                    if (items != null && !items.isEmpty()) {
                        return items.get(0); // return first track directly
                    }
                }
            }

            // Fallback: return full response map if no tracks found
            return responseMap;

        } catch (Exception e) {
            System.out.println("❌ Error calling Spotify API: " + e.getMessage());
            throw new RuntimeException("Error searching Spotify", e);
        }
    }
}













//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URLEncoder;
//import org.springframework.http.HttpHeaders;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//@Service
//public class SpotifySearch {
//    private final String baseURL="https://api.spotify.com/v1/search";
//    private final RestTemplate restTemplate= new RestTemplate();
//
//    public Map<String, Object> SearchSong(String AccessToken, String Query){
//        String encodeQuery = URLEncoder.encode(Query, StandardCharsets.UTF_8);
//
//        String URL = baseURL + "?q="+ encodeQuery + "&type=track&limit=5";
//        HttpHeaders headers =new HttpHeaders();
//        headers.set("Authorization","Bearer "+ AccessToken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        try{
//            ResponseEntity<Map<String,Object>> response = restTemplate.exchange(
//                    URL,
//                    HttpMethod.GET,
//                    entity,
//                    new ParameterizedTypeReference<Map<String,Object>>(){}
//            );
//            return response.getBody();
//        }catch(Exception e){
//            System.out.println("❌ Error calling Spotify API: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Error Searching for Song",e);
//        }
//    }
//}
