package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Service.LoginAuthService;

@RestController
@RequestMapping("/api/auth")
public class LoginAuthController {

    @Autowired
    private LoginAuthService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> verifyNumber(@RequestBody Map<String, String> request) {
        try {
            String phoneNumber = request.get("phone_number");

            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Phone number is required"
                ));
            }

            // Call service method to check user and generate OTP + token
            Map<String, Object> response = loginService.checkUser(phoneNumber);

            // If user not found
            if (response == null || "failed".equalsIgnoreCase((String) response.get("status"))) {
                return ResponseEntity.status(404).body(Map.of(
                    "status", "failed",
                    "message", "User not found"
                ));
            }

            // Return success response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "failed",
                "message", "Internal server error",
                "error", e.getMessage()
            ));
        }
    }
}
