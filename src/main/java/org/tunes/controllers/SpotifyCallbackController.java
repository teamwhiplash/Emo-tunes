package org.tunes.controllers;

import org.springframework.http.ResponseEntity;
import org.tunes.components.TokenStore;
import org.tunes.dto.UserInfo;
import org.tunes.models.Users;
import org.tunes.repositories.UserRepository;
import org.tunes.services.SpotifySearch;
import org.tunes.services.SongHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tunes.auth.TokenInterface;
import org.tunes.services.SpotifyUserService;
import org.tunes.services.UserService;
import java.util.HashMap;
import java.util.Map;


@RestController
public class SpotifyCallbackController {

    @Autowired
    private TokenInterface tokenService;
    @Autowired
    public SpotifySearch Search;
    @Autowired
    public SongHandle S_Handler;

    @Autowired
    private SpotifyUserService spotifyUserService;
    @Autowired
    public UserService userService;
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TokenStore tokenStore;



    @GetMapping("/callback")
    public ResponseEntity<UserInfo> handleSpotifyCallback(@RequestParam("code") String authorizationCode,
                                                          @RequestParam(value = "state", required = false) String state) {
        try {
            // Pass the auth code to service
            Map<String , Object> accessToken = tokenService.getAccessToken(authorizationCode);
            String AT = (String) accessToken.get("access_token");
            String RF =(String) accessToken.get("refresh_token");
            Map<String, Object> userDetails = spotifyUserService.getCurrentUser(AT);
            String spotifyId = (String) userDetails.get("id");
            String email = (String) userDetails.get("email");
            String displayName = (String) userDetails.get("display_name");
            Map<Long , String> accessTokenStore = new HashMap<>();
            Users user = new Users();
            user.setSpotifyId(spotifyId);
            user.setEmail(email);
            user.setUsername(displayName);
            user.setRefreshToken(RF);


            userService.saveOrUpdateUser(user);
            Users dbUser = userRepository.findBySpotifyId(spotifyId);
            Long userId = dbUser.getId();
            String username = dbUser.getUsername();
            String emailId = dbUser.getEmail();
            tokenStore.put(userId, AT);
            System.out.println(tokenStore.accessTokenStore);
            System.out.println("User logged in successfully");
            UserInfo dto = new UserInfo(userId, username, emailId);
            System.out.println(dto);
            return ResponseEntity.ok(dto);



        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }
}
