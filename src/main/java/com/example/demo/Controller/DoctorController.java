package com.example.demo.Controller;

import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Service.DoctorService;
import com.example.demo.Enum.EmploymentStatus;
import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@Validated
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new doctors profile
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDoctor(@Valid @RequestBody DoctorsEntity doctor) {
        Map<String, Object> response = new HashMap<>();
        try {
            DoctorsEntity savedDoctor = doctorService.createDoctor(doctor);
            response.put("success", true);
            response.put("message", "Doctor profile created successfully");
            response.put("data", savedDoctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating doctor profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update doctors profile by userId
     */
    @PutMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> updateDoctorProfile(
            @PathVariable Long userId,
            @Valid @RequestBody DoctorsEntity doctorRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            DoctorsEntity updatedDoctor = doctorService.updateDoctorProfile(userId, doctorRequest);
            if (updatedDoctor != null) {
                response.put("success", true);
                response.put("message", "Doctor profile updated successfully");
                response.put("data", updatedDoctor);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "User not found, not a doctor, or license number already exists");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating doctor profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get doctor by ID
     */
    @GetMapping("/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        Optional<DoctorsEntity> doctor = doctorService.getDoctorById(doctorId);
        if (doctor.isPresent()) {
            response.put("success", true);
            response.put("data", doctor.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Get all doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DoctorsEntity> doctors = doctorService.getAllDoctors();
            response.put("success", true);
            response.put("data", doctors);
            response.put("count", doctors.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching doctors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update doctor
     */
    @PutMapping("/{doctorId}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorsEntity updatedDoctor) {
        Map<String, Object> response = new HashMap<>();
        try {
            DoctorsEntity doctor = doctorService.updateDoctor(doctorId, updatedDoctor);
            if (doctor != null) {
                response.put("success", true);
                response.put("message", "Doctor updated successfully");
                response.put("data", doctor);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Doctor not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating doctor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete doctor
     */
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(@PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        boolean deleted = doctorService.deleteDoctor(doctorId);
        if (deleted) {
            response.put("success", true);
            response.put("message", "Doctor deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Soft delete doctor
     */
    @PatchMapping("/{doctorId}/deactivate")
    public ResponseEntity<Map<String, Object>> softDeleteDoctor(@PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        boolean deactivated = doctorService.softDeleteDoctor(doctorId);
        if (deactivated) {
            response.put("success", true);
            response.put("message", "Doctor deactivated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // ==================== SEARCH AND FILTER ENDPOINTS ====================

    /**
     * Get doctor by license number
     */
    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<Map<String, Object>> getDoctorByLicenseNumber(@PathVariable String licenseNumber) {
        Map<String, Object> response = new HashMap<>();
        Optional<DoctorsEntity> doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
        if (doctor.isPresent()) {
            response.put("success", true);
            response.put("data", doctor.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found with license number: " + licenseNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Get doctors by specialization
     */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<Map<String, Object>> getDoctorsBySpecialization(@PathVariable String specialization) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsBySpecialization(specialization);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<Map<String, Object>> getDoctorsByCity(@PathVariable String city) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByCity(city);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by state
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<Map<String, Object>> getDoctorsByState(@PathVariable String state) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByState(state);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<Map<String, Object>> getDoctorsByCountry(@PathVariable String country) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByCountry(country);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    // ==================== AVAILABILITY AND STATUS ENDPOINTS ====================
    /**
     * Get unavailable doctors
     */
    @GetMapping("/unavailable")
    public ResponseEntity<Map<String, Object>> getUnavailableDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getUnavailableDoctors();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get active doctors
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getActiveDoctors();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get inactive doctors
     */
    @GetMapping("/inactive")
    public ResponseEntity<Map<String, Object>> getInactiveDoctors() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getInactiveDoctors();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors with completed profiles
     */
    @GetMapping("/profiles/completed")
    public ResponseEntity<Map<String, Object>> getDoctorsWithCompletedProfiles() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithCompletedProfiles();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors with incomplete profiles
     */
    @GetMapping("/profiles/incomplete")
    public ResponseEntity<Map<String, Object>> getDoctorsWithIncompleteProfiles() {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithIncompleteProfiles();
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    // ==================== EXPERIENCE AND FEE FILTERS ====================

    /**
     * Get doctors with minimum experience
     */
    @GetMapping("/experience/min/{years}")
    public ResponseEntity<Map<String, Object>> getDoctorsWithMinimumExperience(
            @PathVariable @Min(0) @Max(50) Integer years) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithMinimumExperience(years);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors with experience in range
     */
    @GetMapping("/experience/range")
    public ResponseEntity<Map<String, Object>> getDoctorsWithExperienceRange(
            @RequestParam @Min(0) @Max(50) Integer minYears,
            @RequestParam @Min(0) @Max(50) Integer maxYears) {
        Map<String, Object> response = new HashMap<>();
        if (minYears > maxYears) {
            response.put("success", false);
            response.put("message", "Minimum years cannot be greater than maximum years");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithExperienceRange(minYears, maxYears);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors with consultation fee less than or equal to amount
     */
    @GetMapping("/fee/max/{maxFee}")
    public ResponseEntity<Map<String, Object>> getDoctorsWithMaxFee(
            @PathVariable @Min(0) @Max(50000) Double maxFee) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithMaxFee(maxFee);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors with consultation fee in range
     */
    @GetMapping("/fee/range")
    public ResponseEntity<Map<String, Object>> getDoctorsWithFeeRange(
            @RequestParam @Min(0) Double minFee,
            @RequestParam @Min(0) @Max(50000) Double maxFee) {
        Map<String, Object> response = new HashMap<>();
        if (minFee > maxFee) {
            response.put("success", false);
            response.put("message", "Minimum fee cannot be greater than maximum fee");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<DoctorsEntity> doctors = doctorService.getDoctorsWithFeeRange(minFee, maxFee);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    // ==================== ENUM-BASED FILTERS ====================

    /**
     * Get doctors by gender
     */
    @GetMapping("/gender/{gender}")
    public ResponseEntity<Map<String, Object>> getDoctorsByGender(@PathVariable Gender gender) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByGender(gender);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by employment status
     */
    @GetMapping("/employment-status/{status}")
    public ResponseEntity<Map<String, Object>> getDoctorsByEmploymentStatus(@PathVariable EmploymentStatus status) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByEmploymentStatus(status);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by employment type
     */
    @GetMapping("/employment-type/{type}")
    public ResponseEntity<Map<String, Object>> getDoctorsByEmploymentType(@PathVariable EmploymentType type) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByEmploymentType(type);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    // ==================== COMBINATION QUERIES ====================

    /**
     * Get available doctors by specialization
     */
    @GetMapping("/available/specialization/{specialization}")
    public ResponseEntity<Map<String, Object>> getAvailableDoctorsBySpecialization(@PathVariable String specialization) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getAvailableDoctorsBySpecialization(specialization);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get available doctors in a city
     */
    @GetMapping("/available/city/{city}")
    public ResponseEntity<Map<String, Object>> getAvailableDoctorsInCity(@PathVariable String city) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getAvailableDoctorsInCity(city);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by specialization and city
     */
    @GetMapping("/specialization/{specialization}/city/{city}")
    public ResponseEntity<Map<String, Object>> getDoctorsBySpecializationAndCity(
            @PathVariable String specialization, @PathVariable String city) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsBySpecializationAndCity(specialization, city);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Get doctors by employment status and type
     */
    @GetMapping("/employment")
    public ResponseEntity<Map<String, Object>> getDoctorsByEmploymentStatusAndType(
            @RequestParam EmploymentStatus status,
            @RequestParam EmploymentType type) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.getDoctorsByEmploymentStatusAndType(status, type);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        return ResponseEntity.ok(response);
    }

    // ==================== SEARCH METHODS ====================

    /**
     * Search doctors by hospital name
     */
    @GetMapping("/search/hospital")
    public ResponseEntity<Map<String, Object>> searchDoctorsByHospitalName(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.searchDoctorsByHospitalName(keyword);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        response.put("keyword", keyword);
        return ResponseEntity.ok(response);
    }

    /**
     * Search doctors by specialization keyword
     */
    @GetMapping("/search/specialization")
    public ResponseEntity<Map<String, Object>> searchDoctorsBySpecialization(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.searchDoctorsBySpecialization(keyword);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        response.put("keyword", keyword);
        return ResponseEntity.ok(response);
    }

    /**
     * Search doctors by qualification
     */
    @GetMapping("/search/qualification")
    public ResponseEntity<Map<String, Object>> searchDoctorsByQualification(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.searchDoctorsByQualification(keyword);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        response.put("keyword", keyword);
        return ResponseEntity.ok(response);
    }

    /**
     * Search doctors by bio content
     */
    @GetMapping("/search/bio")
    public ResponseEntity<Map<String, Object>> searchDoctorsByBio(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        List<DoctorsEntity> doctors = doctorService.searchDoctorsByBio(keyword);
        response.put("success", true);
        response.put("data", doctors);
        response.put("count", doctors.size());
        response.put("keyword", keyword);
        return ResponseEntity.ok(response);
    }

    // ==================== PAGINATION METHODS ====================

    /**
     * Get all doctors with pagination
     */
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<DoctorsEntity> doctorPage = doctorService.getDoctorsWithPagination(page, size);
            response.put("success", true);
            response.put("data", doctorPage.getContent());
            response.put("currentPage", doctorPage.getNumber());
            response.put("totalPages", doctorPage.getTotalPages());
            response.put("totalElements", doctorPage.getTotalElements());
            response.put("size", doctorPage.getSize());
            response.put("first", doctorPage.isFirst());
            response.put("last", doctorPage.isLast());
            response.put("empty", doctorPage.isEmpty());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching paginated doctors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get doctors by city with pagination
     */
    @GetMapping("/city/{city}/paginated")
    public ResponseEntity<Map<String, Object>> getDoctorsByCityWithPagination(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<DoctorsEntity> doctorPage = doctorService.getDoctorsByCityWithPagination(city, page, size);
            response.put("success", true);
            response.put("data", doctorPage.getContent());
            response.put("currentPage", doctorPage.getNumber());
            response.put("totalPages", doctorPage.getTotalPages());
            response.put("totalElements", doctorPage.getTotalElements());
            response.put("size", doctorPage.getSize());
            response.put("city", city);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching paginated doctors by city: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get doctors by specialization with pagination
     */
    @GetMapping("/specialization/{specialization}/paginated")
    public ResponseEntity<Map<String, Object>> getDoctorsBySpecializationWithPagination(
            @PathVariable String specialization,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<DoctorsEntity> doctorPage = doctorService.getDoctorsBySpecializationWithPagination(specialization, page, size);
            response.put("success", true);
            response.put("data", doctorPage.getContent());
            response.put("currentPage", doctorPage.getNumber());
            response.put("totalPages", doctorPage.getTotalPages());
            response.put("totalElements", doctorPage.getTotalElements());
            response.put("size", doctorPage.getSize());
            response.put("specialization", specialization);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching paginated doctors by specialization: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get doctors with pagination and sorting
     */
    @GetMapping("/paginated/sorted")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "doctorId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<DoctorsEntity> doctorPage = doctorService.getDoctorsWithPaginationAndSorting(page, size, sortBy, sortDirection);
            response.put("success", true);
            response.put("data", doctorPage.getContent());
            response.put("currentPage", doctorPage.getNumber());
            response.put("totalPages", doctorPage.getTotalPages());
            response.put("totalElements", doctorPage.getTotalElements());
            response.put("size", doctorPage.getSize());
            response.put("sortBy", sortBy);
            response.put("sortDirection", sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching sorted paginated doctors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== STATUS UPDATE METHODS ====================

    /**
     * Update doctor availability status
     */
    @PatchMapping("/{doctorId}/availability")
    public ResponseEntity<Map<String, Object>> updateDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam boolean isAvailable) {
        Map<String, Object> response = new HashMap<>();
        boolean updated = doctorService.updateDoctorAvailability(doctorId, isAvailable);
        if (updated) {
            response.put("success", true);
            response.put("message", "Doctor availability updated successfully");
            response.put("doctorId", doctorId);
            response.put("isAvailable", isAvailable);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Update doctor profile completion status
     */
    @PatchMapping("/{doctorId}/profile-completion")
    public ResponseEntity<Map<String, Object>> updateProfileCompletionStatus(
            @PathVariable Long doctorId,
            @RequestParam boolean isCompleted) {
        Map<String, Object> response = new HashMap<>();
        boolean updated = doctorService.updateProfileCompletionStatus(doctorId, isCompleted);
        if (updated) {
            response.put("success", true);
            response.put("message", "Profile completion status updated successfully");
            response.put("doctorId", doctorId);
            response.put("profileCompleted", isCompleted);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Update consultation fee
     */
    @PatchMapping("/{doctorId}/fee")
    public ResponseEntity<Map<String, Object>> updateConsultationFee(
            @PathVariable Long doctorId,
            @RequestParam @Min(0) @Max(50000) Double newFee) {
        Map<String, Object> response = new HashMap<>();
        boolean updated = doctorService.updateConsultationFee(doctorId, newFee);
        if (updated) {
            response.put("success", true);
            response.put("message", "Consultation fee updated successfully");
            response.put("doctorId", doctorId);
            response.put("newFee", newFee);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Update doctor bio
     */
    @PatchMapping("/{doctorId}/bio")
    public ResponseEntity<Map<String, Object>> updateDoctorBio(
            @PathVariable Long doctorId,
            @RequestParam String bio) {
        Map<String, Object> response = new HashMap<>();
        if (bio.length() > 500) {
            response.put("success", false);
            response.put("message", "Bio cannot exceed 500 characters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        boolean updated = doctorService.updateDoctorBio(doctorId, bio);
        if (updated) {
            response.put("success", true);
            response.put("message", "Doctor bio updated successfully");
            response.put("doctorId", doctorId);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Get all unique specializations for active doctors only
     */
    @GetMapping("/specializations/active")
    public ResponseEntity<Map<String, Object>> getActiveSpecializations() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> specializations = doctorService.getActiveSpecializations();
            response.put("success", true);
            response.put("message", "Active specializations retrieved successfully");
            response.put("data", specializations);
            response.put("count", specializations.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching active specializations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all unique specializations for available doctors only
     */
    @GetMapping("/specializations/available")
    public ResponseEntity<Map<String, Object>> getAvailableSpecializations() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> specializations = doctorService.getAvailableSpecializations();
            response.put("success", true);
            response.put("message", "Available specializations retrieved successfully");
            response.put("data", specializations);
            response.put("count", specializations.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching available specializations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
}