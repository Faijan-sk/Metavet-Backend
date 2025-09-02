// Updated OtpAuthService.java
package com.example.demo.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;

@Service
public class OtpAuthService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

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

            // 7. Generate JWT tokens using updated service
            String jwtAccessToken;
            String jwtRefreshToken;

            try {
                // Use the new methods for User entity
                jwtAccessToken = jwtService.generateToken(authenticatedUser);
                jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser);
            } catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Token generation failed: " + e.getMessage());
                return response;
            }

            // 8. Prepare success response - USER DATA
            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("phoneNumber", user.getPhoneNumber());
            userData.put("countryCode", user.getCountryCode());
            userData.put("email", user.getEmail());
            userData.put("userType", user.getUserType());
            userData.put("userId", user.getUid());
            
            // 9. Response structure matching your controller expectation
            response.put("status", "success");
            response.put("phoneNumber", phoneNumber);
            response.put("userData", userData);
            response.put("accessToken", jwtAccessToken);
            response.put("refreshToken", jwtRefreshToken);

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
}