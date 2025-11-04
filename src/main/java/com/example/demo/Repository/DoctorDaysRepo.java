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
 * Provides convenient finder methods used across the service layer.
 *
 * Note: the DayOfWeek here is the project's enum: com.example.demo.Enum.DayOfWeek
 */
@Repository
public interface DoctorDaysRepo extends JpaRepository<DoctorDays, Long> {

    /**
     * Find all DoctorDays rows for a given DoctorsEntity.
     * Useful when you already have the Doctor entity.
     */
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctor(@Param("doctor") DoctorsEntity doctor);

    /**
     * Same as findByDoctor (kept for compatibility/readability).
     * If you prefer, you can remove one of the two.
     */
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctorId(@Param("doctor") DoctorsEntity doctor);

    /**
     * Find all DoctorDays by doctorId (joins through doctor relationship).
     * Example: findByDoctor_DoctorId(123L)
     */
    List<DoctorDays> findByDoctor_DoctorId(Long doctorId);

    /**
     * Delete all DoctorDays entries for the given doctorId.
     */
    void deleteByDoctor_DoctorId(Long doctorId);

    /**
     * Find all DoctorDays for a given DayOfWeek (project enum).
     * Example: findByDayOfWeek(DayOfWeek.MONDAY)
     */
    List<DoctorDays> findByDayOfWeek(DayOfWeek day);

    /**
     * Return distinct doctors who are available on the given day.
     * Useful to list doctors working on a particular weekday.
     */
    @Query("SELECT DISTINCT d.doctor FROM DoctorDays d WHERE d.dayOfWeek = :day")
    List<DoctorsEntity> findDoctorsByDay(@Param("day") DayOfWeek day);

    /**
     * Find a single DoctorDays record for a given doctorId and dayOfWeek.
     * This is the method used in your service to get the doctor's schedule for a specific date's DayOfWeek.
     * Returns Optional.empty() if not found.
     *
     * Usage example:
     *   doctorDaysRepository.findFirstByDoctor_DoctorIdAndDayOfWeek(doctorId, DayOfWeek.MONDAY)
     */
    Optional<DoctorDays> findFirstByDoctor_DoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

    /**
     * Convenience: find all DoctorDays for a given doctorId and dayOfWeek (in case there are multiple entries).
     */
    List<DoctorDays> findByDoctor_DoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
}
