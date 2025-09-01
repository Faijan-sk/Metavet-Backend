package com.example.demo.Service;



import com.example.demo.Dto.DoctorDtoForAdmin;
import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.DoctorRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Enum.DoctorProfileStatus;
import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private UserRepo userRepository;

    // ==================== HELPER METHOD FOR PROFILE STATUS VALIDATION ====================
    
    /**
     * Helper method to validate and set doctor profile status
     * Frontend se APPROVED/REJECTED aaye to store hoga, otherwise PENDING default
     */
    private void validateAndSetProfileStatus(DoctorsEntity doctor, DoctorProfileStatus requestedStatus) {
        if (requestedStatus != null && 
            (requestedStatus == DoctorProfileStatus.APPROVED || 
             requestedStatus == DoctorProfileStatus.REJECTED || 
             requestedStatus == DoctorProfileStatus.PENDING)) {
            doctor.setDoctorProfileStatus(requestedStatus);
        } else {
            doctor.setDoctorProfileStatus(DoctorProfileStatus.PENDING);
        }
    }

    // ==================== CRUD OPERATIONS ====================

    
    /**
     * Create new doctor profile with enhanced validation and error handling
     * @param doctor - DoctorsEntity object with all required data
     * @return DoctorsEntity - Saved doctor profile
     * @throws IllegalArgumentException - For validation errors
     * @throws RuntimeException - For database or system errors
     */
    @Transactional(rollbackFor = Exception.class)
    public DoctorsEntity createDoctorEnhanced(DoctorsEntity doctor) {
        
        // 1. Basic validation
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor data cannot be null");
        }
        
        if (doctor.getUser() == null || doctor.getUser().getUid() == null) {
            throw new IllegalArgumentException("User information is required");
        }
        
        // 2. Check if user exists and is valid doctor
        UsersEntity user = doctor.getUser();
        if (user.getUserType() != 2) {
            throw new IllegalArgumentException("User is not registered as a doctor (userType must be 2)");
        }
        
        // 3. Check if user already has doctor profile
        if (doctorRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Doctor profile already exists for user ID: " + user.getUid());
        }
        
        // 4. Check license number uniqueness
        if (doctor.getLicenseNumber() != null && 
            doctorRepository.existsByLicenseNumber(doctor.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists: " + doctor.getLicenseNumber());
        }
        
        // 5. Validate date constraints
        validateDoctorDates(doctor);
        
        // 6. Set default values and timestamps
        setDefaultValues(doctor);
        
        // 7. Profile status validation and setting
        validateAndSetProfileStatus(doctor, doctor.getDoctorProfileStatus());
        
        try {
            // 8. Save doctor profile
            DoctorsEntity savedDoctor = doctorRepository.save(doctor);
            
            // Log successful creation
            System.out.println("Doctor profile created successfully for user: " + user.getUid() + 
                             ", License: " + doctor.getLicenseNumber());
            
            return savedDoctor;
            
        } catch (Exception e) {
            // Log error and re-throw with more context
            System.err.println("Error creating doctor profile for user: " + user.getUid() + 
                              ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to save doctor profile: " + e.getMessage(), e);
        }
    }

    /**
     * Validate date-related fields
     */
    private void validateDoctorDates(DoctorsEntity doctor) {
        LocalDate today = LocalDate.now();
        
        // Date of birth validation
        if (doctor.getDateOfBirth() != null) {
            if (doctor.getDateOfBirth().isAfter(today)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
            
            // Age should be between 22-80 for doctors
            int age = Period.between(doctor.getDateOfBirth(), today).getYears();
            if (age < 22 || age > 80) {
                throw new IllegalArgumentException("Doctor age must be between 22 and 80 years");
            }
        }
        
        // License date validations
        if (doctor.getLicenseIssueDate() != null) {
            if (doctor.getLicenseIssueDate().isAfter(today)) {
                throw new IllegalArgumentException("License issue date cannot be in the future");
            }
        }
        
        if (doctor.getLicenseExpiryDate() != null) {
            if (doctor.getLicenseIssueDate() != null && 
                doctor.getLicenseExpiryDate().isBefore(doctor.getLicenseIssueDate())) {
                throw new IllegalArgumentException("License expiry date cannot be before issue date");
            }
        }
        
        // Joining date validation
        if (doctor.getJoiningDate() != null) {
            if (doctor.getJoiningDate().isAfter(today.plusDays(30))) {
                throw new IllegalArgumentException("Joining date cannot be more than 30 days in the future");
            }
        }
        
        // Resignation date validation
        if (doctor.getResignationDate() != null) {
            if (doctor.getJoiningDate() != null && 
                doctor.getResignationDate().isBefore(doctor.getJoiningDate())) {
                throw new IllegalArgumentException("Resignation date cannot be before joining date");
            }
        }
    }

    /**
     * Set default values for new doctor
     */
    private void setDefaultValues(DoctorsEntity doctor) {
        LocalDateTime now = LocalDateTime.now();
        
        // Set timestamps
        doctor.setCreatedAt(now);
        doctor.setUpdatedAt(now);
        
        // Set default boolean values
        if (doctor.getIsActive() == null) {
            doctor.setIsActive(true);
        }
        
        if (doctor.getIsAvailable() == null) {
            doctor.setIsAvailable(true);
        }
        
        // Set default country if not provided
        if (doctor.getCountry() == null || doctor.getCountry().trim().isEmpty()) {
            doctor.setCountry("India");
        }
        
        // Set default profile status if not provided
        if (doctor.getDoctorProfileStatus() == null) {
            doctor.setDoctorProfileStatus(DoctorProfileStatus.PENDING);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Create or update doctor profile
     * @param userId - User ID from UsersEntity
     * @param doctorRequest - Doctor profile data
     * @return DoctorsEntity - Saved doctor profile, null if user not found or not a doctor
     */
    @Transactional
    public DoctorsEntity updateDoctorProfile(Long userId, DoctorsEntity doctorRequest) {
        // Verify user exists and is a doctor
        Optional<UsersEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null; // User not found
        }

        UsersEntity user = userOptional.get();
        if (user.getUserType() != 2) {
            return null; // User is not a doctor
        }

        // Check if license number already exists for another doctor
        Optional<DoctorsEntity> existingDoctorWithLicense =
                doctorRepository.findByLicenseNumber(doctorRequest.getLicenseNumber());

        if (existingDoctorWithLicense.isPresent() &&
                !existingDoctorWithLicense.get().getUser().getUid().equals(userId)) {
            return null; // License number already exists for another doctor
        }

        // Check if doctor profile already exists for this user
        Optional<DoctorsEntity> existingDoctorOptional = doctorRepository.findByUser(user);

        DoctorsEntity doctor;
        if (existingDoctorOptional.isPresent()) {
            // Update existing doctor profile
            doctor = existingDoctorOptional.get();
        } else {
            // Create new doctor profile
            doctor = new DoctorsEntity();
            doctor.setUser(user);
            doctor.setCreatedAt(LocalDateTime.now());
            doctor.setIsActive(true);
        }

        // Set all doctor fields
        doctor.setSpecialization(doctorRequest.getSpecialization());
        doctor.setLicenseNumber(doctorRequest.getLicenseNumber());
        doctor.setLicenseIssueDate(doctorRequest.getLicenseIssueDate());
        doctor.setLicenseExpiryDate(doctorRequest.getLicenseExpiryDate());
        doctor.setExperienceYears(doctorRequest.getExperienceYears());
        doctor.setQualification(doctorRequest.getQualification());
        doctor.setHospitalClinicName(doctorRequest.getHospitalClinicName());
        doctor.setHospitalClinicAddress(doctorRequest.getHospitalClinicAddress());
        doctor.setPincode(doctorRequest.getPincode());
        doctor.setAddress(doctorRequest.getAddress());
        doctor.setCity(doctorRequest.getCity());
        doctor.setState(doctorRequest.getState());
        doctor.setCountry(doctorRequest.getCountry());
        doctor.setBio(doctorRequest.getBio());
        doctor.setConsultationFee(doctorRequest.getConsultationFee());
        doctor.setPreviousWorkplace(doctorRequest.getPreviousWorkplace());
        
        // Personal Information
        doctor.setGender(doctorRequest.getGender());
        doctor.setDateOfBirth(doctorRequest.getDateOfBirth());
        doctor.setEmergencyContactNumber(doctorRequest.getEmergencyContactNumber());
        
        // Employment Information
        doctor.setJoiningDate(doctorRequest.getJoiningDate());
        doctor.setEmploymentType(doctorRequest.getEmploymentType());

        // Set availability if provided, otherwise default to true
        if (doctorRequest.getIsAvailable() != null) {
            doctor.setIsAvailable(doctorRequest.getIsAvailable());
        } else {
            doctor.setIsAvailable(true);
        }

        // Profile status validation - frontend se value aaye to store hogi
        validateAndSetProfileStatus(doctor, doctorRequest.getDoctorProfileStatus());

        doctor.setUpdatedAt(LocalDateTime.now());

        return doctorRepository.save(doctor);
    }

    /**
     * Create a new doctor profile
     */
    @Transactional
//    public DoctorsEntity createDoctor(DoctorsEntity doctor) {
//        // User validation
//        if (doctor.getUser() == null) {
//            throw new IllegalArgumentException("User is required");
//        }
//        
//        // Check if user is already a doctor
//        if (doctorRepository.existsByUser(doctor.getUser())) {
//            throw new IllegalArgumentException("User already has a doctor profile");
//        }
//        
//        doctor.setCreatedAt(LocalDateTime.now());
//        doctor.setUpdatedAt(LocalDateTime.now());
//        doctor.setIsActive(true);
//        
//        if (doctor.getIsAvailable() == null) {
//            doctor.setIsAvailable(true);
//        }
//        
//        // Profile status validation
//        validateAndSetProfileStatus(doctor, doctor.getDoctorProfileStatus());
//        
//        return doctorRepository.save(doctor);
//    }

    /**
     * Get doctor by ID
     */
    public Optional<DoctorsEntity> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    /**
     * Get all doctors
     */
    public List<DoctorsEntity> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Update doctor
     */
    @Transactional
    public DoctorsEntity updateDoctor(Long doctorId, DoctorsEntity updatedDoctor) {
        Optional<DoctorsEntity> existingDoctor = doctorRepository.findById(doctorId);
        if (existingDoctor.isPresent()) {
            DoctorsEntity doctor = existingDoctor.get();
            // Update fields
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setConsultationFee(updatedDoctor.getConsultationFee());
            doctor.setBio(updatedDoctor.getBio());
            doctor.setIsAvailable(updatedDoctor.getIsAvailable());
            
            // Profile status validation
            validateAndSetProfileStatus(doctor, updatedDoctor.getDoctorProfileStatus());
            
            doctor.setUpdatedAt(LocalDateTime.now());
            return doctorRepository.save(doctor);
        }
        return null;
    }

    /**
     * Delete doctor by ID
     */
    @Transactional
    public boolean deleteDoctor(Long doctorId) {
        if (doctorRepository.existsById(doctorId)) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    /**
     * Soft delete doctor (set isActive to false)
     */
    @Transactional
    public boolean softDeleteDoctor(Long doctorId) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setIsActive(false);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    // ==================== SEARCH AND FILTER METHODS ====================

    /**
     * Find doctor by license number
     */
    public Optional<DoctorsEntity> getDoctorByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber);
    }

    /**
     * Find doctor by user entity
     */
    public Optional<DoctorsEntity> getDoctorByUser(UsersEntity user) {
        return doctorRepository.findByUser(user);
    }

    /**
     * Find doctors by specialization
     */
    public List<DoctorsEntity> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    /**
     * Find doctors by city
     */
    public List<DoctorsEntity> getDoctorsByCity(String city) {
        return doctorRepository.findByCity(city);
    }

    /**
     * Find doctors by state
     */
    public List<DoctorsEntity> getDoctorsByState(String state) {
        return doctorRepository.findByState(state);
    }

    /**
     * Find doctors by country
     */
    public List<DoctorsEntity> getDoctorsByCountry(String country) {
        return doctorRepository.findByCountry(country);
    }

    /**
     * Find available doctors
     */
    public List<DoctorsEntity> getAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue();
    }

    /**
     * Find unavailable doctors
     */
    public List<DoctorsEntity> getUnavailableDoctors() {
        return doctorRepository.findByIsAvailableFalse();
    }

    /**
     * Find active doctors
     */
    public List<DoctorsEntity> getActiveDoctors() {
        return doctorRepository.findByIsActiveTrue();
    }

    /**
     * Find inactive doctors
     */
    public List<DoctorsEntity> getInactiveDoctors() {
        return doctorRepository.findByIsActiveFalse();
    }

    // ==================== DOCTOR PROFILE STATUS METHODS ====================

    /**
     * Get doctors by profile status
     */
    public List<DoctorsEntity> getDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatus(status);
    }

    /**
     * Count doctors by profile status
     */
    public long countDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.countByDoctorProfileStatus(status);
    }

    /**
     * Update doctor profile status
     */
    @Transactional
    public boolean updateDoctorProfileStatus(Long doctorId, DoctorProfileStatus status) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            validateAndSetProfileStatus(doctor.get(), status);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    /**
     * Get active doctors by profile status
     */
    public List<DoctorsEntity> getActiveDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatusAndIsActiveTrue(status);
    }

    /**
     * Get available doctors by profile status
     */
    public List<DoctorsEntity> getAvailableDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatusAndIsAvailableTrue(status);
    }

    /**
     * Get doctors by profile status and specialization
     */
    public List<DoctorsEntity> getDoctorsByProfileStatusAndSpecialization(DoctorProfileStatus status, String specialization) {
        return doctorRepository.findByDoctorProfileStatusAndSpecialization(status, specialization);
    }

    /**
     * Get doctors by profile status and city
     */
    public List<DoctorsEntity> getDoctorsByProfileStatusAndCity(DoctorProfileStatus status, String city) {
        return doctorRepository.findByDoctorProfileStatusAndCity(status, city);
    }

    // ==================== EXPERIENCE AND FEE FILTERS ====================

    /**
     * Find doctors with minimum experience
     */
    public List<DoctorsEntity> getDoctorsWithMinimumExperience(Integer years) {
        return doctorRepository.findByExperienceYearsGreaterThanEqual(years);
    }

    /**
     * Find doctors with experience in range
     */
    public List<DoctorsEntity> getDoctorsWithExperienceRange(Integer minYears, Integer maxYears) {
        return doctorRepository.findByExperienceYearsBetween(minYears, maxYears);
    }

    /**
     * Find doctors with consultation fee less than or equal to amount
     */
    public List<DoctorsEntity> getDoctorsWithMaxFee(Double maxFee) {
        return doctorRepository.findByConsultationFeeLessThanEqual(maxFee);
    }

    /**
     * Find doctors with consultation fee in range
     */
    public List<DoctorsEntity> getDoctorsWithFeeRange(Double minFee, Double maxFee) {
        return doctorRepository.findByConsultationFeeBetween(minFee, maxFee);
    }

    // ==================== DATE-BASED QUERIES ====================

    /**
     * Find doctors who joined after a specific date
     */
    public List<DoctorsEntity> getDoctorsJoinedAfter(LocalDate date) {
        return doctorRepository.findByJoiningDateAfter(date);
    }

    /**
     * Find doctors who joined before a specific date
     */
    public List<DoctorsEntity> getDoctorsJoinedBefore(LocalDate date) {
        return doctorRepository.findByJoiningDateBefore(date);
    }

    /**
     * Find doctors who haven't resigned (active employment)
     */
    public List<DoctorsEntity> getCurrentlyEmployedDoctors() {
        return doctorRepository.findByResignationDateIsNull();
    }

    /**
     * Find doctors who have resigned
     */
    public List<DoctorsEntity> getResignedDoctors() {
        return doctorRepository.findByResignationDateIsNotNull();
    }

    /**
     * Find doctors by age range (based on date of birth)
     */
    public List<DoctorsEntity> getDoctorsByAgeRange(LocalDate startDate, LocalDate endDate) {
        return doctorRepository.findByDateOfBirthBetween(startDate, endDate);
    }

    // ==================== ENUM-BASED FILTERS ====================

    /**
     * Find doctors by gender
     */
    public List<DoctorsEntity> getDoctorsByGender(Gender gender) {
        return doctorRepository.findByGender(gender);
    }

    /**
     * Find doctors by employment type
     */
    public List<DoctorsEntity> getDoctorsByEmploymentType(EmploymentType type) {
        return doctorRepository.findByEmploymentType(type);
    }

    // ==================== COMBINATION QUERIES ====================

    /**
     * Find available doctors by specialization
     */
    public List<DoctorsEntity> getAvailableDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsAvailableTrue(specialization);
    }

    /**
     * Find available doctors in a city
     */
    public List<DoctorsEntity> getAvailableDoctorsInCity(String city) {
        return doctorRepository.findByCityAndIsAvailableTrue(city);
    }

    /**
     * Find doctors by specialization and city
     */
    public List<DoctorsEntity> getDoctorsBySpecializationAndCity(String specialization, String city) {
        return doctorRepository.findBySpecializationAndCity(specialization, city);
    }

    /**
     * Find approved and available doctors by specialization
     */
    public List<DoctorsEntity> getApprovedAvailableDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsAvailableTrueAndDoctorProfileStatus(
            specialization, DoctorProfileStatus.APPROVED);
    }

    /**
     * Find approved and available doctors in a city
     */
    public List<DoctorsEntity> getApprovedAvailableDoctorsInCity(String city) {
        return doctorRepository.findByCityAndIsAvailableTrueAndDoctorProfileStatus(
            city, DoctorProfileStatus.APPROVED);
    }

    // ==================== SEARCH METHODS ====================

    /**
     * Search doctors by hospital/clinic name
     */
    public List<DoctorsEntity> searchDoctorsByHospitalName(String keyword) {
        return doctorRepository.findByHospitalClinicNameContainingIgnoreCase(keyword);
    }

    /**
     * Search doctors by specialization keyword
     */
    public List<DoctorsEntity> searchDoctorsBySpecialization(String keyword) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(keyword);
    }

    /**
     * Search doctors by qualification
     */
    public List<DoctorsEntity> searchDoctorsByQualification(String keyword) {
        return doctorRepository.findByQualificationContainingIgnoreCase(keyword);
    }

    /**
     * Search doctors by bio content
     */
    public List<DoctorsEntity> searchDoctorsByBio(String keyword) {
        return doctorRepository.findByBioContainingIgnoreCase(keyword);
    }

    // ==================== PAGINATION METHODS ====================

    /**
     * Get all doctors with pagination
     */
    public Page<DoctorsEntity> getDoctorsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findAll(pageable);
    }

    /**
     * Get doctors by city with pagination
     */
    public Page<DoctorsEntity> getDoctorsByCityWithPagination(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findByCity(city, pageable);
    }

    /**
     * Get doctors by specialization with pagination
     */
    public Page<DoctorsEntity> getDoctorsBySpecializationWithPagination(String specialization, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findBySpecialization(specialization, pageable);
    }

    /**
     * Get doctors by profile status with pagination
     */
    public Page<DoctorsEntity> getDoctorsByProfileStatusWithPagination(DoctorProfileStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findByDoctorProfileStatus(status, pageable);
    }

    /**
     * Get doctors with pagination and sorting
     */
    public Page<DoctorsEntity> getDoctorsWithPaginationAndSorting(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return doctorRepository.findAll(pageable);
    }

    // ==================== VALIDATION AND UTILITY METHODS ====================

    /**
     * Check if license number exists
     */
    public boolean isLicenseNumberExists(String licenseNumber) {
        return doctorRepository.existsByLicenseNumber(licenseNumber);
    }

    /**
     * Check if user has doctor profile
     */
    public boolean hasUserDoctorProfile(UsersEntity user) {
        return doctorRepository.existsByUser(user);
    }

    /**
     * Count doctors in a city
     */
    public long countDoctorsInCity(String city) {
        return doctorRepository.countByCity(city);
    }

    /**
     * Count doctors by specialization
     */
    public long countDoctorsBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }

    // ==================== STATUS UPDATE METHODS ====================

    /**
     * Update doctor availability status
     */
    @Transactional
    public boolean updateDoctorAvailability(Long doctorId, boolean isAvailable) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setIsAvailable(isAvailable);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    /**
     * Update consultation fee
     */
    @Transactional
    public boolean updateConsultationFee(Long doctorId, Double newFee) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setConsultationFee(newFee);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    /**
     * Update doctor bio
     */
    @Transactional
    public boolean updateDoctorBio(Long doctorId, String bio) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setBio(bio);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    // ==================== BUSINESS LOGIC METHODS ====================

    /**
     * Get recommended doctors based on criteria (only approved profiles)
     */
    public List<DoctorsEntity> getRecommendedDoctors(String city, String specialization, Double maxFee, Integer minExperience) {
        List<DoctorsEntity> doctors = doctorRepository.findBySpecializationAndCity(specialization, city);
        return doctors.stream()
                .filter(doctor -> doctor.getIsAvailable() && doctor.getIsActive())
                .filter(doctor -> doctor.getDoctorProfileStatus() == DoctorProfileStatus.APPROVED)
                .filter(doctor -> doctor.getConsultationFee() <= maxFee)
                .filter(doctor -> doctor.getExperienceYears() >= minExperience)
                .toList();
    }

    /**
     * Get top doctors by experience in a specialization (only approved)
     */
    public List<DoctorsEntity> getTopDoctorsByExperience(String specialization, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "experienceYears"));
        return doctorRepository.findBySpecializationAndDoctorProfileStatus(specialization, DoctorProfileStatus.APPROVED, pageable).getContent();
    }

    /**
     * Get doctors with upcoming license expiry (within next 30 days)
     */
    public List<DoctorsEntity> getDoctorsWithExpiringLicense() {
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysFromNow = now.plusDays(30);
        
        return doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getLicenseExpiryDate() != null)
                .filter(doctor -> doctor.getLicenseExpiryDate().isAfter(now) && 
                               doctor.getLicenseExpiryDate().isBefore(thirtyDaysFromNow))
                .toList();
    }
    
    /**
     * Get specializations of all active and approved doctors 
     */
    public List<String> getActiveApprovedSpecializations() {
        return doctorRepository.findAllUniqueSpecializationsForActiveApprovedDoctors();
    }
    
    /**
     * Get specializations of available and approved doctors
     */
    public List<String> getAvailableApprovedSpecializations() {
        return doctorRepository.findAllUniqueSpecializationsForAvailableApprovedDoctors();
    }
    
    // ==================== CUSTOMISED METHODS ====================
    
    /**
     * Get available, active, and APPROVED doctors for client
     */
    public List<DoctorDtoForClient> getAvailableActiveApproved() {
        return doctorRepository.findByIsAvailableTrueAndIsActiveTrueAndDoctorProfileStatus(DoctorProfileStatus.APPROVED)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private DoctorDtoForClient convertToDto(DoctorsEntity doctor) {
        DoctorDtoForClient dto = new DoctorDtoForClient();

        // UserEntity se data
        dto.setDocotrUid(doctor.getUser().getUid());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        dto.setFirstName(doctor.getUser().getFirstName());
        dto.setLastName(doctor.getUser().getLastName());

        // DoctorEntity se data
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

        return dto;
    }

    /**
     * Get all doctors for admin (including all statuses)
     */
    public List<DoctorDtoForAdmin> getAllDoctorsForAdmin() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDtoForAdmin)
                .toList();
    }

    /**
     * Convert DoctorEntity to DoctorDtoForAdmin
     */
    private DoctorDtoForAdmin convertToDtoForAdmin(DoctorsEntity doctor) {
        DoctorDtoForAdmin dto = new DoctorDtoForAdmin();

        // UserEntity data
        dto.setDoctorUid(doctor.getUser().getUid());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        dto.setFirstName(doctor.getUser().getFirstName());
        dto.setLastName(doctor.getUser().getLastName());
        dto.setCreatedAt(doctor.getUser().getCreatedAt());

        // DoctorEntity data
        dto.setDoctorId(doctor.getDoctorId());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setHospitalClinicAddress(doctor.getHospitalClinicAddress());
        dto.setCity(doctor.getCity());
        dto.setState(doctor.getState());
        dto.setBio(doctor.getBio());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setQualification(doctor.getQualification());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setLicenseIssueDate(doctor.getLicenseIssueDate());
        dto.setLicenseExpiryDate(doctor.getLicenseExpiryDate());
        dto.setJoiningDate(doctor.getJoiningDate());
        dto.setIsActive(doctor.getIsActive());
        dto.setIsAvailable(doctor.getIsAvailable());
        dto.setDoctorProfileStatus(doctor.getDoctorProfileStatus()); // Updated field name

        return dto;
    }

    // ==================== ADMIN SPECIFIC METHODS ====================

    /**
     * Get pending doctors for admin approval
     */
    public List<DoctorsEntity> getPendingDoctorsForApproval() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.PENDING);
    }

    /**
     * Get approved doctors
     */
    public List<DoctorsEntity> getApprovedDoctors() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.APPROVED);
    }

    /**
     * Get rejected doctors
     */
    public List<DoctorsEntity> getRejectedDoctors() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.REJECTED);
    }

    /**
     * Approve doctor profile (Admin use)
     */
    @Transactional
    public boolean approveDoctorProfile(Long doctorId, String approvedBy) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setDoctorProfileStatus(DoctorProfileStatus.APPROVED);
            doctor.get().setUpdatedBy(approvedBy);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    /**
     * Reset doctor profile to pending (Admin use)
     */
    @Transactional
    public boolean resetDoctorProfileToPending(Long doctorId, String updatedBy) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setDoctorProfileStatus(DoctorProfileStatus.PENDING);
            doctor.get().setUpdatedBy(updatedBy);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }
    
 // DoctorServiceImpl.java (Implementation)
    public List<DoctorDtoForClient> getAvailableAndActive() {
        List<DoctorsEntity> doctors = doctorRepository.findByIsAvailableTrueAndIsActiveTrue();

        return doctors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method for mapping entity -> DTO

    
}