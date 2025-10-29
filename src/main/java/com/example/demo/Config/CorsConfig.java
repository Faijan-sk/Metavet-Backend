package com.example.demo.Config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ✅ GCP deployment ke liye specific origins (port 8080 remove kiya)
        // ✅ FIXED: Using setAllowedOriginPatterns instead of setAllowedOrigins for better compatibility
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",        // Local React dev
            "http://localhost:5173",        // Local Angular dev
            "http://35.206.66.49",          // GCP frontend (port 80) - MAIN URL
            "http://35.206.66.49:8080",
            "http://35.206.66.49:8282",     // GCP frontend (port 8282)
            "http://34.61.254.251:8282",
            "http://34.61.254.251",
            "http://34.61.254.251:8080",    // GCP frontend HTTPS (port 443)
            "https://34.61.254.251:3000"    // GCP frontend HTTPS (port 3000)
        ));
        
        // ✅ All HTTP methods allowed
        configuration.setAllowedMethods(Arrays.asList(
            "GET", 
            "POST", 
            "PUT", 
            "DELETE", 
            "OPTIONS", 
            "PATCH", 
            "HEAD"
        ));
        
        // ✅ All headers allowed
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // ✅ Allow credentials (for JWT tokens in headers)
        configuration.setAllowCredentials(true);
        
        // ✅ Preflight cache for 1 hour
        configuration.setMaxAge(3600L);
        
        // ✅ Expose Authorization header to frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Apply to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}