package com.example.demo.Config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.example.demo.Entities.AdminsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Exceptions.BadRequest;
import com.example.demo.Repository.AdminRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepo adminRepository;

    @Autowired
    private UserRepo userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.print("############################################## ");

        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        System.out.println("===== JWT Filter Debug =====");
        System.out.println("Request URI: " + requestURI);
        System.out.println("Request Method: " + method);

        // Skip authentication for public endpoints and OPTIONS preflight
        if (shouldSkipAuthentication(requestURI, method)) {
            System.out.println("✅ PUBLIC ENDPOINT - Skipping authentication for: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // Now check for Authorization header (only for protected endpoints)
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header: " + (authHeader != null ? "Present" : "Missing"));
        System.out.println("============================");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header for protected endpoint");
            handleAuthenticationError(response, request, 401, "Missing or invalid authorization header");
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            String cleanJwt = jwt.replace("\"", "").trim();
            System.out.println("🔍 Processing JWT token...");

            final String userEmail = jwtService.extractUsername(cleanJwt, true);
            final String userType = jwtService.extractUserType(cleanJwt, true);
            final Long userId = jwtService.extractUserId(cleanJwt, true);

            System.out.println("Extracted - Email: " + userEmail + ", Type: " + userType + ", ID: " + userId);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // Validate token first
                if (!jwtService.isTokenValid(cleanJwt, true)) {
                    System.out.println("❌ Token validation failed");
                    handleAuthenticationError(response, request, 401, "Invalid or expired token");
                    return;
                }

                CustomUserDetails userDetails = null;

                if ("ADMIN".equals(userType)) {
                    Optional<AdminsEntity> adminOpt = adminRepository.findByEmail(userEmail);
                    if (adminOpt.isPresent()) {
                        AdminsEntity admin = adminOpt.get();
                        userDetails = new CustomUserDetails(admin.getEmail(), "ADMIN", cleanJwt, admin.getId(), admin.getRole());
                        System.out.println("✅ Admin authentication successful");
                    } else {
                        System.out.println("❌ Admin not found in database");
                        handleAuthenticationError(response, request, 401, "Admin not found");
                        return;
                    }
                } else if ("USER".equals(userType)) {
                    UsersEntity user = userRepository.findByEmail(userEmail);
                    if (user != null) {
                        userDetails = new CustomUserDetails(user.getEmail(), "USER", cleanJwt, user.getUid(), user.getUserType());
                        System.out.println("✅ User authentication successful");
                    } else {
                        System.out.println("❌ User not found in database");
                        handleAuthenticationError(response, request, 401, "User not found");
                        return;
                    }
                } else {
                    System.out.println("❌ Unknown user type: " + userType);
                    handleAuthenticationError(response, request, 401, "Invalid user type");
                    return;
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Set request attributes for controllers
                    request.setAttribute("userType", userType);
                    request.setAttribute("userEmail", userEmail);
                    request.setAttribute("userId", userId);

                    System.out.println("✅ Authentication context set successfully");
                }
            }

            System.out.println("➡️ Proceeding to controller...");
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException expiredJwtException) {
            System.out.println("❌ JWT token expired");
            handleAuthenticationError(response, request, 401, "JWT token has expired");
        } catch (Exception exception) {
            System.out.println("❌ JWT validation error: " + exception.getMessage());
            exception.printStackTrace();
            handleAuthenticationError(response, request, 401, "Authentication failed: " + exception.getMessage());
        }
    }

    /**
     * Skip authentication for public endpoints
     */
    private boolean shouldSkipAuthentication(String requestURI, String method) {
        // Always allow OPTIONS requests for CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // Public endpoints - these don't need authentication
        String[] publicPrefixes = {
            "/api/auth/",
            "/pub/",
            "/health",
            "/error",
            "/actuator/health"
        };

        for (String path : publicPrefixes) {
            if (requestURI.startsWith(path)) {
                System.out.println("✅ Matched public prefix: " + path);
                return true;
            }
        }

        boolean isBasicEndpoint = "/".equals(requestURI) ||
                                  "/favicon.ico".equals(requestURI) ||
                                  requestURI.startsWith("/static/");

        if (isBasicEndpoint) {
            System.out.println("✅ Matched basic endpoint");
        }

        return isBasicEndpoint;
    }

    /**
     * Handle authentication errors with proper CORS headers (echo Origin)
     */
    private void handleAuthenticationError(HttpServletResponse response, HttpServletRequest request, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Echo back the Origin header to satisfy credentialed requests
        String origin = request.getHeader("Origin");
        if (origin == null || origin.isEmpty()) {
            // fallback for dev; in prod better to always have explicit allowedOrigins
            origin = "*";
        }

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");

        BadRequest apiResponse = new BadRequest(status, message, null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}

// CustomUserDetails class (unchanged)
class CustomUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final String email;
    private final String userType;
    private final String token;
    private final Long userId;
    private final Integer role;

    public CustomUserDetails(String email, String userType, String token, Long userId, Integer role) {
        this.email = email;
        this.userType = userType;
        this.token = token;
        this.userId = userId;
        this.role = role;
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return null; }

    public String getUserType() { return userType; }

    public String getToken() { return token; }

    public Long getUserId() { return userId; }

    public Integer getRole() { return role; }

    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
                new java.util.ArrayList<>();

        if ("ADMIN".equals(userType)) {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
            if (role != null) {
                authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN_ROLE_" + role));
            }
        } else {
            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
            if (role != null) {
                authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("USER_TYPE_" + role));
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
