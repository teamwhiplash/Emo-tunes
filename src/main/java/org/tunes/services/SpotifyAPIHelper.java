package org.tunes.services;


import org.springframework.stereotype.Service;
import org.tunes.auth.SpotifyTokenService;
import org.tunes.components.TokenInfo;
import org.tunes.components.TokenStore;
import org.tunes.models.User;
import org.tunes.repositories.UserRepository;

@Service
public class SpotifyAPIHelper {
    private final TokenStore tokenStore;
    private final UserRepository userRepository;
    private final SpotifyTokenService spotifyTokenService;

    public SpotifyAPIHelper(TokenStore tokenStore,
                            UserRepository userRepository,
                            SpotifyTokenService spotifyTokenService) {
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
        this.spotifyTokenService = spotifyTokenService;
    }

    public String getValidAccessToken(Long userId) {
        String cached = tokenStore.getValidAccessToken(userId);
        if (cached != null) {
            return cached;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        String refreshed = spotifyTokenService.refreshAccess(user.getRefreshToken());
        if (refreshed == null) {
            throw new IllegalStateException("Unable to refresh Spotify token for user " + userId);
        }

        // Spotify normally returns a 1‑hour TTL; we use the same value here.
        TokenInfo refreshedInfo = new TokenInfo(refreshed, 3600L);
        tokenStore.putInto(userId, refreshedInfo);
        return refreshed;
    }
}

