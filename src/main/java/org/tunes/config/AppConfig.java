
package org.tunes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tunes.components.TokenStore;

@Configuration
public class AppConfig {

    @Bean
    public TokenStore tokenStore() {
        return new TokenStore();
    }
}
