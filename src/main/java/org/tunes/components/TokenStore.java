package org.tunes.components;

<<<<<<< HEAD
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class TokenStore {
    public Map<Long, String> accessTokenStore = new ConcurrentHashMap<>();
    public void put(Long userId, String accessToken) {
        accessTokenStore.put(userId, accessToken);
    }
    public String getAccessToken(Long userId){
        return accessTokenStore.get(userId);
    }

    public Map<Long, String> getAll() {
        return accessTokenStore;
    }
}
=======
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
>>>>>>> upstream/main
