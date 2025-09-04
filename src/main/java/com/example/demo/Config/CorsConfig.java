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

        /**
         * ❌ पहले: तुमने `configuration.setAllowedOriginPatterns(Arrays.asList("*"))` 
         * और साथ में `setAllowCredentials(true)` किया था।
         * 👉 Problem: Spring `*` (wildcard) को credentials=true के साथ allow नहीं करता।
         * 👉 Result: Browser CORS error देता है।
         *
         * ✅ अब: specific origins allow किए हैं (local + GCP deploy + common hosting जैसे vercel/netlify)
         */
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",          // Local React dev
            "http://localhost:4200",          // Local Angular dev
            "http://34.61.254.251:3000",      // GCP frontend (React)
            "http://34.61.254.251:8080",      // GCP backend
            "https://34.61.254.251:3000",     // GCP frontend HTTPS
            "https://*.vercel.app",           // Vercel deployments
            "https://*.netlify.app"           // Netlify deployments
        ));

        // ✅ Allowed methods same रखे
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // ✅ Allowed headers same रखे
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // ✅ Credentials true रखा (cookies/auth headers bhejne ke liye)
        configuration.setAllowCredentials(true);

        // ✅ Preflight cache (OPTIONS request) 1 hour ke liye
        configuration.setMaxAge(3600L);

        /**
         * ❌ पहले: तुमने "*, http://127.0.0.1:* , http://34.61.254.251:*" जैसी entries डाली थी।
         * 👉 Problem: ये invalid हैं क्योंकि setAllowedOrigins में wildcard port/origin accept नहीं होता।
         * ✅ अब: साफ कर दिया, सिर्फ valid origins allow किए।
         */

        // Apply configuration to all API paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
