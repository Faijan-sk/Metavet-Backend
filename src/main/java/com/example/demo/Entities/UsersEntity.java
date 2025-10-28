package com.example.demo.Entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "users_entity")
public class UsersEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    
    @Email(message = "Please provide a valid email")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^\\+[1-9]\\d{0,3}$", message = "Country code must start with + and contain 1-4 digits")
    @Column(name = "country_code", nullable = false)
    private String countryCode;
      
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(name = "phone_number", unique = true, nullable = false, length = 10)
    private String phoneNumber;
      
    @NotBlank(message = "First Name is required")
    @Pattern(
        regexp = "^[A-Za-z ]{3,30}$",
        message = "First name must contain only letters and be 3 to 30 characters long"
    )
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;
    
    @NotBlank(message = "Last Name is required")
    @Pattern(
        regexp = "^[A-Za-z ]{3,30}$",
        message = "Last name must contain only letters and be 3 to 30 characters long"
    )
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;
    
    @NotNull(message = "User type is required")
    @Min(value = 1, message = "User type must be 1 (Client), 2 (Doctor), or 3 (Service Provider)")
    @Max(value = 3, message = "User type must be 1 (Client), 2 (Doctor), or 3 (Service Provider)")
    @Column(name = "user_type", nullable = false)
    private Integer userType; // 1=Client, 2=Doctor, 3=Service Provider
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "otp")
    private String otp;

    // Token field only for response, not saved in DB
    @Transient
    private String token;
    
    @Column(name = "is_profile_completed", nullable = false)
    private boolean isProfileCompleted = false;

    // Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper method to get user type as string
    public String getUserTypeAsString() {
        switch (userType) {
            case 1: return "Client";
            case 2: return "Doctor";
            case 3: return "Service Provider";
            default: return "Unknown";
        }
    }
    
    // Helper method to get full phone number with country code
    public String getFullPhoneNumber() {
        return countryCode + phoneNumber;
    }
    
    // UserDetails implementation - FIXED
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authority based on user type
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + getUserTypeAsString().toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        // Since you're using OTP, return empty string or null
        // If you want to use password later, add a password field
        return "";
    }

    @Override
    public String getUsername() {
        // Return email as username for authentication
        return this.email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    // Getters and Setters
    public Long getUid() {
        return uid;
    }
    
    public void setUid(Long uid) {
        this.uid = uid;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Integer getUserType() {
        return userType;
    }
    
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Token getter & setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public boolean isProfileCompleted() {
        return isProfileCompleted;
    }

    public void setProfileCompleted(boolean isProfileCompleted) {
        this.isProfileCompleted = isProfileCompleted;
    }
}