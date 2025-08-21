package com.example.demo.Service;


import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class LoginAuthService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

        /**
     * Finds user by phone number, generates OTP & token, updates user record,
     * and returns response map
     */
    public Map<String, Object> checkUser(String phone_number) {

        // 1. Find user by phone number
        UsersEntity user = userRepository.findByPhoneNumber(phone_number);

        if (user == null) {
            return null; // Controller will handle 404
        }

        //returning country code too 
        String countryCode = user.getCountryCode();
        
        // 2. Generate raw OTP
        String rawOtp = generateOtp();

        // 3. Encode OTP before saving
        String encodedOtp = passwordEncoder.encode(rawOtp);

        // 4. Update user with new OTP and save
        user.setOtp(encodedOtp);
        userRepository.save(user);

        // 5. Generate temporary Base64 token (for OTP verification)
        String token = generateToken(phone_number);

        
        // 6. Prepare response
        Map<String, Object> response = new HashMap<>();
        
        response.put("status", "success");
        response.put("phone_number", phone_number);
        response.put("otp", rawOtp); // Only for testing
        response.put("token", token);
        response.put("countryCode", countryCode);

        return response;
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
