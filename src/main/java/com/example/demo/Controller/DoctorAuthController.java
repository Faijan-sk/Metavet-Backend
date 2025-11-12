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
import java.util.stream.Collectors;
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

/**
 * DoctorAuthController - cleaned and aligned with DoctorService methods.
 *
 * Notes:
 *  - Use @RestController only (no duplicate @Controller).
 *  - Uses existing DoctorService methods: getDoctorByUser(...) and getDoctorByLicenseNumber(...)
 *    to avoid calling non-existing boolean helpers.
 *  - Maps DoctorsEntity -> DoctorDtoForClient inside controller for the "available doctors" endpoint.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class DoctorAuthController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepo userRepository;

    public DoctorAuthController() {
        System.out.println("🔨 DoctorAuthController instance created!");
    }

    /**
     * Test endpoint to verify controller is working
     */
    @GetMapping("/doctors/test")
    public ResponseEntity<Map<String, Object>> testDoctorController() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "DoctorAuthController is working correctly!");
        response.put("timestamp", System.currentTimeMillis());

        System.out.println("✅ Doctor test endpoint called - Controller is working!");
        return ResponseEntity.ok(response);
    }

    /**
     * Get available doctors
     * GET /api/auth/doctors/available
     *
     * Returns a list of DoctorDtoForClient objects mapped from DoctorsEntity.
     */
    @GetMapping("/doctors/available")
    public ResponseEntity<Map<String, Object>> getAvailableDoctors() {
        System.out.println("=== AVAILABLE DOCTORS ENDPOINT CALLED ===");
        System.out.println("Processing request for available doctors...");

        try {
            Map<String, Object> response = new HashMap<>();

            // Fetch entities (service method returns List<DoctorsEntity>)
            List<DoctorsEntity> entities = doctorService.getAvailableAndActive();

            // Map to DTOs for client
            List<DoctorDtoForClient> doctors = entities.stream()
                    .map(this::mapToClientDto)
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", doctors);
            response.put("count", doctors.size());

            System.out.println("✅ Found " + doctors.size() + " available doctors");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error in getAvailableDoctors: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching doctors: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Create new doctor profile
     * POST /api/auth/doctor/create
     */
    @PostMapping("/doctor/create")
    public ResponseEntity<ApiResponse<DoctorsEntity>> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request,
            BindingResult bindingResult) {

        System.out.println("=== CREATE DOCTOR ENDPOINT CALLED ===");

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
            if (user.getUserType() == null || user.getUserType() != 2) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "User is not registered as a doctor", null));
            }

            // Check if doctor profile already exists (use service method that returns Optional)
            if (doctorService.getDoctorByUser(user).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Doctor profile already exists for this user", null));
            }

            // Check if license number already exists (use service method that returns Optional)
            if (request.getLicenseNumber() != null && doctorService.getDoctorByLicenseNumber(request.getLicenseNumber()).isPresent()) {
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

            System.out.println("✅ Doctor profile created successfully!");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Doctor profile created successfully", savedDoctor));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            System.out.println("❌ Error creating doctor: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to create doctor profile: " + e.getMessage(), null));
        }
    }

    /**
     * Get all doctors for admin
     */
    @GetMapping("/admin/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctorsForAdmin() {
        System.out.println("=== ADMIN DOCTORS ENDPOINT CALLED ===");

        try {
            Map<String, Object> response = new HashMap<>();
            List<DoctorDtoForAdmin> doctors = doctorService.getAllDoctorsForAdmin();
            response.put("success", true);
            response.put("data", doctors);
            response.put("count", doctors.size());

            System.out.println("✅ Fetched " + doctors.size() + " doctors for admin");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error fetching doctors for admin: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching doctors: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get doctors by status
     */
    @GetMapping("/doctors/status/{status}")
    public ResponseEntity<?> getDoctorsByProfileStatus(@PathVariable DoctorProfileStatus status) {
        System.out.println("=== GET DOCTORS BY STATUS: " + status + " ===");

        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByProfileStatus(status);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", doctors.size(),
                    "data", doctors
            ));
        } catch (Exception e) {
            System.out.println("❌ Error getting doctors by status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Update doctor profile status
     */
    @PutMapping("/doctors/{doctorId}/status")
    public ResponseEntity<?> updateDoctorStatus(@PathVariable Long doctorId,
                                                @RequestBody Map<String, String> statusRequest) {
        System.out.println("=== UPDATE DOCTOR STATUS: " + doctorId + " ===");

        try {
            DoctorProfileStatus status = DoctorProfileStatus.valueOf(statusRequest.get("status"));
            boolean updated = doctorService.updateDoctorProfileStatus(doctorId, status);

            if (updated) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Doctor status updated successfully"
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid status value. Valid values: PENDING, APPROVED, REJECTED"
            ));
        } catch (Exception e) {
            System.out.println("❌ Error updating doctor status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to update status: " + e.getMessage()
            ));
        }
    }

    // -------------------- Helper methods --------------------

    /**
     * Map DoctorsEntity -> DoctorDtoForClient
     * (fields set match what DoctorDtoForClient is expected to have in other code)
     */
    private DoctorDtoForClient mapToClientDto(DoctorsEntity doctor) {
        DoctorDtoForClient dto = new DoctorDtoForClient();
        if (doctor == null) return dto;

        try {
            if (doctor.getUser() != null) {
                dto.setDoctorUid(doctor.getUser().getUid());
                dto.setEmail(doctor.getUser().getEmail());
                dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
                dto.setFirstName(doctor.getUser().getFirstName());
                dto.setLastName(doctor.getUser().getLastName());
            }
        } catch (Exception ignored) {
            // ignore user mapping issues
        }

        dto.setDoctorId(doctor.getDoctorId());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setAddress(doctor.getAddress());
        dto.setCity(doctor.getCity());
        dto.setState(doctor.getState());
        dto.setBio(doctor.getBio());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setQualification(doctor.getQualification());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setDoctorProfileStatus(doctor.getDoctorProfileStatus());

        return dto;
    }
}
