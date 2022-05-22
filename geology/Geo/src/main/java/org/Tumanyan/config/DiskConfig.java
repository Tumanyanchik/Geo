package org.Tumanyan.config;

import org.Tumanyan.services.DiskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiskConfig {
    @Bean
    public DiskService diskService() {
        return new DiskService();
    }
}
