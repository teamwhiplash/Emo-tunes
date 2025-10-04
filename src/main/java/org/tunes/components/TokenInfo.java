package org.tunes.components;

import org.springframework.stereotype.Service;

public class TokenInfo {
    private final String accessToken;
    private final long expiresAt;

    public TokenInfo(String accessToken, long expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresAt = System.currentTimeMillis() + (expiresInSeconds * 1000L);
        System.out.println("\n");
        System.out.println("Expiry time:"+ expiresAt);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > (expiresAt - 30_000L);
    }
}
