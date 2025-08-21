package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entities.DoctorAvailabilityEntity;
import com.example.demo.Enum.DayOfWeek;

import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailabilityEntity, Long> {

    // Find active availability by doctor ID
    List<DoctorAvailabilityEntity> findByDoctorIdAndIsActive(Long doctorId, Boolean isActive);

    // Find all availability by doctor ID
    List<DoctorAvailabilityEntity> findByDoctorId(Long doctorId);

    // Fixed: Correct entity name in query
    @Query("SELECT da FROM DoctorAvailabilityEntity da WHERE da.doctorId = :doctorId AND da.dayOfWeek = :dayOfWeek AND da.isActive = true")
    List<DoctorAvailabilityEntity> findByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId,
                                                              @Param("dayOfWeek") DayOfWeek dayOfWeek);

    // Added @Modifying and @Transactional for delete operation
    @Modifying
    @Transactional
    void deleteByDoctorId(Long doctorId);
    
    // Additional useful queries
    
    // Find by doctor ID and specific day
    List<DoctorAvailabilityEntity> findByDoctorIdAndDayOfWeekAndIsActive(Long doctorId, DayOfWeek dayOfWeek, Boolean isActive);
    
    // Check if doctor has availability on specific day
    @Query("SELECT COUNT(da) > 0 FROM DoctorAvailabilityEntity da WHERE da.doctorId = :doctorId AND da.dayOfWeek = :dayOfWeek AND da.isActive = true")
    boolean existsByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId, @Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    // Find all active doctors with availability
    @Query("SELECT DISTINCT da.doctorId FROM DoctorAvailabilityEntity da WHERE da.isActive = true")
    List<Long> findAllActiveDoctorIds();
}