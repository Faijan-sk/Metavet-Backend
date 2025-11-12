package com.example.demo.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    /**
     * Verify OTP using the temporary Base64 token (format: phoneNumber:timestamp)
     * - Token validity: 5 minutes
     * - Compares encoded OTP stored in DB with provided OTP using PasswordEncoder.matches()
     * - On success: clears OTP from DB, marks profile completed (if applicable),
     *   generates JWT access & refresh tokens and returns user data + tokens.
     *
     * Returns a Map with keys:
     * - status: "success" / "failed" / "error"
     * - message: human readable message
     * - userData: (on success) map with user fields
     * - accessToken / refreshToken: (on success) JWT tokens
     */
    public Map<String, Object> verifyOtpWithToken(String token, String otp) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            if (token == null || token.trim().isEmpty()) {
                response.put("status", "failed");
                response.put("message", "Token is required");
                return response;
            }

            if (otp == null || !otp.matches("^[0-9]{4}$")) {
                response.put("status", "failed");
                response.put("message", "OTP must be 4 digits");
                return response;
            }

            // 1. Base64 decode the token
            byte[] decodedBytes;
            try {
                decodedBytes = Base64.getDecoder().decode(token);
            } catch (IllegalArgumentException e) {
                response.put("status", "failed");
                response.put("message", "Invalid token encoding");
                return response;
            }

            String decodedString = new String(decodedBytes);

            // 2. Parse token format: "phoneNumber:timestamp"
            String[] parts = decodedString.split(":");
            if (parts.length != 2) {
                response.put("status", "failed");
                response.put("message", "Invalid token format");
                return response;
            }

            String phoneNumber = parts[0];
            long timestamp;
            try {
                timestamp = Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                response.put("status", "failed");
                response.put("message", "Invalid token timestamp format");
                return response;
            }

            // 3. Check token expiry (5 minutes validity)
            long currentTime = System.currentTimeMillis();
            if (currentTime > (timestamp + 5 * 60 * 1000)) {
                response.put("status", "failed");
                response.put("message", "Token expired. Please request a new OTP.");
                return response;
            }

            // 4. Fetch user from database (repository returns Optional)
            Optional<UsersEntity> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
            if (optionalUser.isEmpty()) {
                response.put("status", "failed");
                response.put("message", "User not found");
                return response;
            }
            UsersEntity user = optionalUser.get();

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

            // 6. OTP is valid -> clear OTP and mark profile completed (if your model supports it)
            try {
                user.setOtp(null);
            } catch (Exception ignored) {
                // In case setOtp is not available, ignore — but repo earlier shows it's present.
            }
            try {
                user.setProfileCompleted(true);
            } catch (Exception ignored) {
                // If UsersEntity doesn't have setProfileCompleted, ignore
            }

            UsersEntity authenticatedUser = userRepository.save(user);

            // 7. Generate JWT tokens using jwtService
            String jwtAccessToken;
            String jwtRefreshToken;

            try {
                jwtAccessToken = jwtService.generateToken(authenticatedUser);
                jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser);
            } catch (Exception e) {
                response.put("status", "error");
                response.put("message", "Token generation failed: " + e.getMessage());
                return response;
            }

            // 8. Prepare success response - USER DATA
            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", authenticatedUser.getFirstName());
            userData.put("lastName", authenticatedUser.getLastName());
            userData.put("phoneNumber", authenticatedUser.getPhoneNumber());
            userData.put("countryCode", authenticatedUser.getCountryCode());
            userData.put("email", authenticatedUser.getEmail());
            userData.put("userType", authenticatedUser.getUserType());
            userData.put("userId", authenticatedUser.getUid());
            userData.put("id", authenticatedUser.getId());
            userData.put("isProfileCompleted", authenticatedUser.isProfileCompleted());

            // 9. Response structure matching controller expectation
            response.put("status", "success");
            response.put("phoneNumber", phoneNumber);
            response.put("userData", userData);
            response.put("accessToken", jwtAccessToken);
            response.put("refreshToken", jwtRefreshToken);

        } catch (Exception e) {
            // Generic fallback
            response.put("status", "error");
            response.put("message", "Verification failed: " + e.getMessage());
        }

        return response;
    }
}
