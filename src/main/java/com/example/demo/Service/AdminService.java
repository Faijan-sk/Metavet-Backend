package com.example.demo.Service;

import com.example.demo.Entities.AdminsEntity;
import com.example.demo.Repository.AdminRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
	
    @Autowired
    private AdminRepo adminRepository;

    @Autowired
    private JwtService jwtService;
	 
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminsEntity registerAdmin(AdminsEntity admin, String password) {
        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        
        admin.setPassword(passwordEncoder.encode(password));
        
        if (admin.getRole() == null) {
            admin.setRole(2); // Default role: Admin
        }
        
        return adminRepository.save(admin);
    }

    public AdminsEntity loginAdmin(String usernameOrEmail, String password) {
        Optional<AdminsEntity> adminOpt = adminRepository.findByUsernameOrEmail(usernameOrEmail);
        
        if (adminOpt.isPresent()) {
            AdminsEntity admin = adminOpt.get();
            if (passwordEncoder.matches(password, admin.getPassword())) {
                return admin;
            } else {
                throw new RuntimeException("Invalid password!");
            }
        } else {
            throw new RuntimeException("Admin not found!");
        }
    }

    public List<AdminsEntity> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<AdminsEntity> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<AdminsEntity> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Optional<AdminsEntity> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public List<AdminsEntity> getAdminsByRole(Integer role) {
        return adminRepository.findByRole(role);
    }

    public AdminsEntity updateAdmin(Long id, AdminsEntity updatedAdmin) {
        Optional<AdminsEntity> existingAdminOpt = adminRepository.findById(id);
        
        if (existingAdminOpt.isPresent()) {
            AdminsEntity existingAdmin = existingAdminOpt.get();
            
            if (updatedAdmin.getFullName() != null) {
                existingAdmin.setFullName(updatedAdmin.getFullName());
            }
            if (updatedAdmin.getEmail() != null && !updatedAdmin.getEmail().equals(existingAdmin.getEmail())) {
                if (adminRepository.existsByEmail(updatedAdmin.getEmail())) {
                    throw new RuntimeException("Email already exists!");
                }
                existingAdmin.setEmail(updatedAdmin.getEmail());
            }
            if (updatedAdmin.getRole() != null) {
                existingAdmin.setRole(updatedAdmin.getRole());
            }
            
            return adminRepository.save(existingAdmin);
        } else {
            throw new RuntimeException("Admin not found with ID: " + id);
        }
    }

    public AdminsEntity changePassword(Long adminId, String oldPassword, String newPassword) {
        Optional<AdminsEntity> adminOpt = adminRepository.findById(adminId);
        
        if (adminOpt.isPresent()) {
            AdminsEntity admin = adminOpt.get();
            
            if (passwordEncoder.matches(oldPassword, admin.getPassword())) {
                admin.setPassword(passwordEncoder.encode(newPassword));
                return adminRepository.save(admin);
            } else {
                throw new RuntimeException("Old password is incorrect!");
            }
        } else {
            throw new RuntimeException("Admin not found!");
        }
    }

    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException("Admin not found with ID: " + id);
        }
    }

    public boolean isUsernameAvailable(String username) {
        return !adminRepository.existsByUsername(username);
    }

    public boolean isEmailAvailable(String email) {
        return !adminRepository.existsByEmail(email);
    }
}
