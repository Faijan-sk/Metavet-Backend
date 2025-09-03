package com.example.demo.Service;

import com.example.demo.Entities.AdminsEntity;
import com.example.demo.Entities.UsersEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Properties ko application.properties ya application.yml se load kiya gaya hai
    @Value("${security.jwt.secret-key}")
    private String accessSecretKey;

    @Value("${security.jwt.refresh-secret-key}")
    private String refreshSecretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh.expiration-time}")
    private long refreshTokenValidity;

    // --- Token Generation Methods ---

    // Generic token builder
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, boolean isAccessToken) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(isAccessToken), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generates a new access token for a User entity
    public String generateToken(UsersEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUid());
        claims.put("userType", "USER");
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(true), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generates a new access token for an Admin entity
    public String generateToken(AdminsEntity admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getId());
        claims.put("userType", "ADMIN");
        claims.put("role", admin.getRole());
        claims.put("roleName", admin.getRoleName());
        claims.put("fullName", admin.getFullName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(admin.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(true), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generates a new refresh token for a User entity
    public String generateRefreshToken(UsersEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUid());
        claims.put("userType", "USER");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSignInKey(false), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generates a new refresh token for an Admin entity
    public String generateRefreshToken(AdminsEntity admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getId());
        claims.put("userType", "ADMIN");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(admin.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSignInKey(false), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- Token Extraction & Validation Methods ---

    // Extracts a single claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isAccessToken) {
        final Claims claims = extractAllClaims(token, isAccessToken);
        return claimsResolver.apply(claims);
    }

    // Extracts all claims (the payload) from the token
    private Claims extractAllClaims(String token, boolean isAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey(isAccessToken))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extracts username (subject) from token
    public String extractUsername(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getSubject, isAccessToken);
    }

    // Extracts user type (USER/ADMIN) from token
    public String extractUserType(String token, boolean isAccessToken) {
        return extractClaim(token, claims -> claims.get("userType", String.class), isAccessToken);
    }

    // Extracts user ID from token
    public Long extractUserId(String token, boolean isAccessToken) {
        return extractClaim(token, claims -> claims.get("userId", Long.class), isAccessToken);
    }

    // Checks if a token is valid
    public boolean isTokenValid(String token, boolean isAccessToken) {
        try {
            return !isTokenExpired(token, isAccessToken);
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Added helper methods
    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, true);
    }

    //missing method 
    public boolean isRefreshTokenValid(String token) {
        return isTokenValid(token, false);
    }

    // Checks if a token is expired
    private boolean isTokenExpired(String token, boolean isAccessToken) {
        return extractExpiration(token, isAccessToken).before(new Date());
    }

    // Extracts expiration date from token
    private Date extractExpiration(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getExpiration, isAccessToken);
    }

    // Returns the correct secret key for signing/verification (access or refresh)
    private Key getSignInKey(boolean isAccessToken) {
        try {
            String secretKey = isAccessToken ? accessSecretKey : refreshSecretKey;

            // JWT libraries often use URL-safe Base64, which we need to convert to standard Base64
            String standardBase64 = secretKey
                    .replace('-', '+')
                    .replace('_', '/');

            while (standardBase64.length() % 4 != 0) {
                standardBase64 += '=';
            }

            byte[] keyBytes = Decoders.BASE64.decode(standardBase64);
            return Keys.hmacShaKeyFor(keyBytes);

        } catch (Exception e) {
            throw new RuntimeException("Invalid secret key format: " + e.getMessage());
        }
    }
}
