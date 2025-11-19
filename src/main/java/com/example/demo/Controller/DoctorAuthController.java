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
import java.util.UUID;

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

    @PostMapping("/doctor/create")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody CreateDoctorRequest request, BindingResult bindingResult) {
        
        // Validation errors check
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", "Validation failed",
                    "errors", errors
                ));
        }

        try {
            // Convert userId string to UUID
            UUID userUid;
            try {
                userUid = UUID.fromString(request.getUserId());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "Invalid userId format. Must be a valid UUID"
                    ));
            }

            // Find user
            Optional<UsersEntity> userOpt = userRepository.findByUid(userUid);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "message", "User not found with UID: " + userUid
                    ));
            }

            // Create doctor entity
            DoctorsEntity doctor = new DoctorsEntity();
            doctor.setUser(userOpt.get());
            doctor.setExperienceYears(request.getExperienceYears());
            doctor.setHospitalClinicName(request.getHospitalClinicName());
            doctor.setHospitalClinicAddress(request.getHospitalClinicAddress());
            doctor.setPincode(request.getPincode());
            doctor.setAddress(request.getAddress());
            doctor.setCountry(request.getCountry());
            doctor.setCity(request.getCity());
            doctor.setState(request.getState());
            doctor.setBio(request.getBio());
            doctor.setConsultationFee(request.getConsultationFee());
            doctor.setGender(request.getGender());
            doctor.setDateOfBirth(request.getDateOfBirth());
            doctor.setLicenseNumber(request.getLicenseNumber());
            doctor.setLicenseIssueDate(request.getLicenseIssueDate());
            doctor.setLicenseExpiryDate(request.getLicenseExpiryDate());
            doctor.setQualification(request.getQualification());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setPreviousWorkplace(request.getPreviousWorkplace());
            doctor.setJoiningDate(request.getJoiningDate());
            doctor.setResignationDate(request.getResignationDate());
            doctor.setEmploymentType(request.getEmploymentType());
            doctor.setIsActive(request.getIsActive());
            doctor.setEmergencyContactNumber(request.getEmergencyContactNumber());

            // Save using service
            DoctorsEntity savedDoctor = doctorService.createDoctorEnhanced(doctor);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "message", "Doctor profile created successfully",
                    "data", savedDoctor
                ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "message", "Failed to create doctor profile: " + e.getMessage()
                ));
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
