package com.example.demo.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class OtpAuthService {

    
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
	private JwtService jwtService;

    // Fixed for JJWT 0.12.6 - Generate SecretKey properly
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public Map<String, Object> verifyOtpWithToken(String token, String otp) {
    	
    	

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Base64 decode the token
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String decodedString = new String(decodedBytes);

            // 2. Parse token format: "phoneNumber:timestamp"
            String[] parts = decodedString.split(":");
            if (parts.length != 2) {
                response.put("status", "failed");
                response.put("message", "Invalid token format");
                return response;
            }

            String phoneNumber = parts[0];
            long timestamp = Long.parseLong(parts[1]);

            // 3. Check token expiry (5 minutes validity)
            long currentTime = System.currentTimeMillis();
            if (currentTime > (timestamp + 5 * 60 * 1000)) {
                response.put("status", "failed");
                response.put("message", "Token expired. Please request a new OTP.");
                return response;
            }

            // 4. Fetch user from database
            UsersEntity user = userRepository.findByPhoneNumber(phoneNumber);

            if (user == null) {
                response.put("status", "failed");
                response.put("message", "User not found");
                return response;
            }

            // 5. Verify stored OTP using PasswordEncoder.matches()
            if (user.getOtp() == null) {
                response.put("status", "failed");
                response.put("message", "No OTP found for this user");
                return response;
            }

            boolean isOtpValid = passwordEncoder.matches(otp, user.getOtp());

            if (!isOtpValid) {
                response.put("status", "failed");
                response.put("message", "Invalid OTP");
                return response;
            }

            // 6. Mark user as verified and save changes
            UsersEntity authenticatedUser = userRepository.save(user);

           
         // Generate JWT tokens
            String jwtAccessToken;
            String jwtRefreshToken;

            try {
                jwtAccessToken = jwtService.generateToken(authenticatedUser);
                // FIXED: Pass UserDetails object instead of phone number
                jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser);
                // या फिर email pass करें: 
                // jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser.getEmail());
            } catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Token generation failed: " + e.getMessage());
                return response;
            }

            // 7. Prepare success response
            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("phoneNumber", user.getPhoneNumber());
            userData.put("countryCode", user.getCountryCode());
            userData.put("email", user.getEmail());
            userData.put("userType", user.getUserType());          userData.put("accessToken", jwtAccessToken);
            userData.put("refreshToken", jwtRefreshToken);

            response.put("status", "success");
            response.put("phoneNumber", phoneNumber);
            response.put("userData", userData);

        } catch (NumberFormatException e) {
            response.put("status", "failed");
            response.put("message", "Invalid token timestamp format");
        } catch (IllegalArgumentException e) {
            response.put("status", "failed");
            response.put("message", "Invalid token encoding");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Verification failed: " + e.getMessage());
        }

        return response;
    }

    // Additional utility methods for JWT handling

    /**
     * Extract claims from JWT token
     */
   

    
}
