package com.example.demo.Config;

import java.io.IOException;
import java.util.Optional;

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

    @Autowired
    private AdminRepo adminRepository;

    @Autowired
    private UserRepo userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // ✅ Debug logging (production में हटा सकते हैं)
        System.out.println("===== JWT Filter Debug =====");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Auth Header: " + (request.getHeader("Authorization") != null ? "Present" : "Missing"));
        System.out.println("============================");

        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        // ✅ Skip authentication for public endpoints and OPTIONS requests
        if (shouldSkipAuthentication(requestURI, method)) {
            System.out.println("✅ PUBLIC ENDPOINT - Authentication skipped for: " + requestURI + " [" + method + "]");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Protected endpoints - JWT token required
        System.out.println("🔒 PROTECTED ENDPOINT - JWT validation required for: " + requestURI);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header");
            handleAuthenticationError(response, 401, "Missing or invalid authorization header");
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
                    handleAuthenticationError(response, 401, "Invalid or expired token");
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
                        handleAuthenticationError(response, 401, "Admin not found");
                        return;
                    }
                } else if ("USER".equals(userType)) {
                    UsersEntity user = userRepository.findByEmail(userEmail);
                    if (user != null) {
                        userDetails = new CustomUserDetails(user.getEmail(), "USER", cleanJwt, user.getUid(), user.getUserType());
                        System.out.println("✅ User authentication successful");
                    } else {
                        System.out.println("❌ User not found in database");
                        handleAuthenticationError(response, 401, "User not found");
                        return;
                    }
                } else {
                    System.out.println("❌ Unknown user type: " + userType);
                    handleAuthenticationError(response, 401, "Invalid user type");
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
            handleAuthenticationError(response, 401, "JWT token has expired");
        } catch (Exception exception) {
            System.out.println("❌ JWT validation error: " + exception.getMessage());
            exception.printStackTrace();
            handleAuthenticationError(response, 401, "Authentication failed: " + exception.getMessage());
        }
    }

    /**
     * ✅ Skip authentication for public endpoints
     */
    private boolean shouldSkipAuthentication(String requestURI, String method) {
        // Always allow OPTIONS requests for CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // ✅ Public endpoints - these don't need authentication
        String[] publicPrefixes = {
            "/api/auth/",          // All auth endpoints (login, register, etc.)
            "/pub/",               // Public static content
            "/health",             // Health check
            "/error",              // Error pages
            "/actuator/health"     // Actuator health endpoint
        };

        for (String path : publicPrefixes) {
            if (requestURI.startsWith(path)) {
                return true;
            }
        }

        // Basic system endpoints
        return "/".equals(requestURI) || 
               "/favicon.ico".equals(requestURI) ||
               requestURI.startsWith("/static/");
    }

    /**
     * ✅ Handle authentication errors with proper CORS headers
     */
    private void handleAuthenticationError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // ✅ Add CORS headers for error responses
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        BadRequest apiResponse = new BadRequest(status, message, null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}

// ✅ Custom UserDetails implementation
class CustomUserDetails implements UserDetails {
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