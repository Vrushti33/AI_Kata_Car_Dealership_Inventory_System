package com.cardealership.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        
        // Read allowed origins from environment variable in production, fallback to wildcard patterns for dev
        String allowedOriginsEnv = System.getProperty("CORS_ALLOWED_ORIGINS");
        if (allowedOriginsEnv == null) {
            allowedOriginsEnv = System.getenv("CORS_ALLOWED_ORIGINS");
        }
        
        if (allowedOriginsEnv != null && !allowedOriginsEnv.trim().isEmpty()) {
            config.setAllowedOrigins(Arrays.asList(allowedOriginsEnv.split(",")));
        } else {
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
        }
        
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Cookie"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
