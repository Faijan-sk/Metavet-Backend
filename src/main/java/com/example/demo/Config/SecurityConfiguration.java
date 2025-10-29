package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.Service.UserDetailsServiceImpl;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * ✅ CRITICAL: CORS Configuration Bean
     * This was MISSING in your original code - that's why preflight requests were failing
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // ✅ Allow your frontend origins - ADD ALL YOUR FRONTEND URLs HERE
        configuration.setAllowedOrigins(Arrays.asList(
            "http://35.206.66.49:8282",      // Your GCP frontend (from error log)
            "http://34.61.254.251",          // From your properties file
            "http://localhost:3000",         // Local development
            "http://localhost:4200",         // Local development (Angular)
            "http://localhost:8282",         // Local development
            "http://127.0.0.1:3000",         // Local development
            "http://127.0.0.1:4200",         // Local development
            "http://127.0.0.1:8282"          // Local development
        ));
        
        // ✅ Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // ✅ Allow all headers (including Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // ✅ IMPORTANT: Allow credentials (required for Authorization headers and cookies)
        configuration.setAllowCredentials(true);
        
        // ✅ Expose headers to frontend JavaScript
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type",
            "X-Total-Count",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // ✅ Cache preflight response for 1 hour (3600 seconds)
        configuration.setMaxAge(3600L);
        
        // ✅ Register configuration for all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        System.out.println("✅ CORS Configuration initialized with origins: " + configuration.getAllowedOrigins());
        
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ CRITICAL FIX: CORS must be FIRST, before CSRF
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // ✅ Disable CSRF for stateless REST API
            .csrf(csrf -> csrf.disable())
            
            // ✅ HTTP requests authorization - Updated for proper endpoint security
            .authorizeHttpRequests(requests -> requests
                // ✅ FIXED: Public endpoints - no authentication required
                .requestMatchers("/api/auth/**").permitAll()
                
                // ✅ FIXED: Allow all OPTIONS requests for CORS preflight
                .requestMatchers("OPTIONS", "/**").permitAll()
                
                // ✅ Health check endpoints
                .requestMatchers("/health", "/actuator/health", "/actuator/info").permitAll()
                
                // ✅ Error endpoints
                .requestMatchers("/error").permitAll()
                
                // Admin endpoints - require ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // User endpoints - require USER role
                .requestMatchers("/api/user/**").hasRole("USER")
                
                // Common endpoints - accessible by both ADMIN and USER
                .requestMatchers("/api/common/**").hasAnyRole("ADMIN", "USER")
                
                // Any other request requires authentication
                .anyRequest().authenticated())
            
            // ✅ Session management - stateless (JWT-based)
            .sessionManagement(management -> 
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // ✅ Authentication provider
            .authenticationProvider(authenticationProvider())
            
            // ✅ JWT filter - Updated filter that handles both Admin and User
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ Security Filter Chain initialized successfully");
        
        return http.build();
    }
}