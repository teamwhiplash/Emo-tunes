package org.tunes.auth;

public interface TokenInterface {
    String getAccessToken(String AuthCode);

    String getRefreshToken(String Authcode);

    String refreshAccess(String refresh_token);
}
