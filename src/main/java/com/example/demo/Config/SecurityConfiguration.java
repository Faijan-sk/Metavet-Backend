package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.Service.UserDetailsServiceImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsServiceImpl userDetailsService,
            CorsConfigurationSource corsConfigurationSource) {
        System.out.print("%%%%%%%%%%%%5");
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.print("%%%%%%%%%%%%%4");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        System.out.print("%%%%%%%%%%%%%%%3");
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%2");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%1");
        http
            // Enable CORS (use our CorsConfig bean)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // Disable CSRF for APIs
            .csrf(csrf -> csrf.disable())
            // Authorization rules
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/admin/**", "/auth/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/common/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
            )
            // Stateless session
            .sessionManagement(management ->
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Authentication provider
            .authenticationProvider(authenticationProvider())
            // JWT filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
