package org.Tumanyan.config;

import org.Tumanyan.services.DiskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiskConfig {
    private final String NAME = "Tym";
    private final String TOKEN = "AQAAAAAT3cmsAADLW6DgBVJwJ0e_rnH9sVXL5hM";

    @Bean
    public DiskService diskService() {
        return new DiskService(NAME, TOKEN);
    }
}