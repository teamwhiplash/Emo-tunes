package org.tunes.components;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TokenStore {
    private final Map<Integer, TokenInfo> accessTokenStore = new ConcurrentHashMap<>();

    public void putInto(int userId, TokenInfo tokenInfo) {
        accessTokenStore.put(userId, tokenInfo);
    }

    public String getValidAccessToken(int userId) {
        TokenInfo info = accessTokenStore.get(userId);
        return (info != null && !info.isExpired()) ? info.getAccessToken() : null;
    }

    public Map<Integer, TokenInfo> getAll() {
        return accessTokenStore;
    }
}
