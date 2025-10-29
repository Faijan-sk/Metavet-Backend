package com.example.demo.Config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        System.out.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ lavde chal na ");
        // ✅ GCP deployment ke liye specific origins (avoid "*" when allowCredentials = true)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:5173",
            "http://35.206.66.49",
            "http://35.206.66.49:8080",
            "https://34.61.254.251",
            "https://34.61.254.251:3000",
            "http://35.206.66.49:8282"
        ));

        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // Allow headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Allow credentials for Authorization header
        configuration.setAllowCredentials(true);

        // Preflight cache
        configuration.setMaxAge(3600L);

        // Expose Authorization header to frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Ensure CorsFilter runs with highest precedence so headers are set before other filters.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsConfigurationSource source) {
        CorsFilter filter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
