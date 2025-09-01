package com.example.demo.Repository;

import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Enum.DoctorProfileStatus;
import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DoctorRepo extends JpaRepository<DoctorsEntity, Long> {
    // ---------- BASIC FINDS ----------
    Optional<DoctorsEntity> findByLicenseNumber(String licenseNumber);

    List<DoctorsEntity> findAll();

    Optional<DoctorsEntity> findByUser(UsersEntity user);

    List<DoctorsEntity> findBySpecialization(String specialization);

    List<DoctorsEntity> findByCity(String city);

    List<DoctorsEntity> findByState(String state);

    List<DoctorsEntity> findByCountry(String country);

    // ---------- BOOLEAN FILTERS ----------
    List<DoctorsEntity> findByIsAvailableTrue();
    List<DoctorsEntity> findByIsAvailableFalse();

    List<DoctorsEntity> findByIsActiveTrue();
    List<DoctorsEntity> findByIsActiveFalse();

    List<DoctorsEntity> findByIsAvailableTrueAndIsActiveTrue();

    // ---------- PROFILE STATUS QUERIES (fixed) ----------
    List<DoctorsEntity> findByDoctorProfileStatus(DoctorProfileStatus status);
    long countByDoctorProfileStatus(DoctorProfileStatus status);

    // Combined profile status queries
    List<DoctorsEntity> findByDoctorProfileStatusAndIsActiveTrue(DoctorProfileStatus status);
    List<DoctorsEntity> findByDoctorProfileStatusAndIsAvailableTrue(DoctorProfileStatus status);

    // ---------- RANGE QUERIES ----------
    List<DoctorsEntity> findByExperienceYearsGreaterThanEqual(Integer years);
    List<DoctorsEntity> findByExperienceYearsBetween(Integer min, Integer max);

    List<DoctorsEntity> findByConsultationFeeLessThanEqual(Double fee);
    List<DoctorsEntity> findByConsultationFeeBetween(Double minFee, Double maxFee);

    // ---------- DATE QUERIES ----------
    List<DoctorsEntity> findByJoiningDateAfter(LocalDate date);
    List<DoctorsEntity> findByJoiningDateBefore(LocalDate date);
    List<DoctorsEntity> findByResignationDateIsNull();
    List<DoctorsEntity> findByResignationDateIsNotNull();

    List<DoctorsEntity> findByDateOfBirthBetween(LocalDate start, LocalDate end);

    // ---------- ENUM FILTERS ----------
    List<DoctorsEntity> findByGender(Gender gender);
    List<DoctorsEntity> findByEmploymentType(EmploymentType type);

    // ---------- COMBINATION QUERIES ----------
    List<DoctorsEntity> findBySpecializationAndIsActiveTrueAndIsAvailableTrue(String specialization);
    List<DoctorsEntity> findBySpecializationAndCity(String specialization, String city);
    List<DoctorsEntity> findBySpecializationAndIsAvailableTrue(String specialization);
    List<DoctorsEntity> findByCityAndIsAvailableTrue(String city);

    // ---------- SEARCH-LIKE QUERIES ----------
    List<DoctorsEntity> findByHospitalClinicNameContainingIgnoreCase(String keyword);
    List<DoctorsEntity> findBySpecializationContainingIgnoreCase(String keyword);
    List<DoctorsEntity> findByQualificationContainingIgnoreCase(String keyword);
    List<DoctorsEntity> findByBioContainingIgnoreCase(String keyword);

    // ---------- PAGINATION & SORTING ----------
    Page<DoctorsEntity> findAll(Pageable pageable);
    Page<DoctorsEntity> findByCity(String city, Pageable pageable);
    Page<DoctorsEntity> findBySpecialization(String specialization, Pageable pageable);

    // ---------- EXISTS & COUNT ----------
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByUser(UsersEntity user);

    long countByCity(String city);
    long countBySpecialization(String specialization);

    // ------ FIND SPECIALISATION--------
    @Query("SELECT DISTINCT d.specialization FROM DoctorsEntity d WHERE d.specialization IS NOT NULL AND d.isActive = true ORDER BY d.specialization")
    List<String> findAllUniqueSpecializationsForActiveDoctors();

    @Query("SELECT DISTINCT d.specialization FROM DoctorsEntity d WHERE d.specialization IS NOT NULL AND d.isActive = true AND d.isAvailable = true ORDER BY d.specialization")
    List<String> findAllUniqueSpecializationsForAvailableDoctors();

    @Query("SELECT DISTINCT d.specialization FROM DoctorsEntity d WHERE d.specialization IS NOT NULL ORDER BY d.specialization")
    List<String> findAllUniqueSpecializations();

    // ------ PROFILE STATUS WITH PAGINATION ------
    Page<DoctorsEntity> findByDoctorProfileStatus(DoctorProfileStatus status, Pageable pageable);

    // ------ PROFILE STATUS COMBINATIONS ------
    List<DoctorsEntity> findByDoctorProfileStatusAndSpecialization(DoctorProfileStatus status, String specialization);
    List<DoctorsEntity> findByDoctorProfileStatusAndCity(DoctorProfileStatus status, String city);

    // 🔹 Approved + Available
    List<DoctorsEntity> findBySpecializationAndIsAvailableTrueAndDoctorProfileStatus(String specialization, DoctorProfileStatus status);
    List<DoctorsEntity> findByCityAndIsAvailableTrueAndDoctorProfileStatus(String city, DoctorProfileStatus status);

    // 🔹 Pagination Support
    Page<DoctorsEntity> findBySpecializationAndDoctorProfileStatus(String specialization, DoctorProfileStatus status, Pageable pageable);

    // 🔹 Custom Queries (Unique Specializations)
    @Query("SELECT DISTINCT d.specialization FROM DoctorsEntity d " +
           "WHERE d.isActive = true AND d.doctorProfileStatus = com.example.demo.Enum.DoctorProfileStatus.APPROVED")
    List<String> findAllUniqueSpecializationsForActiveApprovedDoctors();

    @Query("SELECT DISTINCT d.specialization FROM DoctorsEntity d " +
           "WHERE d.isAvailable = true AND d.doctorProfileStatus = com.example.demo.Enum.DoctorProfileStatus.APPROVED")
    List<String> findAllUniqueSpecializationsForAvailableApprovedDoctors();

    // 🔹 Available + Active + Approved Doctors
    List<DoctorsEntity> findByIsAvailableTrueAndIsActiveTrueAndDoctorProfileStatus(DoctorProfileStatus status);

    // 🔹 Example of Top-N Query without Pageable
    List<DoctorsEntity> findTop5BySpecializationAndDoctorProfileStatusOrderByExperienceYearsDesc(
            String specialization, DoctorProfileStatus status
    );
    

}
