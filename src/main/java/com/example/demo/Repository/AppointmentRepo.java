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
    
    // ✅ Find booked slot IDs for a doctor on a specific date
//    @Query("SELECT a.slotId FROM Appointment a WHERE a.doctorId = :doctorId " +
//           "AND a.doctorDayId = :doctorDayId AND a.appointmentDate = :date")
//    List<Long> findBookedSlotIds(@Param("doctorId") Long doctorId,
//                                  @Param("doctorDayId") Long doctorDayId,
//                                  @Param("date") LocalDate date);
    
    // Find appointments by user
    List<Appointment> findByUserId(Long userId);
    
    // Find appointments by pet
    List<Appointment> findByPetId(Long petId);
    
    // Find appointments by slot
    List<Appointment> findBySlotId(Long slotId);
    
    // Check if slot is already booked
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a " +
           "WHERE a.slotId = :slotId AND a.appointmentDate = :date AND a.status = 'BOOKED'")
    boolean isSlotBooked(@Param("slotId") Long slotId, @Param("date") LocalDate date);
    
    // Find appointments by status
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Find user's appointments by status
    List<Appointment> findByUserIdAndStatus(Long userId, AppointmentStatus status);
    
    // Find doctor's appointments by status
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
    

    // ✅ Improved query with JOIN FETCH for complete details
    @Query("SELECT a FROM Appointment a " +
           "LEFT JOIN FETCH a.user " +
           "LEFT JOIN FETCH a.doctor " +
           "LEFT JOIN FETCH a.slot " +
           "WHERE a.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.status = com.example.demo.Enum.AppointmentStatus.BOOKED")
    List<Appointment> findBookedAppointmentsByDoctorAndDateWithDetails(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);


	List<Appointment> findByDoctorId(Long doctorId);
	
	List<Appointment> findByDoctorIdAndDoctorDayIdAndAppointmentDateAndStatus(
	        Long doctorId,
	        Long doctorDayId,
	        LocalDate appointmentDate,
	        AppointmentStatus status
	);
	// add inside AppointmentRepo
	List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(Long doctorId, LocalDate appointmentDate, AppointmentStatus status);

//	List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status); // already present but safe to keep

	
}