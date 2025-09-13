package org.tunes.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Service
public class SpotifyTokenService implements TokenInterface {

    @Autowired
    private SpotifyCred credentials;

    private final String tokenUrl = "https://accounts.spotify.com/api/token";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken(String authorizationCode) {
        Map<String, Object> response = requestTokens("authorization_code", authorizationCode, null);
        return response != null ? (String) response.get("access_token") : null;
    }

    @Override
    public String getRefreshToken(String authorizationCode) {
        Map<String, Object> response = requestTokens("authorization_code", authorizationCode, null);
        return response != null ? (String) response.get("refresh_token") : null;
    }

    @Override
    public String refreshAccess(String refreshToken) {
        Map<String, Object> response = requestTokens("refresh_token", null, refreshToken);
        return response != null ? (String) response.get("access_token") : null;
    }

    private Map<String, Object> requestTokens(String grantType, String code, String refreshToken) {
        String clientId = credentials.giveClient_ID();
        String clientSecret = credentials.giveClient_Secret();
        String spotifyRedirect = credentials.getRedirect();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String creds = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        headers.set("Authorization", "Basic " + creds);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);

        if ("authorization_code".equals(grantType) && code != null) {
            body.add("code", code);
            if (spotifyRedirect != null) {
                body.add("redirect_uri", spotifyRedirect);
            }
        } else if ("refresh_token".equals(grantType) && refreshToken != null) {
            body.add("refresh_token", refreshToken);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(tokenUrl, HttpMethod.POST, request,
                            new ParameterizedTypeReference<Map<String, Object>>() {});

            return response.getBody();
        } catch (RestClientException ex) {
            System.err.println("Error while requesting Spotify token: " + ex.getMessage());
            return Collections.emptyMap(); // return safe empty map
        }
    }
}
