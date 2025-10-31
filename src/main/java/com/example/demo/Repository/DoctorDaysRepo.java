package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;

@Repository
public interface DoctorDaysRepo extends JpaRepository<DoctorDays, Long> {

    /**
     * Kept your original method name for compatibility.
     * Use an explicit JPQL query so Spring doesn't try to parse method name.
     */
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctorId(@Param("doctor") DoctorsEntity doctor);

    /**
     * Alternative convenient methods (you can keep these, they are safe).
     * They use property-path style which relies on your field name 'doctorId' in DoctorsEntity.
     */
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctor(DoctorsEntity doctor);

    // find by primitive doctor id (uses property path to doctor.doctorId)
    List<DoctorDays> findByDoctor_DoctorId(Long doctorId);

    // delete all days for a doctor by primitive id
    void deleteByDoctor_DoctorId(Long doctorId);

    // find rows by day
    List<DoctorDays> findByDayOfWeek(DayOfWeek day);
    
    
 // NEW METHOD - Get all doctors available on a specific day
    @Query("SELECT DISTINCT d.doctor FROM DoctorDays d WHERE d.dayOfWeek = :day")
    List<DoctorsEntity> findDoctorsByDay(@Param("day") DayOfWeek day);
    
}
