package com.example.demo.Controller;

import com.example.demo.Dto.DoctorDtoForAdmin;
import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Enum.DoctorProfileStatus;
import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;
import com.example.demo.Service.DoctorService;
import com.example.demo.Repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // If you don't need userRepository in controller, you can remove this.
    @Autowired
    private UserRepo userRepository;

    // ==================== BASIC CRUD OPERATIONS ====================

    /**
     * Create a new doctor profile
     * POST /api/doctors
     */
    @PostMapping
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorsEntity doctor) {
        try {
            DoctorsEntity savedDoctor = doctorService.createDoctorEnhanced(doctor);

            // Initialize lazy fields to avoid serialization issues
            if (savedDoctor.getUser() != null) {
                savedDoctor.getUser().getEmail(); // touch the lazy field
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "message", "Doctor profile created successfully. User profile marked as completed.",
                    "data", savedDoctor,
                    "profileCompleted", savedDoctor.getUser().isProfileCompleted()
                ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "message", e.getMessage()
                ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "success", false,
                    "message", "Failed to create doctor profile: " + e.getMessage()
                ));
        }
    }

    /**
     * Update doctor profile by user UID (UUID)
     * PUT /api/doctors/user/{userUid}
     *
     * NOTE: changed from Long to UUID to match service signature.
     */
    @PutMapping("/user/{userUid}")
    public ResponseEntity<?> updateDoctorProfile(@PathVariable("userUid") UUID userUid,
                                                 @Valid @RequestBody DoctorsEntity doctorRequest) {
        try {
            DoctorsEntity updatedDoctor = doctorService.updateDoctorProfile(userUid, doctorRequest);
            if (updatedDoctor != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor profile updated successfully",
                    "data", updatedDoctor
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "User not found, not a doctor, or license number already exists"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to update doctor profile: " + e.getMessage()
            ));
        }
    }

    /**
     * Get doctor by ID
     * GET /api/doctors/{doctorId}
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long doctorId) {
        try {
            Optional<DoctorsEntity> doctor = doctorService.getDoctorById(doctorId);
            if (doctor.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", doctor.get()
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctor: " + e.getMessage()
            ));
        }
    }

    /**
     * Get all doctors
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<DoctorsEntity> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Update doctor
     * PUT /api/doctors/{doctorId}
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long doctorId,
                                          @Valid @RequestBody DoctorsEntity updatedDoctor) {
        try {
            DoctorsEntity doctor = doctorService.updateDoctor(doctorId, updatedDoctor);
            if (doctor != null) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor updated successfully",
                    "data", doctor
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to update doctor: " + e.getMessage()
            ));
        }
    }

    /**
     * Delete doctor (hard delete)
     * DELETE /api/doctors/{doctorId}
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long doctorId) {
        try {
            boolean deleted = doctorService.deleteDoctor(doctorId);
            if (deleted) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor deleted successfully"
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to delete doctor: " + e.getMessage()
            ));
        }
    }

    /**
     * Soft delete doctor
     * PUT /api/doctors/{doctorId}/soft-delete
     */
    @PutMapping("/{doctorId}/soft-delete")
    public ResponseEntity<?> softDeleteDoctor(@PathVariable Long doctorId) {
        try {
            boolean deleted = doctorService.softDeleteDoctor(doctorId);
            if (deleted) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor deactivated successfully"
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to deactivate doctor: " + e.getMessage()
            ));
        }
    }

    // -------------- SEARCH / FILTER endpoints (kept same) --------------

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<?> getDoctorByLicense(@PathVariable String licenseNumber) {
        try {
            Optional<DoctorsEntity> doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
            if (doctor.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "data", doctor.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctor: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<?> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsBySpecialization(specialization);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<?> getDoctorsByCity(@PathVariable String city) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByCity(city);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<?> getDoctorsByState(@PathVariable String state) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByState(state);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDoctors() {
        try {
            List<DoctorsEntity> doctors = doctorService.getAvailableDoctors();
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving available doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveDoctors() {
        try {
            List<DoctorsEntity> doctors = doctorService.getActiveDoctors();
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving active doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== PROFILE STATUS ENDPOINTS ====================

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getDoctorsByProfileStatus(@PathVariable DoctorProfileStatus status) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByProfileStatus(status);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{doctorId}/status")
    public ResponseEntity<?> updateDoctorStatus(@PathVariable Long doctorId,
                                                @RequestBody Map<String, String> statusRequest) {
        try {
            DoctorProfileStatus status = DoctorProfileStatus.valueOf(statusRequest.get("status"));
            boolean updated = doctorService.updateDoctorProfileStatus(doctorId, status);

            if (updated) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Doctor status updated successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid status value. Valid values: PENDING, APPROVED, REJECTED"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to update status: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{doctorId}/approve")
    public ResponseEntity<?> approveDoctorProfile(@PathVariable Long doctorId,
                                                 @RequestBody(required = false) Map<String, String> request) {
        try {
            String approvedBy = request != null ? request.get("approvedBy") : "Admin";
            boolean approved = doctorService.approveDoctorProfile(doctorId, approvedBy);

            if (approved) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Doctor profile approved successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to approve doctor: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{doctorId}/reset-pending")
    public ResponseEntity<?> resetToPending(@PathVariable Long doctorId,
                                           @RequestBody(required = false) Map<String, String> request) {
        try {
            String updatedBy = request != null ? request.get("updatedBy") : "Admin";
            boolean reset = doctorService.resetDoctorProfileToPending(doctorId, updatedBy);

            if (reset) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Doctor profile reset to pending successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to reset status: " + e.getMessage()
            ));
        }
    }

    // ==================== ADMIN ENDPOINTS ====================

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllDoctorsForAdmin() {
        try {
            List<DoctorDtoForAdmin> doctors = doctorService.getAllDoctorsForAdmin();
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors for admin: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<?> getPendingDoctors() {
        try {
            List<DoctorsEntity> pendingDoctors = doctorService.getPendingDoctorsForApproval();
            return ResponseEntity.ok(Map.of("success", true, "count", pendingDoctors.size(), "data", pendingDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving pending doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/admin/approved")
    public ResponseEntity<?> getApprovedDoctors() {
        try {
            List<DoctorsEntity> approvedDoctors = doctorService.getApprovedDoctors();
            return ResponseEntity.ok(Map.of("success", true, "count", approvedDoctors.size(), "data", approvedDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving approved doctors: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/admin/rejected")
    public ResponseEntity<?> getRejectedDoctors() {
        try {
            List<DoctorsEntity> rejectedDoctors = doctorService.getRejectedDoctors();
            return ResponseEntity.ok(Map.of("success", true, "count", rejectedDoctors.size(), "data", rejectedDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving rejected doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== CLIENT ENDPOINTS ====================

    @GetMapping("/client/available")
    public ResponseEntity<?> getAvailableDoctorsForClient() {
        try {
            List<DoctorDtoForClient> doctors = doctorService.getAvailableActiveApproved();
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving available doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== FILTER ENDPOINTS ====================

    @GetMapping("/filter/experience")
    public ResponseEntity<?> getDoctorsByExperience(@RequestParam(defaultValue = "0") @Min(0) Integer min,
                                                   @RequestParam(defaultValue = "50") @Max(50) Integer max) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsWithExperienceRange(min, max);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by experience: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/filter/fee")
    public ResponseEntity<?> getDoctorsByFeeRange(@RequestParam(defaultValue = "0") Double min,
                                                  @RequestParam(defaultValue = "50000") Double max) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsWithFeeRange(min, max);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by fee: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/filter/gender/{gender}")
    public ResponseEntity<?> getDoctorsByGender(@PathVariable Gender gender) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByGender(gender);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by gender: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/filter/employment/{type}")
    public ResponseEntity<?> getDoctorsByEmploymentType(@PathVariable EmploymentType type) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByEmploymentType(type);
            return ResponseEntity.ok(Map.of("success", true, "count", doctors.size(), "data", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by employment type: " + e.getMessage()
            ));
        }
    }



    

  

  



}
