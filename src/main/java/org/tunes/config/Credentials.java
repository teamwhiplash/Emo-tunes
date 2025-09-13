package org.tunes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix ="spotify")
public class Credentials {
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT;

    public String getCLIENT_ID(){
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID){
        this.CLIENT_ID=CLIENT_ID;
    }
    public String getCLIENT_SECRET(){
        return CLIENT_SECRET;
    }

    public void setCLIENT_SECRET(String CLIENT_SECRET){
        this.CLIENT_SECRET= CLIENT_SECRET;
    }

    public String getREDIRECT(){
        return REDIRECT;
    }
    public void setREDIRECT(String REDIRECT){
        this.REDIRECT=REDIRECT;
    }

}
