package org.tunes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.tunes.config.Credentials;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class SpotifyLoginController {

    @Autowired
    private Credentials credentials;

    @GetMapping("/spotify/login")
    public RedirectView login() {
        String clientId = credentials.getCLIENT_ID();
        String redirectUri = credentials.getREDIRECT();
        String scope = "user-read-email playlist-modify-private"; // add scopes you need
        String state = "xyz123"; // random string for security


        String url = "https://accounts.spotify.com/authorize"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8)
                + "&state=" + state;

        return new RedirectView(url);
    }
}
