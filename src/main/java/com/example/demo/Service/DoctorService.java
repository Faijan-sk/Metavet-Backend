package com.example.demo.Service;



import com.example.demo.Dto.DoctorDtoForAdmin;
import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.DoctorRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Enum.AppointmentStatus;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private UserRepo userRepository;

    // ==================== CRUD OPERATIONS ====================

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
            doctor.setAppointmentStatus(AppointmentStatus.PENDING); // Set default appointment status
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

        // Set appointment status if provided
        if (doctorRequest.getAppointmentStatus() != null) {
            doctor.setAppointmentStatus(doctorRequest.getAppointmentStatus());
        }

        doctor.setUpdatedAt(LocalDateTime.now());

        return doctorRepository.save(doctor);
    }

    /**
     * Create a new doctor profile
     */
    @Transactional
    public DoctorsEntity createDoctor(DoctorsEntity doctor) {
        // User validation
        if (doctor.getUser() == null) {
            throw new IllegalArgumentException("User is required");
        }
        
        // Check if user is already a doctor
        if (doctorRepository.existsByUser(doctor.getUser())) {
            throw new IllegalArgumentException("User already has a doctor profile");
        }
        
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());
        doctor.setIsActive(true);
        
        if (doctor.getIsAvailable() == null) {
            doctor.setIsAvailable(true);
        }
        if (doctor.getAppointmentStatus() == null) {
            doctor.setAppointmentStatus(AppointmentStatus.PENDING);
        }
        
        return doctorRepository.save(doctor);
    }

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
            doctor.setAppointmentStatus(updatedDoctor.getAppointmentStatus());
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

    // ==================== APPOINTMENT STATUS METHODS ====================

    /**
     * Get doctors by appointment status
     */
    public List<DoctorsEntity> getDoctorsByAppointmentStatus(AppointmentStatus status) {
        return doctorRepository.findByAppointmentStatus(status);
    }

    /**
     * Count doctors by appointment status
     */
    public long countDoctorsByAppointmentStatus(AppointmentStatus status) {
        return doctorRepository.countByAppointmentStatus(status);
    }

    /**
     * Update doctor appointment status
     */
    @Transactional
    public boolean updateDoctorAppointmentStatus(Long doctorId, AppointmentStatus status) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setAppointmentStatus(status);
            doctor.get().setUpdatedAt(LocalDateTime.now());
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    /**
     * Get active doctors by appointment status
     */
    public List<DoctorsEntity> getActiveDoctorsByAppointmentStatus(AppointmentStatus status) {
        return doctorRepository.findByAppointmentStatusAndIsActiveTrue(status);
    }

    /**
     * Get available doctors by appointment status
     */
    public List<DoctorsEntity> getAvailableDoctorsByAppointmentStatus(AppointmentStatus status) {
        return doctorRepository.findByAppointmentStatusAndIsAvailableTrue(status);
    }

    /**
     * Get doctors by appointment status and specialization
     */
    public List<DoctorsEntity> getDoctorsByAppointmentStatusAndSpecialization(AppointmentStatus status, String specialization) {
        return doctorRepository.findByAppointmentStatusAndSpecialization(status, specialization);
    }

    /**
     * Get doctors by appointment status and city
     */
    public List<DoctorsEntity> getDoctorsByAppointmentStatusAndCity(AppointmentStatus status, String city) {
        return doctorRepository.findByAppointmentStatusAndCity(status, city);
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
     * Get doctors by appointment status with pagination
     */
    public Page<DoctorsEntity> getDoctorsByAppointmentStatusWithPagination(AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findByAppointmentStatus(status, pageable);
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
     * Get recommended doctors based on criteria
     */
    public List<DoctorsEntity> getRecommendedDoctors(String city, String specialization, Double maxFee, Integer minExperience) {
        List<DoctorsEntity> doctors = doctorRepository.findBySpecializationAndCity(specialization, city);
        return doctors.stream()
                .filter(doctor -> doctor.getIsAvailable() && doctor.getIsActive())
                .filter(doctor -> doctor.getConsultationFee() <= maxFee)
                .filter(doctor -> doctor.getExperienceYears() >= minExperience)
                .toList();
    }

    /**
     * Get top doctors by experience in a specialization
     */
    public List<DoctorsEntity> getTopDoctorsByExperience(String specialization, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "experienceYears"));
        return doctorRepository.findBySpecialization(specialization, pageable).getContent();
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
     * Get Specialization of all active Doctor 
     */
    public List<String> getActiveSpecializations() {
        return doctorRepository.findAllUniqueSpecializationsForActiveDoctors();
    }
    
    public List<String> getAvailableSpecializations() {
        return doctorRepository.findAllUniqueSpecializationsForAvailableDoctors();
    }
    
    // ==================== CUSTOMISED METHODS ====================
    
    public List<DoctorDtoForClient> getAvailableAndActive() {
        return doctorRepository.findByIsAvailableTrueAndIsActiveTrue()
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

    // Get all doctors for admin (including inactive ones)
    public List<DoctorDtoForAdmin> getAllDoctorsForAdmin() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDtoForAdmin)
                .toList();
    }

    // Convert DoctorEntity to DoctorDtoForAdmin
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
        dto.setAppointmentStatus(doctor.getAppointmentStatus()); // Added appointment status

        return dto;
    }
}