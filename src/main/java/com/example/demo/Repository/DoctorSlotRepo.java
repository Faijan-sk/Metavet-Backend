package com.example.demo.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.SlotStatus;

@Repository
public interface DoctorSlotRepo extends JpaRepository<DoctorSlots, Long> {
    
    // Get all slots for a specific doctor day
    List<DoctorSlots> findByDoctorDay_Id(Long doctorDayId);
    
    // Get all slots for a specific doctor
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.doctor.doctorId = :doctorId")
    List<DoctorSlots> findByDoctorId(@Param("doctorId") Long doctorId);
    
    // Get available slots for a specific doctor on a specific day
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.doctor.doctorId = :doctorId " +
           "AND s.doctorDay.dayOfWeek = :day AND s.status = :status")
    List<DoctorSlots> findAvailableSlots(@Param("doctorId") Long doctorId, 
                                        @Param("day") DayOfWeek day,
                                        @Param("status") SlotStatus status);
    
    // Delete all slots for a doctor day
    void deleteByDoctorDay_Id(Long doctorDayId);
}