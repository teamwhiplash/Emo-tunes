package org.tunes.auth;

import java.util.Map;

public interface TokenInterface {
    Map<String,Object> getAccessToken(String AuthCode);

    String refreshAccess(String refresh_token);
}
