package com.example.demo.Controller;


import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.DoctorService;
import com.example.demo.Dto.ApiResponse;
import com.example.demo.Dto.CreateDoctorRequest;
import com.example.demo.Dto.DoctorDtoForAdmin;
import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Enum.DoctorProfileStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class DoctorAuthController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepo userRepository;

    /**
     * Create new doctor profile
     * POST /api/doctors/create
     */
    @PostMapping("/doctor/create")
    public ResponseEntity<ApiResponse<DoctorsEntity>> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request,
            BindingResult bindingResult) {
        
        try {
            // Validation errors check
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining(", "));
                
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Validation failed: " + errorMessage, null));
            }

            // Check if user exists
            Optional<UsersEntity> userOptional = userRepository.findById(request.getUserId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "User not found with ID: " + request.getUserId(), null));
            }
            
            UsersEntity user = userOptional.get();

            // Check if user is doctor (userType = 2)
            if (user.getUserType() != 2) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "User is not registered as a doctor", null));
            }

            // Check if doctor profile already exists
            if (doctorService.hasUserDoctorProfile(user)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Doctor profile already exists for this user", null));
            }

            // Check if license number already exists
            if (doctorService.isLicenseNumberExists(request.getLicenseNumber())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "License number already exists: " + request.getLicenseNumber(), null));
            }

            // Create DoctorsEntity from request
            DoctorsEntity doctor = new DoctorsEntity();
            
            // Set user
            doctor.setUser(user);
            
            // Professional Information
            doctor.setLicenseNumber(request.getLicenseNumber());
            doctor.setLicenseIssueDate(request.getLicenseIssueDate());
            doctor.setLicenseExpiryDate(request.getLicenseExpiryDate());
            doctor.setQualification(request.getQualification());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setExperienceYears(request.getExperienceYears());
            
            // Hospital/Clinic Information
            doctor.setHospitalClinicName(request.getHospitalClinicName());
            doctor.setHospitalClinicAddress(request.getHospitalClinicAddress());
            doctor.setPincode(request.getPincode());
            
            // Address Information
            doctor.setAddress(request.getAddress());
            doctor.setCity(request.getCity());
            doctor.setState(request.getState());
            doctor.setCountry(request.getCountry() != null ? request.getCountry() : "India");
            
            // Personal Information
            doctor.setGender(request.getGender());
            doctor.setDateOfBirth(request.getDateOfBirth());
            doctor.setEmergencyContactNumber(request.getEmergencyContactNumber());
            
            // Employment Information
            doctor.setEmploymentType(request.getEmploymentType());
            doctor.setJoiningDate(request.getJoiningDate());
            doctor.setPreviousWorkplace(request.getPreviousWorkplace());
            
            // Professional Details
            doctor.setBio(request.getBio());
            doctor.setConsultationFee(request.getConsultationFee());
            
            // Default values
            doctor.setIsAvailable(true);
            doctor.setIsActive(true);
            doctor.setDoctorProfileStatus(DoctorProfileStatus.PENDING);
            doctor.setCreatedAt(LocalDateTime.now());
            doctor.setUpdatedAt(LocalDateTime.now());

            // Save doctor
            DoctorsEntity savedDoctor = doctorService.createDoctorEnhanced(doctor);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Doctor profile created successfully", savedDoctor));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to create doctor profile: " + e.getMessage(), null));
        }
    }

    /**
     * Get doctor by ID
     * GET /api/doctors/{doctorId}
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<ApiResponse<DoctorsEntity>> getDoctorById(@PathVariable Long doctorId) {
        try {
            return doctorService.getDoctorById(doctorId)
                    .map(doctor -> ResponseEntity.ok(
                            new ApiResponse<>(true, "Doctor found", doctor)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Doctor not found with ID: " + doctorId, null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching doctor: " + e.getMessage(), null));
        }
    }

    /**
     * Update doctor profile status (Admin use)
     * PUT /api/doctors/{doctorId}/status
     */
//    @PutMapping("/{doctorId}/status")
//    public ResponseEntity<ApiResponse<String>> updateDoctorStatus(
//            @PathVariable Long doctorId,
//            @RequestParam DoctorProfileStatus status,
//            @RequestParam(required = false) String updatedBy) {
//        
//        try {
//            boolean updated = doctorService.updateDoctorProfileStatus(doctorId, status);
//            
//            if (updated) {
//                return ResponseEntity.ok(
//                        new ApiResponse<>(true, "Doctor status updated successfully to: " + status, status.toString()));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse<>(false, "Doctor not found with ID: " + doctorId, null));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, "Error updating status: " + e.getMessage(), null));
//        }
//    }

    /**
     * Get all doctors with pagination (Optional)
     * GET /api/doctors?page=0&size=10
     */
    @GetMapping("/doctors/available")
    public ResponseEntity<Map<String, Object>> getAvailableDoctors() {
        Map<String, Object> response = new HashMap();
        List<DoctorDtoForClient> doctors = doctorService.getAvailableAndActive();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }
 
 @GetMapping("/admin/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctorsForAdmin() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorDtoForAdmin> doctors = doctorService.getAllDoctorsForAdmin();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }
 

  
   
}