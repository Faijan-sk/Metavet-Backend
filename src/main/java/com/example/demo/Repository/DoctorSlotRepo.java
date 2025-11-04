package com.example.demo.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Enum.DayOfWeek;

@Repository
public interface DoctorSlotRepo extends JpaRepository<DoctorSlots, Long> {
    
    // ✅ PRIMARY METHOD: Find all slots by doctorId directly
    List<DoctorSlots> findByDoctorId(Long doctorId);
    
    // ✅ Find slots by doctorId and order by start time
    List<DoctorSlots> findByDoctorIdOrderByStartTimeAsc(Long doctorId);
    
    // ✅ Count slots by doctorId
    Long countByDoctorId(Long doctorId);
    
    // Find slots by doctor day
    List<DoctorSlots> findByDoctorDay(DoctorDays doctorDay);
    
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.id = :doctorDayId")
    List<DoctorSlots> findByDoctorDay_Id(@Param("doctorDayId") Long doctorDayId);
    
    // ✅ Get slots for a specific doctor on a specific day using doctorId field
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "AND s.doctorDay.dayOfWeek = :day")
    List<DoctorSlots> findByDoctorIdAndDay(@Param("doctorId") Long doctorId, 
                                            @Param("day") DayOfWeek day);
    
    // Get all slots for a specific doctor (using join - backup method)
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.doctor.doctorId = :doctorId")
    List<DoctorSlots> findByDoctorIdViaJoin(@Param("doctorId") Long doctorId);
    
    // Find slots by time range
    @Query("SELECT s FROM DoctorSlots s WHERE s.startTime >= :startTime AND s.endTime <= :endTime")
    List<DoctorSlots> findByTimeRange(@Param("startTime") LocalTime startTime, 
                                      @Param("endTime") LocalTime endTime);
    
    // Find slots for doctor on specific day with time range
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "AND s.doctorDay.dayOfWeek = :day " +
           "AND s.startTime >= :startTime AND s.endTime <= :endTime")
    List<DoctorSlots> findDoctorSlotsByDayAndTimeRange(@Param("doctorId") Long doctorId,
                                                        @Param("day") DayOfWeek day,
                                                        @Param("startTime") LocalTime startTime,
                                                        @Param("endTime") LocalTime endTime);
    
    // Check if slot exists for doctor day with specific time
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM DoctorSlots s " +
           "WHERE s.doctorDay.id = :doctorDayId AND s.startTime = :startTime")
    boolean existsByDoctorDayAndStartTime(@Param("doctorDayId") Long doctorDayId, 
                                          @Param("startTime") LocalTime startTime);
    
    // Find overlapping slots for a doctor day
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.id = :doctorDayId " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<DoctorSlots> findOverlappingSlots(@Param("doctorDayId") Long doctorDayId,
                                           @Param("startTime") LocalTime startTime,
                                           @Param("endTime") LocalTime endTime);
    
    // Count slots by doctor day
    @Query("SELECT COUNT(s) FROM DoctorSlots s WHERE s.doctorDay.id = :doctorDayId")
    Long countSlotsByDoctorDay(@Param("doctorDayId") Long doctorDayId);
    
    // Get slots ordered by start time
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.id = :doctorDayId " +
           "ORDER BY s.startTime ASC")
    List<DoctorSlots> findByDoctorDayIdOrderByStartTime(@Param("doctorDayId") Long doctorDayId);
    
    // Get slots for doctor ordered by day and time (using doctorId field)
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "ORDER BY s.doctorDay.dayOfWeek, s.startTime ASC")
    List<DoctorSlots> findByDoctorIdOrderByDayAndTime(@Param("doctorId") Long doctorId);
    
    // Find specific slot by doctor day and time
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorDay.id = :doctorDayId " +
           "AND s.startTime = :startTime AND s.endTime = :endTime")
    Optional<DoctorSlots> findByDoctorDayAndTime(@Param("doctorDayId") Long doctorDayId,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);
    
    // Find slots with eager loading
    @Query("SELECT s FROM DoctorSlots s " +
           "LEFT JOIN FETCH s.doctorDay dd " +
           "LEFT JOIN FETCH dd.doctor " +
           "WHERE s.id = :slotId")
    Optional<DoctorSlots> findByIdWithDetails(@Param("slotId") Long slotId);
    
    // Get all slots for doctor with details
    @Query("SELECT DISTINCT s FROM DoctorSlots s " +
           "LEFT JOIN FETCH s.doctorDay dd " +
           "LEFT JOIN FETCH dd.doctor " +
           "WHERE s.doctorId = :doctorId " +
           "ORDER BY dd.dayOfWeek, s.startTime")
    List<DoctorSlots> findByDoctorIdWithDetails(@Param("doctorId") Long doctorId);
    
    // Delete all slots for a doctor day
    void deleteByDoctorDay_Id(Long doctorDayId);
    
    // Delete slots by IDs
    @Modifying
    @Query("DELETE FROM DoctorSlots s WHERE s.id IN :ids")
    void deleteByIdIn(@Param("ids") List<Long> ids);
    
    // Find multiple slots by IDs
    List<DoctorSlots> findByIdIn(List<Long> ids);
    
    // Find morning slots (before 12 PM)
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "AND s.startTime < :noonTime")
    List<DoctorSlots> findMorningSlots(@Param("doctorId") Long doctorId, 
                                       @Param("noonTime") LocalTime noonTime);
    
    // Find afternoon slots (12 PM to 5 PM)
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "AND s.startTime >= :afternoonStart AND s.startTime < :eveningStart")
    List<DoctorSlots> findAfternoonSlots(@Param("doctorId") Long doctorId,
                                         @Param("afternoonStart") LocalTime afternoonStart,
                                         @Param("eveningStart") LocalTime eveningStart);
    
    // Find evening slots (after 5 PM)
    @Query("SELECT s FROM DoctorSlots s WHERE s.doctorId = :doctorId " +
           "AND s.startTime >= :eveningStart")
    List<DoctorSlots> findEveningSlots(@Param("doctorId") Long doctorId,
                                       @Param("eveningStart") LocalTime eveningStart);
    
    // Count total slots by doctor
    @Query("SELECT COUNT(s) FROM DoctorSlots s WHERE s.doctorId = :doctorId")
    Long countSlotsByDoctor(@Param("doctorId") Long doctorId);
}