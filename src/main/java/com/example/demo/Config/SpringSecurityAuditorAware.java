package com.example.demo.Config;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No authenticated user found");
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        try {
            String email = null;

            // If principal is UserDetails (including your CustomUserDetails), get username/email
            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            }
            // If principal is a plain string (sometimes frameworks set username as String)
            else if (principal instanceof String) {
                email = (String) principal;
            } else {
                System.out.println("❌ Principal type not recognized: " + principal.getClass().getName());
                return Optional.empty();
            }

            if (email == null || email.trim().isEmpty()) {
                System.out.println("❌ No username/email available in principal");
                return Optional.empty();
            }

            // Use the repository's Optional-returning method
            Optional<UsersEntity> userOpt = userRepository.findByEmail(email.trim());

            if (userOpt.isPresent()) {
                UsersEntity user = userOpt.get();
                System.out.println("✅ Current Auditor: " + user.getFirstName() + " " + user.getLastName()
                        + " (ID: " + user.getUid() + ")");
                return Optional.of(user);
            } else {
                System.out.println("❌ User not found in database: " + email);
                return Optional.empty();
            }

        } catch (Exception e) {
            System.out.println("❌ Error getting current auditor: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Walks the current stacktrace and checks whether any method in the call stack is annotated with
     * {@link NoAuditing}. If so, auditing is skipped.
     *
     * This method is defensive: any reflection failures are ignored and treated as "no annotation found".
     */
    private boolean isNoAuditingEnabled() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            try {
                Class<?> clazz = Class.forName(element.getClassName());
                // Try to find method with matching name (we don't know parameter types safely) - iterate declared methods
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(element.getMethodName())) {
                        if (method.isAnnotationPresent(NoAuditing.class)) {
                            return true;
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                // ignore and continue
            } catch (Throwable t) {
                // any other reflection error - ignore to avoid breaking auditing lookup
            }
        }
        return false;
    }
}
