package com.example.demo.Controller;

import com.example.demo.Entities.AdminsEntity;
import com.example.demo.Service.AdminService;
import com.example.demo.Service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class AdminController {
    
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    private boolean isTokenExpired(String token) {
        try {
            return jwtService.extractClaim(token, claims -> claims.getExpiration(), true)
                    .before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerAdmin(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
      
        try {
            AdminsEntity admin = new AdminsEntity();
            admin.setFullName(request.get("fullName"));
            admin.setUsername(request.get("username"));
            admin.setEmail(request.get("email"));
            
            // Role as Integer
            String roleStr = request.get("role");
            if (roleStr != null) {
                admin.setRole(Integer.parseInt(roleStr));
            }

            String password = request.get("password");
            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required!");
                return ResponseEntity.badRequest().body(response);
            }
            
            AdminsEntity savedAdmin = adminService.registerAdmin(admin, password);
            response.put("success", true);
            response.put("message", "Admin registered successfully!");
            response.put("data", Map.of(
                "id", savedAdmin.getId(),
                "fullName", savedAdmin.getFullName(),
                "username", savedAdmin.getUsername(),
                "email", savedAdmin.getEmail(),
                "role", savedAdmin.getRole(),
                "roleName", savedAdmin.getRoleName()
            ));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("we are in login API ");
        try {
            String usernameOrEmail = loginRequest.get("userName");
            String password = loginRequest.get("password");
            if (usernameOrEmail == null || password == null) {
                response.put("success", false);
                response.put("message", "Username/Email and Password are required!");
                return ResponseEntity.badRequest().body(response);
            }
            
            AdminsEntity admin = adminService.loginAdmin(usernameOrEmail, password);
            String jwtAccessToken = jwtService.generateToken(admin);
            
            List<Map<String, String>> permissions = List.of(
                Map.of("subject", "doctor-management", "action", "read"),
                Map.of("subject", "doctor-management", "action", "create"),
                Map.of("subject", "doctor-management", "action", "edit"),
                Map.of("subject", "doctor-management", "action", "delete"),
                Map.of("subject", "patient-management", "action", "read")
            );
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", admin.getId());
            userData.put("role", admin.getRole());
            userData.put("roleName", admin.getRoleName());
            userData.put("fullName", admin.getFullName());
            userData.put("username", admin.getUsername());
            userData.put("email", admin.getEmail());
            userData.put("permission", permissions);
            
            response.put("success", true);
            response.put("accessToken", jwtAccessToken);
            response.put("userData", userData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAdmins() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AdminsEntity> admins = adminService.getAllAdmins();
            response.put("success", true);
            response.put("message", "Admins retrieved successfully!");
            response.put("data", admins);
            response.put("count", admins.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAdminById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AdminsEntity> adminOpt = adminService.getAdminById(id);
            if (adminOpt.isPresent()) {
                AdminsEntity admin = adminOpt.get();
                response.put("success", true);
                response.put("message", "Admin found!");
                response.put("data", Map.of(
                    "id", admin.getId(),
                    "fullName", admin.getFullName(),
                    "username", admin.getUsername(),
                    "email", admin.getEmail(),
                    "role", admin.getRole(),
                    "roleName", admin.getRoleName()
                ));
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Admin not found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAdmin(@PathVariable Long id, @RequestBody AdminsEntity updatedAdmin) {
        Map<String, Object> response = new HashMap<>();
        try {
            AdminsEntity admin = adminService.updateAdmin(id, updatedAdmin);
            response.put("success", true);
            response.put("message", "Admin updated successfully!");
            response.put("data", Map.of(
                "id", admin.getId(),
                "fullName", admin.getFullName(),
                "username", admin.getUsername(),
                "email", admin.getEmail(),
                "role", admin.getRole(),
                "roleName", admin.getRoleName()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            if (oldPassword == null || newPassword == null) {
                response.put("success", false);
                response.put("message", "Old password and new password are required!");
                return ResponseEntity.badRequest().body(response);
            }
            
            adminService.changePassword(id, oldPassword, newPassword);
            response.put("success", true);
            response.put("message", "Password changed successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAdmin(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.deleteAdmin(id);
            response.put("success", true);
            response.put("message", "Admin deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = adminService.isUsernameAvailable(username);
        response.put("success", true);
        response.put("available", isAvailable);
        response.put("message", isAvailable ? "Username is available!" : "Username is already taken!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = adminService.isEmailAvailable(email);
        response.put("success", true);
        response.put("available", isAvailable);
        response.put("message", isAvailable ? "Email is available!" : "Email is already registered!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Map<String, Object>> getAdminsByRole(@PathVariable Integer role) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AdminsEntity> admins = adminService.getAdminsByRole(role);
            response.put("success", true);
            response.put("message", "Admins with role '" + role + "' retrieved successfully!");
            response.put("data", admins);
            response.put("count", admins.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
