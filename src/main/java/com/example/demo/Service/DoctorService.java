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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepo doctorRepository;
    private final UserRepo userRepository;

    public DoctorService(DoctorRepo doctorRepository, UserRepo userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    // Helper for profile status
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

    // ---------------- CREATE (enhanced) ----------------
    @Transactional(rollbackFor = Exception.class)
    public DoctorsEntity createDoctorEnhanced(DoctorsEntity doctor) {

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor data cannot be null");
        }

        if (doctor.getUser() == null || doctor.getUser().getUid() == null) {
            throw new IllegalArgumentException("User information (uid) is required");
        }

        // treat uid as UUID
        UUID userUid = doctor.getUser().getUid();

        Optional<UsersEntity> userOpt = userRepository.findByUid(userUid);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with UID: " + userUid);
        }
        UsersEntity user = userOpt.get();

        if (user.getUserType() == null || user.getUserType() != 2) {
            throw new IllegalArgumentException("User is not registered as a doctor (userType must be 2)");
        }

        if (doctorRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Doctor profile already exists for user UID: " + user.getUid());
        }

        if (Boolean.TRUE.equals(user.isProfileCompleted())) {
            throw new IllegalArgumentException("User profile is already completed. Cannot create duplicate doctor profile.");
        }

        if (doctor.getLicenseNumber() != null && doctorRepository.existsByLicenseNumber(doctor.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists: " + doctor.getLicenseNumber());
        }

        validateDoctorDates(doctor);

        // Attach managed user
        doctor.setUser(user);

        setDefaultValues(doctor);

        validateAndSetProfileStatus(doctor, doctor.getDoctorProfileStatus());

        try {
            DoctorsEntity savedDoctor = doctorRepository.save(doctor);

            user.setProfileCompleted(true);
            userRepository.save(user);

            log.info("Doctor profile created for userUid={} license={} doctorId={}",
                    user.getUid(), doctor.getLicenseNumber(), savedDoctor.getDoctorId());

            return savedDoctor;
        } catch (Exception e) {
            log.error("Failed to create doctor profile for userUid={}. Error: {}", user.getUid(), e.getMessage(), e);
            throw new RuntimeException("Failed to save doctor profile: " + e.getMessage(), e);
        }
    }

    // ---------------- validate dates ----------------
    private void validateDoctorDates(DoctorsEntity doctor) {
        LocalDate today = LocalDate.now();

        if (doctor.getDateOfBirth() != null) {
            if (doctor.getDateOfBirth().isAfter(today)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }
            int age = Period.between(doctor.getDateOfBirth(), today).getYears();
            if (age < 22 || age > 80) {
                throw new IllegalArgumentException("Doctor age must be between 22 and 80 years");
            }
        }

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

        if (doctor.getJoiningDate() != null) {
            if (doctor.getJoiningDate().isAfter(today.plusDays(30))) {
                throw new IllegalArgumentException("Joining date cannot be more than 30 days in the future");
            }
        }

        if (doctor.getResignationDate() != null) {
            if (doctor.getJoiningDate() != null &&
                doctor.getResignationDate().isBefore(doctor.getJoiningDate())) {
                throw new IllegalArgumentException("Resignation date cannot be before joining date");
            }
        }
    }

    // ---------------- defaults ----------------
    private void setDefaultValues(DoctorsEntity doctor) {
        if (doctor.getIsActive() == null) {
            doctor.setIsActive(true);
        }
        if (doctor.getIsAvailable() == null) {
            doctor.setIsAvailable(true);
        }
        if (doctor.getCountry() == null || doctor.getCountry().trim().isEmpty()) {
            doctor.setCountry("India");
        }
        if (doctor.getDoctorProfileStatus() == null) {
            doctor.setDoctorProfileStatus(DoctorProfileStatus.PENDING);
        }
    }

    // ---------------- UPDATE by user UID (UUID) ----------------
    @Transactional
    public DoctorsEntity updateDoctorProfile(java.util.UUID userUid, DoctorsEntity doctorRequest) {
        Optional<UsersEntity> userOptional = userRepository.findByUid(userUid);
        if (userOptional.isEmpty()) {
            return null;
        }
        UsersEntity user = userOptional.get();

        if (user.getUserType() == null || user.getUserType() != 2) {
            return null;
        }

        Optional<DoctorsEntity> existingDoctorWithLicense =
                doctorRepository.findByLicenseNumber(doctorRequest.getLicenseNumber());

        if (existingDoctorWithLicense.isPresent() &&
                !existingDoctorWithLicense.get().getUser().getUid().equals(user.getUid())) {
            return null;
        }

        Optional<DoctorsEntity> existingDoctorOptional = doctorRepository.findByUser(user);

        DoctorsEntity doctor;
        if (existingDoctorOptional.isPresent()) {
            doctor = existingDoctorOptional.get();
        } else {
            doctor = new DoctorsEntity();
            doctor.setUser(user);
            doctor.setIsActive(true);
        }

        // Copy fields from request
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

        // Personal info
        doctor.setGender(doctorRequest.getGender());
        doctor.setDateOfBirth(doctorRequest.getDateOfBirth());
        doctor.setEmergencyContactNumber(doctorRequest.getEmergencyContactNumber());

        // Employment
        doctor.setJoiningDate(doctorRequest.getJoiningDate());
        doctor.setEmploymentType(doctorRequest.getEmploymentType());

        // Availability
        if (doctorRequest.getIsAvailable() != null) {
            doctor.setIsAvailable(doctorRequest.getIsAvailable());
        } else {
            doctor.setIsAvailable(true);
        }

        validateAndSetProfileStatus(doctor, doctorRequest.getDoctorProfileStatus());

        return doctorRepository.save(doctor);
    }

    // ---------------- basic CRUD ----------------
    public Optional<DoctorsEntity> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

    public List<DoctorsEntity> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public DoctorsEntity updateDoctor(Long doctorId, DoctorsEntity updatedDoctor) {
        Optional<DoctorsEntity> existingDoctor = doctorRepository.findById(doctorId);
        if (existingDoctor.isPresent()) {
            DoctorsEntity doctor = existingDoctor.get();
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setConsultationFee(updatedDoctor.getConsultationFee());
            doctor.setBio(updatedDoctor.getBio());
            doctor.setIsAvailable(updatedDoctor.getIsAvailable());

            validateAndSetProfileStatus(doctor, updatedDoctor.getDoctorProfileStatus());

            return doctorRepository.save(doctor);
        }
        return null;
    }

    @Transactional
    public boolean deleteDoctor(Long doctorId) {
        if (doctorRepository.existsById(doctorId)) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean softDeleteDoctor(Long doctorId) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setIsActive(false);
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    // -------------- search / filters --------------
    public Optional<DoctorsEntity> getDoctorByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByLicenseNumber(licenseNumber);
    }

    public Optional<DoctorsEntity> getDoctorByUser(UsersEntity user) {
        return doctorRepository.findByUser(user);
    }

    public List<DoctorsEntity> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<DoctorsEntity> getDoctorsByCity(String city) {
        return doctorRepository.findByCity(city);
    }

    public List<DoctorsEntity> getDoctorsByState(String state) {
        return doctorRepository.findByState(state);
    }

    public List<DoctorsEntity> getDoctorsByCountry(String country) {
        return doctorRepository.findByCountry(country);
    }

    public List<DoctorsEntity> getAvailableDoctors() {
        return doctorRepository.findByIsAvailableTrue();
    }

    public List<DoctorsEntity> getUnavailableDoctors() {
        return doctorRepository.findByIsAvailableFalse();
    }

    public List<DoctorsEntity> getActiveDoctors() {
        return doctorRepository.findByIsActiveTrue();
    }

    public List<DoctorsEntity> getInactiveDoctors() {
        return doctorRepository.findByIsActiveFalse();
    }

    public List<DoctorsEntity> getDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatus(status);
    }

    public long countDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.countByDoctorProfileStatus(status);
    }

    @Transactional
    public boolean updateDoctorProfileStatus(Long doctorId, DoctorProfileStatus status) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            validateAndSetProfileStatus(doctor.get(), status);
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    public List<DoctorsEntity> getActiveDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatusAndIsActiveTrue(status);
    }

    public List<DoctorsEntity> getAvailableDoctorsByProfileStatus(DoctorProfileStatus status) {
        return doctorRepository.findByDoctorProfileStatusAndIsAvailableTrue(status);
    }

    public List<DoctorsEntity> getDoctorsByProfileStatusAndSpecialization(DoctorProfileStatus status, String specialization) {
        return doctorRepository.findByDoctorProfileStatusAndSpecialization(status, specialization);
    }

    public List<DoctorsEntity> getDoctorsByProfileStatusAndCity(DoctorProfileStatus status, String city) {
        return doctorRepository.findByDoctorProfileStatusAndCity(status, city);
    }

    public List<DoctorsEntity> getDoctorsWithMinimumExperience(Integer years) {
        return doctorRepository.findByExperienceYearsGreaterThanEqual(years);
    }

    public List<DoctorsEntity> getDoctorsWithExperienceRange(Integer minYears, Integer maxYears) {
        return doctorRepository.findByExperienceYearsBetween(minYears, maxYears);
    }

    public List<DoctorsEntity> getDoctorsWithMaxFee(Double maxFee) {
        return doctorRepository.findByConsultationFeeLessThanEqual(maxFee);
    }

    public List<DoctorsEntity> getDoctorsWithFeeRange(Double minFee, Double maxFee) {
        return doctorRepository.findByConsultationFeeBetween(minFee, maxFee);
    }

    public List<DoctorsEntity> getDoctorsJoinedAfter(LocalDate date) {
        return doctorRepository.findByJoiningDateAfter(date);
    }

    public List<DoctorsEntity> getDoctorsJoinedBefore(LocalDate date) {
        return doctorRepository.findByJoiningDateBefore(date);
    }

    public List<DoctorsEntity> getCurrentlyEmployedDoctors() {
        return doctorRepository.findByResignationDateIsNull();
    }

    public List<DoctorsEntity> getResignedDoctors() {
        return doctorRepository.findByResignationDateIsNotNull();
    }

    public List<DoctorsEntity> getDoctorsByAgeRange(LocalDate startDate, LocalDate endDate) {
        return doctorRepository.findByDateOfBirthBetween(startDate, endDate);
    }

    public List<DoctorsEntity> getDoctorsByGender(Gender gender) {
        return doctorRepository.findByGender(gender);
    }

    public List<DoctorsEntity> getDoctorsByEmploymentType(EmploymentType type) {
        return doctorRepository.findByEmploymentType(type);
    }

    public List<DoctorsEntity> getAvailableDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsAvailableTrue(specialization);
    }

    public List<DoctorsEntity> getAvailableDoctorsInCity(String city) {
        return doctorRepository.findByCityAndIsAvailableTrue(city);
    }

    public List<DoctorsEntity> getDoctorsBySpecializationAndCity(String specialization, String city) {
        return doctorRepository.findBySpecializationAndCity(specialization, city);
    }

    public List<DoctorsEntity> getApprovedAvailableDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsAvailableTrueAndDoctorProfileStatus(
            specialization, DoctorProfileStatus.APPROVED);
    }

    public List<DoctorsEntity> getApprovedAvailableDoctorsInCity(String city) {
        return doctorRepository.findByCityAndIsAvailableTrueAndDoctorProfileStatus(
            city, DoctorProfileStatus.APPROVED);
    }

    public List<DoctorDtoForClient> getAvailableActiveApproved() {
        return doctorRepository.findByIsAvailableTrueAndIsActiveTrueAndDoctorProfileStatus(DoctorProfileStatus.APPROVED)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DoctorDtoForClient convertToDto(DoctorsEntity doctor) {
        DoctorDtoForClient dto = new DoctorDtoForClient();

        dto.setDoctorUid(doctor.getUser().getUid());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        dto.setFirstName(doctor.getUser().getFirstName());
        dto.setLastName(doctor.getUser().getLastName());

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

    public List<DoctorDtoForAdmin> getAllDoctorsForAdmin() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDtoForAdmin)
                .collect(Collectors.toList());
    }

    private DoctorDtoForAdmin convertToDtoForAdmin(DoctorsEntity doctor) {
        DoctorDtoForAdmin dto = new DoctorDtoForAdmin();

        dto.setDoctorUid(doctor.getUser().getUid());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        dto.setFirstName(doctor.getUser().getFirstName());
        dto.setLastName(doctor.getUser().getLastName());
        dto.setCreatedAt(doctor.getUser().getCreatedAt());

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
        dto.setDoctorProfileStatus(doctor.getDoctorProfileStatus());

        return dto;
    }

    public List<DoctorsEntity> getPendingDoctorsForApproval() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.PENDING);
    }

    public List<DoctorsEntity> getApprovedDoctors() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.APPROVED);
    }

    public List<DoctorsEntity> getRejectedDoctors() {
        return getDoctorsByProfileStatus(DoctorProfileStatus.REJECTED);
    }

    @Transactional
    public boolean approveDoctorProfile(Long doctorId, String approvedBy) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setDoctorProfileStatus(DoctorProfileStatus.APPROVED);
            doctor.get().setUpdatedBy(approvedBy);
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean resetDoctorProfileToPending(Long doctorId, String updatedBy) {
        Optional<DoctorsEntity> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            doctor.get().setDoctorProfileStatus(DoctorProfileStatus.PENDING);
            doctor.get().setUpdatedBy(updatedBy);
            doctorRepository.save(doctor.get());
            return true;
        }
        return false;
    }

    public List<DoctorsEntity> getAvailableAndActive() {
        List<DoctorsEntity> doctors = doctorRepository.findByIsAvailableTrueAndIsActiveTrue();
        return doctors.stream()
                .map(d -> d)
                .collect(Collectors.toList());
    }

    // convert user uid -> doctor id (assumes repository custom query expects UUID)
    public Long getDoctorIdByUserUid(UUID uid) {
        if (uid == null) return null;
        return doctorRepository.findDoctorIdByUserUid(uid);
    }
}
