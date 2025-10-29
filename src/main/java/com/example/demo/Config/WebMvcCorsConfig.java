package com.example.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web MVC CORS configuration as a fallback to ensure
 * Spring MVC will respond to preflight requests with proper headers.
 *
 * This is safe to keep even if you already have a CorsFilter registered.
 */
@Configuration
public class WebMvcCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://35.206.66.49",
                "http://35.206.66.49:8080",
                "http://35.206.66.49:8282",
                "https://34.61.254.251"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
            .allowedHeaders("*")
            .allowCredentials(true)
            .exposedHeaders("Authorization", "Content-Type")
            .maxAge(3600);
    }
}
