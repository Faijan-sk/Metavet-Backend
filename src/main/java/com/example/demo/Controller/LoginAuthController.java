package com.example.demo.Controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.LoginAuthService;



@RestController
@RequestMapping("/auth")
public class LoginAuthController {

    @Autowired
    private LoginAuthService loginService;

    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> verifyNumber(@RequestBody Map<String, String> request) {

        String phoneNumber = request.get("phone_number");

        // Call service method to check user and generate OTP + token
        Map<String, Object> response = loginService.checkUser(phoneNumber);

        // If user not found, return 404 error response
        if (response == null) {
            return ResponseEntity.status(404).body(Map.of(
                "status", "failed",
                "message", "User not found"
            ));
        }

        // Return success response
        return ResponseEntity.ok(response);
    }
}

