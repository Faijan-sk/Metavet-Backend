package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.OtpAuthService;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpAuthController {

    @Autowired
    private OtpAuthService otpServices;

    // ✅ Endpoint kept exactly as requested: /api/auth/otp/verify-otp/{token}
    @PostMapping("/verify-otp/{token}")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @PathVariable("token") String token,
            @RequestBody Map<String, String> request) {

        System.out.println("===== OTP VERIFICATION CONTROLLER =====");
        System.out.println("Received token: " + token);
        System.out.println("Request body: " + request);
        System.out.println("======================================");

        Map<String, Object> response = new HashMap<>();

        try {
            // Extract OTP from request body
            String otp = request.get("otp");
            
            System.out.println("Extracted OTP: " + otp);

            // OTP validation (4 digits)
            if (otp == null || !otp.matches("^[0-9]{4}$")) {
                response.put("success", false);
                response.put("message", "OTP must be 4 digits");
                return ResponseEntity.badRequest().body(response);
            }

            // Token validation
            if (token == null || token.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Token is required");
                return ResponseEntity.badRequest().body(response);
            }

            System.out.println("Calling OTP service...");
            
            // Service call
            Map<String, Object> verificationResult = otpServices.verifyOtpWithToken(token, otp);
            
            System.out.println("Service result: " + verificationResult);

            if ("success".equals(verificationResult.get("status"))) {
                // Success response
                response.put("success", true);
                response.put("message", "OTP verified successfully.");
                response.put("data", verificationResult.get("userData"));
                response.put("accessToken", verificationResult.get("accessToken"));
                response.put("refreshToken", verificationResult.get("refreshToken"));

                System.out.println("✅ OTP verification successful");
                return ResponseEntity.ok(response);
            } else {
                // Failure response
                response.put("success", false);
                response.put("message", verificationResult.get("message"));
                
                System.out.println("❌ OTP verification failed: " + verificationResult.get("message"));
                // Use 400 for invalid OTP/token, 404 or other codes could be used based on message.
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid token format: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Invalid token format");
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            System.out.println("❌ Internal error: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "OTP verification failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // ✅ Additional debugging endpoint to test controller mapping
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Controller is working correctly");
        response.put("timestamp", System.currentTimeMillis());
        
        System.out.println("✅ Test endpoint called - Controller is working!");
        return ResponseEntity.ok(response);
    }
}
