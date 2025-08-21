package com.example.demo.Config;
import java.io.IOException;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.demo.Exceptions.BadRequest;
import com.example.demo.Service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        
        System.out.println("Request URI: " + requestURI);
        System.out.println("Auth Header: " + authHeader);
        
        // Skip authentication for public endpoints
        if (requestURI.contains("/auth") || requestURI.contains("/pub")) {
            System.out.println("Skipping authentication for public endpoint");
            filterChain.doFilter(request, response);
            return;
        }
        
        // Check if Authorization header is present and valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            BadRequest apiResponse = new BadRequest(401, "Missing or invalid authorization header", null);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            return; // Don't continue the filter chain
        }
        
        try {
            final String jwt = authHeader.substring(7);
            
            // Clean quotes if present - Fix for base64url error
            String cleanJwt = jwt.replace("\"", "").trim();
            System.out.println("Original JWT: " + jwt);
            System.out.println("Cleaned JWT: " + cleanJwt);
            
            final String userEmail = jwtService.extractUsername(cleanJwt, true);
            
            System.out.println("Extracted email from token: " + userEmail);
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                if (jwtService.isTokenValid(cleanJwt, userDetails, true)) {
                    System.out.println("Token is valid, setting authentication");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("Token validation failed");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    BadRequest apiResponse = new BadRequest(401, "Invalid token", null);
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                    return;
                }
            }
            
            System.out.println("Proceeding to controller...");
            filterChain.doFilter(request, response);
            
        } catch (io.jsonwebtoken.ExpiredJwtException expiredJwtException) {
            System.out.println("JWT token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            BadRequest apiResponse = new BadRequest(401, "JWT token has expired", null);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (Exception exception) {
            System.out.println("JWT validation error: " + exception.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            BadRequest apiResponse = new BadRequest(401, "Authentication failed: " + exception.getMessage(), null);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }
    }
}