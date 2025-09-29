package org.tunes.config;

import org.springframework.stereotype.Service;

@Service
public class Credentials {
    private final String CLIENT_ID = System.getenv("CLIENT_ID");
    private final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
    private final String REDIRECT = System.getenv("REDIRECT_URI");

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }


    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }


    public String getREDIRECT() {
        return REDIRECT;


    }
}
