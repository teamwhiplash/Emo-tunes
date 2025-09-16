package org.tunes.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class SpotifySearch {
    private final String baseURL="https://api.spotify.com/v1/search";
    private final RestTemplate restTemplate= new RestTemplate();

    public Map<String, Object> SearchSong(String AccessToken, String Query){
        String encodeQuery = URLEncoder.encode(Query, StandardCharsets.UTF_8);

        String URL = baseURL + "?q="+ encodeQuery + "&type=track&limit=5";
        HttpHeaders headers =new HttpHeaders();
        headers.set("Authorization","Bearer "+ AccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<Map<String,Object>> response = restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String,Object>>(){}
            );
            return response.getBody();
        }catch(Exception e){
            System.out.println("❌ Error calling Spotify API: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error Searching for Song",e);
        }
    }
}
