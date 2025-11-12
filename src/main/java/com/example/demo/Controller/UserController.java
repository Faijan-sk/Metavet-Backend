package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.UserService;
import com.example.demo.Entities.UsersEntity;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsersEntity request, BindingResult bindingResult) {
        try {
            // Check for validation errors
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Validation failed");
                response.put("errors", errors);
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Call service and get saved user (with OTP + Token in response only)
            UsersEntity savedUser = userService.registerUser(request);
            
            // Check for null (email/phone already exists or invalid user type)
            if (savedUser == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email or phone number already exists, or invalid user type");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Success response with OTP and Token
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User Created Successfully as " + savedUser.getUserTypeAsString());
            response.put("data", getUserData(savedUser));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Helper method to format user data for response
     * Now includes both id (Long) and uid (UUID) from BaseEntity
     */
    private Map<String, Object> getUserData(UsersEntity user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());           // Long ID from BaseEntity
        userData.put("uid", user.getUid());         // UUID from BaseEntity
        userData.put("email", user.getEmail());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("countryCode", user.getCountryCode());
        userData.put("phoneNumber", user.getPhoneNumber());
        userData.put("userType", user.getUserType());
        userData.put("createdAt", user.getCreatedAt());     // From BaseEntity
        userData.put("updatedAt", user.getUpdatedAt());     // From BaseEntity
        // OTP and Token from saved user (DB has OTP encoded, response has plain OTP)
        userData.put("otp", user.getOtp());
        userData.put("token", user.getToken());
        return userData;
    }
}