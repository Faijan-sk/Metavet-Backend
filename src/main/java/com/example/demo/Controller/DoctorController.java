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

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

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
            savedDoctor.getUser().getEmail(); // Touch the lazy field
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
     * Update doctor profile by user ID
     * PUT /api/doctors/user/{userId}
     */
    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateDoctorProfile(@PathVariable Long userId, 
                                               @Valid @RequestBody DoctorsEntity doctorRequest) {
        try {
            DoctorsEntity updatedDoctor = doctorService.updateDoctorProfile(userId, doctorRequest);
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

    // ==================== SEARCH AND FILTER ENDPOINTS ====================

    /**
     * Get doctor by license number
     * GET /api/doctors/license/{licenseNumber}
     */
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<?> getDoctorByLicense(@PathVariable String licenseNumber) {
        try {
            Optional<DoctorsEntity> doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
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
     * Get doctors by specialization
     * GET /api/doctors/specialization/{specialization}
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<?> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsBySpecialization(specialization);
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
     * Get doctors by city
     * GET /api/doctors/city/{city}
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<?> getDoctorsByCity(@PathVariable String city) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByCity(city);
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
     * Get doctors by state
     * GET /api/doctors/state/{state}
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<?> getDoctorsByState(@PathVariable String state) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByState(state);
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
     * Get available doctors
     * GET /api/doctors/available
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDoctors() {
        try {
            List<DoctorsEntity> doctors = doctorService.getAvailableDoctors();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving available doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Get active doctors
     * GET /api/doctors/active
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveDoctors() {
        try {
            List<DoctorsEntity> doctors = doctorService.getActiveDoctors();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving active doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== PROFILE STATUS ENDPOINTS ====================

    /**
     * Get doctors by profile status
     * GET /api/doctors/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getDoctorsByProfileStatus(@PathVariable DoctorProfileStatus status) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByProfileStatus(status);
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
     * Update doctor profile status
     * PUT /api/doctors/{doctorId}/status
     */
    @PutMapping("/{doctorId}/status")
    public ResponseEntity<?> updateDoctorStatus(@PathVariable Long doctorId,
                                              @RequestBody Map<String, String> statusRequest) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to update status: " + e.getMessage()
            ));
        }
    }

    /**
     * Approve doctor profile (Admin endpoint)
     * PUT /api/doctors/{doctorId}/approve
     */
    @PutMapping("/{doctorId}/approve")
    public ResponseEntity<?> approveDoctorProfile(@PathVariable Long doctorId,
                                                @RequestBody(required = false) Map<String, String> request) {
        try {
            String approvedBy = request != null ? request.get("approvedBy") : "Admin";
            boolean approved = doctorService.approveDoctorProfile(doctorId, approvedBy);
            
            if (approved) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor profile approved successfully"
                ));
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

    /**
     * Reset doctor profile to pending
     * PUT /api/doctors/{doctorId}/reset-pending
     */
    @PutMapping("/{doctorId}/reset-pending")
    public ResponseEntity<?> resetToPending(@PathVariable Long doctorId,
                                          @RequestBody(required = false) Map<String, String> request) {
        try {
            String updatedBy = request != null ? request.get("updatedBy") : "Admin";
            boolean reset = doctorService.resetDoctorProfileToPending(doctorId, updatedBy);
            
            if (reset) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor profile reset to pending successfully"
                ));
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

    /**
     * Get all doctors for admin
     * GET /api/doctors/admin/all
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllDoctorsForAdmin() {
        try {
            List<DoctorDtoForAdmin> doctors = doctorService.getAllDoctorsForAdmin();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors for admin: " + e.getMessage()
            ));
        }
    }

    /**
     * Get pending doctors for approval
     * GET /api/doctors/admin/pending
     */
    @GetMapping("/admin/pending")
    public ResponseEntity<?> getPendingDoctors() {
        try {
            List<DoctorsEntity> pendingDoctors = doctorService.getPendingDoctorsForApproval();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", pendingDoctors.size(),
                "data", pendingDoctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving pending doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Get approved doctors
     * GET /api/doctors/admin/approved
     */
    @GetMapping("/admin/approved")
    public ResponseEntity<?> getApprovedDoctors() {
        try {
            List<DoctorsEntity> approvedDoctors = doctorService.getApprovedDoctors();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", approvedDoctors.size(),
                "data", approvedDoctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving approved doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Get rejected doctors
     * GET /api/doctors/admin/rejected
     */
    @GetMapping("/admin/rejected")
    public ResponseEntity<?> getRejectedDoctors() {
        try {
            List<DoctorsEntity> rejectedDoctors = doctorService.getRejectedDoctors();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", rejectedDoctors.size(),
                "data", rejectedDoctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving rejected doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== CLIENT ENDPOINTS ====================

    /**
     * Get available, active and approved doctors for client
     * GET /api/doctors/client/available
     */
    @GetMapping("/client/available")
    public ResponseEntity<?> getAvailableDoctorsForClient() {
        try {
            List<DoctorDtoForClient> doctors = doctorService.getAvailableActiveApproved();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving available doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== FILTER ENDPOINTS ====================

    /**
     * Get doctors by experience range
     * GET /api/doctors/filter/experience?min={minYears}&max={maxYears}
     */
    @GetMapping("/filter/experience")
    public ResponseEntity<?> getDoctorsByExperience(@RequestParam(defaultValue = "0") @Min(0) Integer min,
                                                   @RequestParam(defaultValue = "50") @Max(50) Integer max) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsWithExperienceRange(min, max);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by experience: " + e.getMessage()
            ));
        }
    }

    /**
     * Get doctors by consultation fee range
     * GET /api/doctors/filter/fee?min={minFee}&max={maxFee}
     */
    @GetMapping("/filter/fee")
    public ResponseEntity<?> getDoctorsByFeeRange(@RequestParam(defaultValue = "0") Double min,
                                                @RequestParam(defaultValue = "50000") Double max) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsWithFeeRange(min, max);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by fee: " + e.getMessage()
            ));
        }
    }

    /**
     * Get doctors by gender
     * GET /api/doctors/filter/gender/{gender}
     */
    @GetMapping("/filter/gender/{gender}")
    public ResponseEntity<?> getDoctorsByGender(@PathVariable Gender gender) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByGender(gender);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by gender: " + e.getMessage()
            ));
        }
    }

    /**
     * Get doctors by employment type
     * GET /api/doctors/filter/employment/{type}
     */
    @GetMapping("/filter/employment/{type}")
    public ResponseEntity<?> getDoctorsByEmploymentType(@PathVariable EmploymentType type) {
        try {
            List<DoctorsEntity> doctors = doctorService.getDoctorsByEmploymentType(type);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error filtering doctors by employment type: " + e.getMessage()
            ));
        }
    }

    // ==================== SEARCH ENDPOINTS ====================

    /**
     * Search doctors by hospital name
     * GET /api/doctors/search/hospital?keyword={keyword}
     */
    @GetMapping("/search/hospital")
    public ResponseEntity<?> searchDoctorsByHospital(@RequestParam String keyword) {
        try {
            List<DoctorsEntity> doctors = doctorService.searchDoctorsByHospitalName(keyword);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error searching doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Search doctors by qualification
     * GET /api/doctors/search/qualification?keyword={keyword}
     */
    @GetMapping("/search/qualification")
    public ResponseEntity<?> searchDoctorsByQualification(@RequestParam String keyword) {
        try {
            List<DoctorsEntity> doctors = doctorService.searchDoctorsByQualification(keyword);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error searching doctors by qualification: " + e.getMessage()
            ));
        }
    }

    /**
     * Search doctors by bio content
     * GET /api/doctors/search/bio?keyword={keyword}
     */
    @GetMapping("/search/bio")
    public ResponseEntity<?> searchDoctorsByBio(@RequestParam String keyword) {
        try {
            List<DoctorsEntity> doctors = doctorService.searchDoctorsByBio(keyword);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error searching doctors by bio: " + e.getMessage()
            ));
        }
    }

    // ==================== RECOMMENDATION ENDPOINTS ====================

    /**
     * Get recommended doctors based on criteria
     * GET /api/doctors/recommend?city={city}&specialization={specialization}&maxFee={fee}&minExperience={years}
     */
    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendedDoctors(@RequestParam String city,
                                                 @RequestParam String specialization,
                                                 @RequestParam Double maxFee,
                                                 @RequestParam Integer minExperience) {
        try {
            List<DoctorsEntity> doctors = doctorService.getRecommendedDoctors(city, specialization, maxFee, minExperience);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", doctors.size(),
                "data", doctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error getting recommended doctors: " + e.getMessage()
            ));
        }
    }

    /**
     * Get top doctors by experience in specialization
     * GET /api/doctors/top-by-experience/{specialization}?limit={limit}
     */
    @GetMapping("/top-by-experience/{specialization}")
    public ResponseEntity<?> getTopDoctorsByExperience(@PathVariable String specialization,
                                                      @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<DoctorsEntity> topDoctors = doctorService.getTopDoctorsByExperience(specialization, limit);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", topDoctors.size(),
                "data", topDoctors
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving top doctors: " + e.getMessage()
            ));
        }
    }

    // ==================== PAGINATION ENDPOINTS ====================

    /**
     * Get doctors with pagination
     * GET /api/doctors/page?page={page}&size={size}
     */
    @GetMapping("/page")
    public ResponseEntity<?> getDoctorsWithPagination(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        try {
            Page<DoctorsEntity> doctorsPage = doctorService.getDoctorsWithPagination(page, size);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", doctorsPage.getContent(),
                "totalElements", doctorsPage.getTotalElements(),
                "totalPages", doctorsPage.getTotalPages(),
                "currentPage", doctorsPage.getNumber(),
                "pageSize", doctorsPage.getSize()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors with pagination: " + e.getMessage()
            ));
        }
    }

    /**
     * Get doctors with pagination and sorting
     * GET /api/doctors/page/sort?page={page}&size={size}&sortBy={field}&sortDirection={asc/desc}
     */
    @GetMapping("/page/sort")
    public ResponseEntity<?> getDoctorsWithPaginationAndSorting(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "doctorId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<DoctorsEntity> doctorsPage = doctorService.getDoctorsWithPaginationAndSorting(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", doctorsPage.getContent(),
                "totalElements", doctorsPage.getTotalElements(),
                "totalPages", doctorsPage.getTotalPages(),
                "currentPage", doctorsPage.getNumber(),
                "pageSize", doctorsPage.getSize(),
                "sortBy", sortBy,
                "sortDirection", sortDirection
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error retrieving doctors with sorting: " + e.getMessage()
            ));
        }
    }

    // ==================== UTILITY ENDPOINTS ====================

}