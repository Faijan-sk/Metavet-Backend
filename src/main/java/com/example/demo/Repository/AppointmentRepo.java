package com.example.demo.Repository;

import com.example.demo.Entities.Appointment;
import com.example.demo.Enum.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    
    // Find appointments by doctor and date
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);
    
    // Find appointments by doctor, day and date
    List<Appointment> findByDoctorIdAndDoctorDayIdAndAppointmentDate(
            Long doctorId, Long doctorDayId, LocalDate appointmentDate);
    
    // ✅ UPDATED: Find appointments by user with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.userId = :userId")
    List<Appointment> findByUserId(@Param("userId") Long userId);
    
    // Find appointments by pet
    List<Appointment> findByPetId(Long petId);
    
    // Find appointments by slot
    List<Appointment> findBySlotId(Long slotId);
    
    // Check if slot is already booked
    // Use enum constant in JPQL for stronger typing and to avoid string comparisons
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "WHERE a.slotId = :slotId AND a.appointmentDate = :date AND a.status = com.example.demo.Enum.AppointmentStatus.BOOKED")
    boolean isSlotBooked(@Param("slotId") Long slotId, @Param("date") LocalDate date);
    
    // Find appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // ✅ UPDATED: Find user's appointments by status with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.userId = :userId AND a.status = :status")
    List<Appointment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") AppointmentStatus status);
    
    // ✅ UPDATED: Find doctor's appointments by status with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.doctorId = :doctorId AND a.status = :status")
    List<Appointment> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId, @Param("status") AppointmentStatus status);
    
    // ✅ UPDATED: Improved query with JOIN FETCH for complete details including pet
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.user " +
           "LEFT JOIN FETCH a.doctor " +
           "LEFT JOIN FETCH a.slot " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.status = com.example.demo.Enum.AppointmentStatus.BOOKED")
    List<Appointment> findBookedAppointmentsByDoctorAndDateWithDetails(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);
    
    // ✅ UPDATED: Basic finder by doctor with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.doctorId = :doctorId")
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);
    
    // ✅ UPDATED: Find by doctor, doctorDay, date and status with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.doctorId = :doctorId " +
           "AND a.doctorDayId = :doctorDayId " +
           "AND a.appointmentDate = :appointmentDate " +
           "AND a.status = :status")
    List<Appointment> findByDoctorIdAndDoctorDayIdAndAppointmentDateAndStatus(
            @Param("doctorId") Long doctorId,
            @Param("doctorDayId") Long doctorDayId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("status") AppointmentStatus status
    );
    
    // ✅ UPDATED: Find by doctor, date and status with pet details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.pet " +
           "WHERE a.doctorId = :doctorId " +
           "AND a.appointmentDate = :appointmentDate " +
           "AND a.status = :status")
    List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(
            @Param("doctorId") Long doctorId, 
            @Param("appointmentDate") LocalDate appointmentDate, 
            @Param("status") AppointmentStatus status);
}