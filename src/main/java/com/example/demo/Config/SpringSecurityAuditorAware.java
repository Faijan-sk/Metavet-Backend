package com.example.demo.Config;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<UsersEntity> {
    
    private final UserRepo userRepository;

    @Autowired
    public SpringSecurityAuditorAware(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UsersEntity> getCurrentAuditor() {
        // Skip auditing if NoAuditing annotation is present
        if (isNoAuditingEnabled()) {
            System.out.println("⚠️ Auditing disabled for this operation");
            return Optional.empty();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated() 
            || "anonymousUser".equals(authentication.getPrincipal())) {
            System.out.println("❌ No authenticated user found");
            return Optional.empty();
        }

        try {
            Object principal = authentication.getPrincipal();
            
            // ✅ Handle CustomUserDetails from JWT Filter
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                String userType = userDetails.getUserType();
                
                // Only process USER type (not ADMIN)
                if ("USER".equals(userType)) {
                    String email = userDetails.getUsername();
                    UsersEntity user = userRepository.findByEmail(email);
                    
                    if (user != null) {
                        System.out.println("✅ Current Auditor: " + user.getFirstName() 
                            + " " + user.getLastName() + " (ID: " + user.getUid() + ")");
                        return Optional.of(user);
                    } else {
                        System.out.println("❌ User not found in database: " + email);
                    }
                }
            } 
            // ✅ Fallback: Handle if principal is String (username/email)
            else if (principal instanceof String) {
                String username = (String) principal;
                UsersEntity user = userRepository.findByEmail(username);
                
                if (user != null) {
                    System.out.println("✅ Current Auditor (fallback): " + user.getFirstName() 
                        + " (ID: " + user.getUid() + ")");
                    return Optional.of(user);
                }
            }
            
            System.out.println("❌ Principal type not recognized: " + principal.getClass().getName());
            return Optional.empty();
            
        } catch (Exception e) {
            System.out.println("❌ Error getting current auditor: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private boolean isNoAuditingEnabled() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            try {
                Class<?> clazz = Class.forName(element.getClassName());
                Method method = clazz.getMethod(element.getMethodName());
                if (method.isAnnotationPresent(NoAuditing.class)) {
                    return true;
                }
            } catch (ClassNotFoundException | NoSuchMethodException ignored) {
                // Method not found, continue
            }
        }
        return false;
    }
}