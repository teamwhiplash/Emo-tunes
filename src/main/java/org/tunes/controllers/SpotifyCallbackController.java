package org.tunes.controllers;

import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunes.dto.SongInfo;
import org.tunes.dto.UserInfo;
import org.tunes.models.Users;
import org.tunes.services.SongMapper;
import org.tunes.services.SpotifySearch;
import org.tunes.services.SpotifyUserService;
import org.tunes.auth.TokenInterface;
import org.tunes.components.TokenStore;
import org.tunes.components.TokenInfo;
import org.tunes.services.UserService;
import org.tunes.repositories.UserRepository;

@RestController
@RequestMapping("/")
public class SpotifyCallbackController {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyCallbackController.class);

    private final TokenInterface tokenService;
    private final SpotifyUserService spotifyUserService;
    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    private final TokenStore tokenStore;


    @Autowired
    public SpotifySearch search;
    @Autowired
    public SongMapper mapper;

    public SpotifyCallbackController(
           TokenInterface tokenService,
            SpotifyUserService spotifyUserService,
            UserService userService,
            UserRepository userRepository,
            TokenStore tokenStore) {

        this.tokenService = Objects.requireNonNull(tokenService);
        this.spotifyUserService = Objects.requireNonNull(spotifyUserService);
        this.userService = Objects.requireNonNull(userService);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.tokenStore = Objects.requireNonNull(tokenStore);
    }

    @GetMapping("/callback")
    public ResponseEntity<UserInfo> handleSpotifyCallback(
            @RequestParam(value = "code", required = false) String authorizationCode,
            @RequestParam(value = "state", required = false) String state) {

        if (authorizationCode == null || authorizationCode.isBlank()) {
            LOG.warn("Callback invoked without a Spotify authorization code (state={})", state);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Map<String, Object> tokenResponse = tokenService.getAccessToken(authorizationCode);
            String accessToken = (String) tokenResponse.get("access_token");
            String refreshToken = (String) tokenResponse.get("refresh_token");

            if (accessToken == null) {
                LOG.error("Token service returned no access_token for code {}", authorizationCode);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //Song Test:
            Map<String,Object> SongResponse = search.RequestSong(accessToken,"Saiyaara", new SpotifySearch.SearchSong());
            SongInfo Song = mapper.toSongInfo(SongResponse);
            System.out.println("\n");
            System.out.println(Song);
            System.out.println("Song Name:"+ Song.getSongName()+"\n"+"Artist:"+ Song.getArtistName());

            Map<String, Object> userDetails = spotifyUserService.getCurrentUser(accessToken);
            String spotifyId = (String) userDetails.get("id");
            String email = (String) userDetails.get("email");
            String displayName = (String) userDetails.get("display_name");

            if (spotifyId == null) {
                LOG.error("Spotify user profile missing 'id' – response: {}", userDetails);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Users user = new Users();
            user.setSpotifyId(spotifyId);
            user.setEmail(email);
            user.setUsername(displayName);
            user.setRefreshToken(refreshToken);
            Users persisted = userService.saveOrUpdateUser(user);

            if (persisted == null) {
                LOG.error("User service failed to persist user with Spotify ID {}", spotifyId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            int userId = persisted.getId();

            long expiresIn = ((Number) tokenResponse.get("expires_in")).longValue();
            TokenInfo tokenInfo = new TokenInfo(accessToken, expiresIn);
            tokenStore.putInto(userId, tokenInfo);

            LOG.info("User {} (id={}) logged in – access token cached", displayName, userId);

            UserInfo dto = new UserInfo(userId, persisted.getUsername(), persisted.getEmail());
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            if (e instanceof org.springframework.web.client.HttpClientErrorException.Unauthorized) {
                LOG.warn("Spotify token endpoint returned 401 – likely invalid/expired auth code", e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            if (e instanceof org.springframework.web.client.ResourceAccessException) {
                LOG.error("Unable to reach Spotify token endpoint ({}); client may retry",
                        e.getCause() != null ? e.getCause().getClass().getSimpleName() : "N/A", e);
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            }
            LOG.error("Unexpected error while processing Spotify callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
//package org.tunes.controllers;
//
//import java.util.Map;
//import java.util.Objects;
//import org.slf4j.*;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.tunes.auth.TokenInterface;
//import org.tunes.components.TokenInfo;
//import org.tunes.components.TokenStore;
//import org.tunes.dto.*;
//import org.tunes.models.User;
//import org.tunes.repositories.UserRepository;
//import org.tunes.services.*;
//
//@RestController
//public class SpotifyCallbackController {
//
//    @Autowired
//    private TokenInterface tokenService;
//    @Autowired
//    public SpotifySearch Search;
//    @Autowired
//    public SongHandle S_Handler;
//
//    @Autowired
//    private SpotifyUserService spotifyUserService;
//    @Autowired
//    public UserService userService;
//    @Autowired
//    public UserRepository userRepository;
//
//    @Autowired
//    public TokenStore tokenStore;
//
//
//
//    @GetMapping("/callback")
//    public ResponseEntity<UserInfo> handleSpotifyCallback(@RequestParam("code") String authorizationCode,
//                                                          @RequestParam(value = "state", required = false) String state) {
//        try {
//            // Pass the auth code to service
//            Map<String , Object> accessToken = tokenService.getAccessToken(authorizationCode);
//            String AT = (String) accessToken.get("access_token");
//            String RF =(String) accessToken.get("refresh_token");
//            Map<String, Object> userDetails = spotifyUserService.getCurrentUser(AT);
//            String spotifyId = (String) userDetails.get("id");
//            String email = (String) userDetails.get("email");
//            String displayName = (String) userDetails.get("display_name");
//            Map<Long , String> accessTokenStore = new HashMap<>();
//            User user = new User();
//            user.setSpotifyId(spotifyId);
//            user.setEmail(email);
//            user.setUsername(displayName);
//            user.setRefreshToken(RF);
//
//
//            userService.saveOrUpdateUser(user);
//            User dbUser = userRepository.findBySpotifyId(spotifyId);
//            Long userId = dbUser.getId();
//            String username = dbUser.getUsername();
//            String emailId = dbUser.getEmail();
//            tokenStore.put(userId, AT);
//            System.out.println(tokenStore.accessTokenStore);
//            System.out.println("User logged in successfully");
//            UserInfo dto = new UserInfo(userId, username, emailId);
//            System.out.println(dto);
//            return ResponseEntity.ok(dto);
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).build();
//        }
//
//    }
//}
