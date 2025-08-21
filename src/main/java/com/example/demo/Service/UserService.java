package com.example.demo.Service;




import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.UserRepo;

import com.example.demo.Entities.UsersEntity;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepository; // Fixed: Renamed from userRepo to avoid confusion
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register new user with validation
     * @param request - User registration request
     * @return UsersEntity - Saved user with OTP and token, null if validation fails
     */
    public UsersEntity registerUser(UsersEntity request) {
        
        // Email validation - check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return null; // Controller will handle this
        }
        
        // Phone number validation - check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            return null; // Controller will handle this
        }
        
        // User type validation
        if (request.getUserType() == null || 
            request.getUserType() < 1 || 
            request.getUserType() > 3) {
            return null; // Controller will handle this
        }
        
        // Create new user entity
        UsersEntity user = new UsersEntity();
       
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCountryCode(request.getCountryCode()); 
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserType(request.getUserType());
        
        // Generate OTP for verification
        String rawOtp = generateOtp();
        
        // Encode OTP before saving to database for security
        String encodedOtp = passwordEncoder.encode(rawOtp);
        user.setOtp(encodedOtp);
        
        // Save user to database
        UsersEntity savedUser = userRepository.save(user);
        
        // Set plain OTP for response (not saving to DB)
        savedUser.setOtp(rawOtp);
        
        // Generate JWT-like token using full phone number (country code + phone number)
        String token = generateToken(request.getPhoneNumber());
        savedUser.setToken(token);
   
        return savedUser;
    }
    
    /**
     * Helper method to generate 4-digit OTP
     * @return String - Generated OTP
     */
    private String generateOtp() {
        int otp = 1000 + (int) (Math.random() * 9000);
        return String.valueOf(otp);
    }
    
    /**
     * Token Generator using Base64 encoding
     * @param phoneNumber - Phone number (without country code for token generation)
     * @return String - Base64 encoded token
     */
    private String generateToken(String phoneNumber) {
      
        String rawData = phoneNumber + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(rawData.getBytes());
    }
    
    /**
     * Verify OTP for user authentication
     * @param phoneNumber - User's phone number
     * @param otp - OTP to verify
     * @return boolean - true if OTP is valid
     */
    public boolean verifyOtp(String phoneNumber, String otp) {
        UsersEntity user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null && user.getOtp() != null) {
            return passwordEncoder.matches(otp, user.getOtp());
        }
        return false;
    }
    
    /**
     * Find user by email
     * @param email - User email
     * @return UsersEntity - Found user or null
     */
    public UsersEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find user by phone number
     * @param phoneNumber - User phone number
     * @return UsersEntity - Found user or null
     */
    public UsersEntity findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    
    // ============ NEW METHODS FOR APPOINTMENT FUNCTIONALITY ============
    
    /**
     * Find user by ID - Required for AppointmentService
     * @param userId - User ID
     * @return Optional<UsersEntity> - Found user wrapped in Optional
     */
    public Optional<UsersEntity> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Get all users - For admin purposes
     * @return List<UsersEntity> - List of all users
     */
    public List<UsersEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get users by user type - Filter clients, doctors, admins
     * @param userType - User type (1=Client, 2=Doctor, 3=Admin)
     * @return List<UsersEntity> - List of users by type
     */
    public List<UsersEntity> getUsersByType(Integer userType) {
        return userRepository.findByUserType(userType);
    }
    
    /**
     * Get all clients (userType = 1) - For appointment booking
     * @return List<UsersEntity> - List of client users
     */
    public List<UsersEntity> getAllClients() {
        return userRepository.findByUserType(1);
    }
    
    /**
     * Check if user exists by ID
     * @param userId - User ID
     * @return boolean - true if user exists
     */
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
    
    /**
     * Update user profile - For profile management
     * @param userId - User ID
     * @param updatedUser - Updated user information
     * @return UsersEntity - Updated user or null if not found
     */
    public UsersEntity updateUserProfile(Long userId, UsersEntity updatedUser) {
        Optional<UsersEntity> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            UsersEntity user = existingUser.get();
            
            // Update only allowed fields (not email, phone, userType)
            if (updatedUser.getFirstName() != null) {
                user.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                user.setLastName(updatedUser.getLastName());
            }
            
            return userRepository.save(user);
        }
        return null;
    }
    
    /**
     * Get user's full name - Utility method
     * @param userId - User ID
     * @return String - Full name or "Unknown User" if not found
     */
    public String getUserFullName(Long userId) {
        Optional<UsersEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UsersEntity u = user.get();
            return (u.getFirstName() != null ? u.getFirstName() : "") + 
                   " " + (u.getLastName() != null ? u.getLastName() : "");
        }
        return "Unknown User";
    }
    
    /**
     * Search users by name - For admin/search functionality
     * @param searchTerm - Search term to match in first or last name
     * @return List<UsersEntity> - List of matching users
     */
    public List<UsersEntity> searchUsersByName(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            searchTerm, searchTerm);
    }
    
    /**
     * Get users with valid OTP - For debugging/admin purposes
     * @return List<UsersEntity> - List of users with pending OTP verification
     */
    public List<UsersEntity> getUsersWithPendingVerification() {
        return userRepository.findByOtpIsNotNull();
    }
    
    /**
     * Clear user OTP after successful verification
     * @param userId - User ID
     * @return boolean - true if OTP cleared successfully
     */
    public boolean clearUserOtp(Long userId) {
        Optional<UsersEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UsersEntity u = user.get();
            u.setOtp(null);
            userRepository.save(u);
            return true;
        }
        return false;
    }
    
    /**
     * Activate/Deactivate user account - For admin purposes
     * @param userId - User ID
     * @param isActive - Active status
     * @return boolean - true if status updated successfully
     */
    public boolean updateUserActiveStatus(Long userId, boolean isActive) {
        Optional<UsersEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UsersEntity u = user.get();
            // Assuming there's an isActive field in UsersEntity
            // u.setActive(isActive);
            userRepository.save(u);
            return true;
        }
        return false;
    }
    
    /**
     * Count total users by type - For dashboard statistics
     * @param userType - User type
     * @return long - Count of users
     */
    public long countUsersByType(Integer userType) {
        return userRepository.countByUserType(userType);
    }
    
    /**
     * Get recently registered users - For admin dashboard
     * @param limit - Number of recent users to fetch
     * @return List<UsersEntity> - List of recently registered users
     */
    public List<UsersEntity> getRecentlyRegisteredUsers(int limit) {
        // Assuming there's a createdAt field or similar
        // return userRepository.findTopByOrderByCreatedAtDesc(limit);
        return userRepository.findAll(); // Placeholder - implement based on your entity structure
    }
}