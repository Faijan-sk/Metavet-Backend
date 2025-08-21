package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.DoctorSlot;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.SlotStatus;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {
    
    List<DoctorSlot> findByDoctorAvailabilityId(Long availabilityId);
    
    @Query("SELECT ds FROM DoctorSlot ds WHERE ds.doctorAvailability.doctorId = :doctorId AND ds.slotStatus = :status")
    List<DoctorSlot> findByDoctorIdAndSlotStatus(@Param("doctorId") Long doctorId, 
                                                @Param("status") SlotStatus status);
    
    @Query("SELECT ds FROM DoctorSlot ds WHERE ds.doctorAvailability.doctorId = :doctorId AND ds.doctorAvailability.dayOfWeek = :dayOfWeek")
    List<DoctorSlot> findByDoctorIdAndDayOfWeek(@Param("doctorId") Long doctorId, 
                                               @Param("dayOfWeek") DayOfWeek dayOfWeek);
    
    void deleteByDoctorAvailabilityId(Long availabilityId);
}
