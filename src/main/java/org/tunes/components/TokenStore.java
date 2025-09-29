package org.tunes.components;

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
