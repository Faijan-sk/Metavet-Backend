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
public class LoginAuthService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Finds user by phone number, generates OTP & token, updates user record,
     * and returns response map.
     */
    public Map<String, Object> checkUser(String phoneNumber) {
        try {
            // Basic validation
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "failed");
                response.put("message", "Phone number is required");
                return response;
            }

            // 1. Find user by phone number
            Optional<UsersEntity> optionalUser = userRepository.findByPhoneNumber(phoneNumber);

            if (optionalUser.isEmpty()) {
                return null; // Controller will handle user not found
            }

            UsersEntity user = optionalUser.get();
            String countryCode = user.getCountryCode();

            // 2. Generate raw OTP
            String rawOtp = generateOtp();

            // 3. Encode OTP before saving
            String encodedOtp = passwordEncoder.encode(rawOtp);

            // 4. Update user with new OTP and save
            user.setOtp(encodedOtp);
            userRepository.save(user);

            // 5. Generate temporary Base64 token (for OTP verification)
            String token = generateToken(phoneNumber);

            // 6. Prepare success response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "OTP generated successfully");
            response.put("phone_number", phoneNumber);
            response.put("countryCode", countryCode);
            response.put("otp", rawOtp); // Only for testing - remove in production
            response.put("token", token);
            response.put("userType", user.getUserType());
            response.put("userTypeName", user.getUserTypeAsString());
            response.put("uid", user.getUid());
            response.put("id", user.getId());

            return response;

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "failed");
            errorResponse.put("message", "Error while generating OTP");
            errorResponse.put("error", e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Generate 4-digit OTP
     */
    private String generateOtp() {
        int otp = 1000 + (int) (Math.random() * 9000);
        return String.valueOf(otp);
    }

    /**
     * Generate temporary token for OTP verification
     */
    private String generateToken(String phoneNumber) {
        String rawData = phoneNumber + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(rawData.getBytes());
    }
}
