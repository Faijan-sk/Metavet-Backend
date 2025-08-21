package com.example.demo.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
 
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
 
    public String extractUsername(String token, boolean isAccessToken) {
        return extractClaim(token, Claims::getSubject, isAccessToken);
    }
 
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isAccessToken) {
        final Claims claims = extractAllClaims(token, isAccessToken);
        return claimsResolver.apply(claims);
    }
 
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
 
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration, true);
    }
 
    public long getExpirationTime() {
        return jwtExpiration;
    }
 
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, boolean isAccessToken) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Always use email as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(isAccessToken), SignatureAlgorithm.HS256)
                .compact();
    }
 
    // FIXED: Use email instead of phone number for refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, userDetails, refreshTokenValidity, false);
    }
    
    // Alternative method if you want to pass email directly
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Use email consistently
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSignInKey(false), SignatureAlgorithm.HS256)
                .compact();
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
            
            // Fix base64url to standard base64 if needed
            String standardBase64 = secretKey
                    .replace('-', '+')
                    .replace('_', '/');
            
            // Add padding if needed
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