package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;

/**
 * Repository for DoctorDays entity.
 */
@Repository
public interface DoctorDaysRepo extends JpaRepository<DoctorDays, Long> {

    /**
     * Find all DoctorDays rows for a given DoctorsEntity.
     */
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctor(@Param("doctor") DoctorsEntity doctor);

    /**
     * Find all DoctorDays by doctor id (use doctor.id because id now lives in BaseEntity).
     */
    List<DoctorDays> findByDoctor_Id(Long doctorId);

    /**
     * Delete all DoctorDays entries for the given doctorId.
     */
    void deleteByDoctor_Id(Long doctorId);

    /**
     * Find all DoctorDays for a given DayOfWeek.
     */
    List<DoctorDays> findByDayOfWeek(DayOfWeek day);

    /**
     * Return distinct doctors who are available on the given day.
     */
    @Query("SELECT DISTINCT d.doctor FROM DoctorDays d WHERE d.dayOfWeek = :day")
    List<DoctorsEntity> findDoctorsByDay(@Param("day") DayOfWeek day);

    /**
     * Find a single DoctorDays record for a given doctorId and dayOfWeek.
     */
    Optional<DoctorDays> findFirstByDoctor_IdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    /**
     * Convenience: find all DoctorDays for a given doctorId and dayOfWeek.
     */
    List<DoctorDays> findByDoctor_IdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}
