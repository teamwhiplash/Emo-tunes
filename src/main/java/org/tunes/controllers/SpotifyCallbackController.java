package org.tunes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tunes.auth.TokenInterface;

@RestController
public class SpotifyCallbackController {

    @Autowired
    private TokenInterface tokenService;

    @GetMapping("/callback")
    public String handleSpotifyCallback(@RequestParam("code") String authorizationCode,
                                        @RequestParam(value = "state", required = false) String state) {
        try {
            // Pass the auth code to service
            String accessToken = tokenService.getAccessToken(authorizationCode);
            String refreshToken = tokenService.getRefreshToken(authorizationCode);

            // Print them in the browser
            return "<h2>✅ Spotify Authorization Successful!</h2>"
                    + "<p><b>Access Token:</b> " + accessToken + "</p>"
                    + "<p><b>Refresh Token:</b> " + refreshToken + "</p>";
        } catch (Exception e) {
            return "<h2>❌ Error during token exchange:</h2><p>" + e.getMessage() + "</p>";
        }
    }
}
