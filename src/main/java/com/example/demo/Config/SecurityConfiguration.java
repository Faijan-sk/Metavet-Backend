package com.example.demo.Config;

import java.util.List;

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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsServiceImpl userDetailsService,
            CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            // CSRF disable kar diya hai
//            .csrf(csrf -> csrf.disable())
//            
//            // CORS configuration - separate CorsConfig class se inject kar rahe hain
//            .cors(cors -> cors.configurationSource(corsConfigurationSource))
//            
//            // HTTP requests authorization - Updated for proper endpoint security
//            .authorizeHttpRequests(requests -> requests
//                // Public endpoints - no authentication required (only /api/auth/*)
//                .requestMatchers("/api/auth/**", "/pub/**", "/health", "/error", "/actuator/health").permitAll()
//                
//                // OPTIONS requests ko allow karna hai CORS preflight ke liye
//                .requestMatchers("OPTIONS", "/**").permitAll()
//                
//                // Admin endpoints - require ADMIN role (ab /auth/admin/* protected hai)
//                .requestMatchers("/api/admin/**", "/auth/admin/**").hasRole("ADMIN")
//                
//                // User endpoints - require USER role
//                .requestMatchers("/api/user/**").hasRole("USER")
//                
//                // Common endpoints - accessible by both ADMIN and USER
//                .requestMatchers("/api/common/**").hasAnyRole("ADMIN", "USER")
//                
//                // Any other request requires authentication
//                .anyRequest().authenticated())
//            
//            // Session management - stateless
//            .sessionManagement(management -> 
//                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            
//            // Authentication provider
//            .authenticationProvider(authenticationProvider())
//            
//            // JWT filter - Updated filter that handles both Admin and User
//            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
 
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type","X-IP","X-Location"));
		configuration.setAllowCredentials(true);
 
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
 
		return source;
 
	}
}