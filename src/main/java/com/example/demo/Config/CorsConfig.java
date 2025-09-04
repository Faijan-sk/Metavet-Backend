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

        // Production और Development दोनों के लिए origins
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",   
            "http://localhost:8080", // Local React development
            "http://localhost:4200",           // Local Angular development
            "http://34.61.254.251:3000",  
            "http://34.61.254.251:8080",
            // GCP Frontend (React default port)
            "https://34.61.254.251:3000",     // GCP Frontend HTTPS
            "http://34.61.254.251:*",         // Any port on GCP IP
            "https://34.61.254.251:*",        // Any port on GCP IP HTTPS
            "http://127.0.0.1:*",             // Local development all ports
            "https://*.vercel.app",           // Vercel deployments
            "https://*.netlify.app",          // Netlify deployments
            "https://*.googleplex.com",       // Google internal
            "*"                               // Allow all for development (remove in strict production)
        ));

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials for authentication
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        // Expose important headers for frontend
//        configuration.setExposedHeaders(Arrays.asList(
//            "Access-Control-Allow-Origin",
//            "Access-Control-Allow-Credentials",
//            "Authorization",
//            "Content-Type",
//            "X-Total-Count",
//            "X-Page",
//            "X-Page-Size",
//            "X-Requested-With"
//        ));

        // Apply configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}