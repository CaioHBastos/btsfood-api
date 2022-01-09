package br.com.btstech.btsfoodapi.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.MediaType;

@Configuration
public class BtsHalConfiguration {

    @Bean
    public HalConfiguration globalPolicy() {
        return new HalConfiguration()
                .withMediaType(MediaType.APPLICATION_JSON)
                .withMediaType(BtsMediaTypes.V1_APPLICATION_JSON);
    }
}
