package org.tunes.controllers;

import org.tunes.services.SpotifySearch;
import org.tunes.services.SongHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tunes.auth.TokenInterface;

import java.util.Map;

@RestController
public class SpotifyCallbackController {

    @Autowired
    private TokenInterface tokenService;
    @Autowired
    public SpotifySearch Search;
    @Autowired
    public SongHandle S_Handler;

    @GetMapping("/callback")
    public String handleSpotifyCallback(@RequestParam("code") String authorizationCode,
                                        @RequestParam(value = "state", required = false) String state) {
        try {
            // Pass the auth code to service
            Map<String , Object> accessToken = tokenService.getAccessToken(authorizationCode);
            String AT = (String) accessToken.get("access_token");
            int time = (int) accessToken.get("expires_in");
            String RF =(String) accessToken.get("refresh_token");
            Map<String,Object> response = Search.SearchSong(AT,"Rasputin");
            Map<String,Object> Song = S_Handler.extractSongInfo(response);


            // Print them in the browser
            return "<h2>✅ Spotify Authorization Successful!</h2>"
                    + "<p><b>Access Token:</b> " + accessToken.get("access_token") + "</p>" +"<p><b>Expires in:</b> " + time + "</p>"
                    + "<p><b>Refresh Token:</b> " + RF + "</p>"+
                    "<p><b>Refreshed Access Token:</b>"+ tokenService.refreshAccess(RF) +
                    "<p><b>Requested Song:</b></p>"+"<p><b>Title:</b>"+ Song.get("songName")+"</p>"+
                    "<p><b>Year:</b>"+Song.get("releaseDate")+"</p>"+"<p><b>Artist:</b>"+Song.get("artistName")+"</p>"+"<p><b>Preview URL:</b>"+Song.get("preview_url")+"</p>";


        } catch (Exception e) {
            return "<h2>❌ Error during token exchange:</h2><p>" + e.getMessage() + "</p>";
        }
    }
}
