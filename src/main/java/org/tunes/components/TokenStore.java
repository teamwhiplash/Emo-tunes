package org.tunes.components;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TokenStore {
    private final Map<Long, TokenInfo> accessTokenStore = new ConcurrentHashMap<>();

    public void putInto(Long userId, TokenInfo tokenInfo) {
        accessTokenStore.put(userId, tokenInfo);
    }

    public String getValidAccessToken(Long userId) {
        TokenInfo info = accessTokenStore.get(userId);
        return (info != null && !info.isExpired()) ? info.getAccessToken() : null;
    }

    public Map<Long, TokenInfo> getAll() {
        return accessTokenStore;
    }
}

