package org.tunes.auth;

import org.springframework.stereotype.Service;
import org.tunes.config.Credentials;

@Service
public class SpotifyCred {
    private final Credentials credentials;

    public SpotifyCred(Credentials credentials){
        this.credentials=credentials;
    }

    public String giveClient_ID(){
        return credentials.getCLIENT_ID();
    }
    public String giveClient_Secret(){
        return credentials.getCLIENT_SECRET();
    }

    public String getRedirect(){
        return credentials.getREDIRECT();
    }

}



