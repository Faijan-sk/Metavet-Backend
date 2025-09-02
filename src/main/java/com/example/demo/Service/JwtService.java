package com.example.demo.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.AdminsEntity;
import com.example.demo.Entities.UsersEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String accessSecretKey;

    @Value("${security.jwt.refresh-secret-key}")
    private String refreshSecretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh.expiration-time}")
    private long refreshTokenValidity;

    // Extract username from token
    public String extractUsername(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getSubject, isAccessToken);
    }

    // Extract user type from token (USER/ADMIN)
    public String extractUserType(String token, boolean isAccessToken) {
        return extractClaim(token, claims -> claims.get("userType", String.class), isAccessToken);
    }

    // Extract user ID from token
    public Long extractUserId(String token, boolean isAccessToken) {
        return extractClaim(token, claims -> claims.get("userId", Long.class), isAccessToken);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isAccessToken) {
        final Claims claims = extractAllClaims(token, isAccessToken);
        return claimsResolver.apply(claims);
    }

    // Generate token for UserDetails (existing method)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration, true);
    }

    // NEW: Generate token for Admin Entity
    public String generateToken(AdminsEntity admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getId());
        claims.put("userType", "ADMIN");
        claims.put("role", admin.getRole());
        claims.put("roleName", admin.getRoleName());
        claims.put("fullName", admin.getFullName());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(admin.getEmail()) // Use email as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(true), SignatureAlgorithm.HS256)
                .compact();
    }

    // NEW: Generate token for User Entity  
    public String generateToken(UsersEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUid());
        claims.put("userType", "USER");
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // Use email as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(true), SignatureAlgorithm.HS256)
                .compact();
    }

    // NEW: Generate refresh token for Admin
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

    // NEW: Generate refresh token for User
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

    // Existing methods
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails, refreshTokenValidity, false);
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSignInKey(false), SignatureAlgorithm.HS256)
                .compact();
    }

    // NEW: Validate token for any entity type
    public boolean isTokenValid(String token, boolean isAccessToken) {
        try {
            return !isTokenExpired(token, isAccessToken);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails, boolean isAccessToken) {
        final String username = extractUsername(token, isAccessToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, isAccessToken);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            return !isTokenExpired(refreshToken, false);
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, boolean isAccessToken) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(isAccessToken), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token, boolean isAccessToken) {
        return extractExpiration(token, isAccessToken).before(new Date());
    }

    private Date extractExpiration(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getExpiration, isAccessToken);
    }

    private Claims extractAllClaims(String token, boolean isAccessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey(isAccessToken))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(boolean isAccessToken) {
        try {
            String secretKey = isAccessToken ? accessSecretKey : refreshSecretKey;
            
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