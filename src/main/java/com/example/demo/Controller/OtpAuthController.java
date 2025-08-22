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
@RequestMapping("/auth/otp")
public class OtpAuthController {

    @Autowired
    private OtpAuthService otpServices;

    // Token URL me hoga aur OTP body me
    @PostMapping("/verify-otp/{token}")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @PathVariable("token") String token,
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            String otp = request.get("otp");

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

            // Service call
            Map<String, Object> verificationResult = otpServices.verifyOtpWithToken(token, otp);

            if ("success".equals(verificationResult.get("status"))) {
                // Include tokens at top level, outside data
                response.put("success", true);
                response.put("message", "OTP verified successfully.");
                response.put("data", verificationResult.get("userData"));
                response.put("accessToken", verificationResult.get("accessToken")); // Added at top level
                response.put("refreshToken", verificationResult.get("refreshToken")); // Added at top level

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", verificationResult.get("message"));
                return ResponseEntity.badRequest().body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Invalid token format");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "OTP verification failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}