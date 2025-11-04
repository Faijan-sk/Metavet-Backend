package com.example.demo.Service;

import com.example.demo.Entities.Appointment;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.AppointmentStatus;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Repository.AppointmentRepo;
import com.example.demo.Repository.DoctorDaysRepo;
import com.example.demo.Repository.DoctorRepo;
import com.example.demo.Repository.DoctorSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepo appointmentRepository;

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private DoctorDaysRepo doctorDaysRepository;

    @Autowired
    private DoctorSlotRepo doctorSlotRepository;

    /**
     * ✅ STEP 1: Get all doctors available on a specific day
     * Returns doctors with full details
     */
    public List<DoctorsEntity> getDoctorsByDay(DayOfWeek day) {
        if (day == null) {
            throw new RuntimeException("Day cannot be null");
        }
        return doctorDaysRepository.findDoctorsByDay(day);
    }

    /**
     * ✅ STEP 2: Get available slots for a doctor on a specific date
     * Excludes slots that are already booked
     *
     * @param doctorId - Doctor's ID
     * @param doctorDayId - DoctorDay ID (from step 1)
     * @param date - Appointment date
     * @return List of available slots
     */
    public List<DoctorSlots> getAvailableSlots(Long doctorId, Long doctorDayId, LocalDate date) {

        if (doctorId == null || doctorDayId == null || date == null) {
            throw new RuntimeException("Doctor ID, DoctorDay ID, and Date are required");
        }

        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }

        if (!doctorDaysRepository.existsById(doctorDayId)) {
            throw new RuntimeException("Doctor day not found with id: " + doctorDayId);
        }

        // ✅ 1. Get all slots configured for this doctor on that day
        List<DoctorSlots> allSlots = doctorSlotRepository.findByDoctorDay_Id(doctorDayId);

        // ✅ 2. Get all slot IDs already booked for this doctor, day, and date
        List<Appointment> bookedAppointment = getBookedAppointmentsByDoctorAndDate(doctorId, date);

        System.out.println("&&&&&&&&&&&&&&&&&&" + bookedAppointment);

        List<Long> bookedSlotIds = appointmentRepository
                .findByDoctorIdAndDoctorDayIdAndAppointmentDateAndStatus(
                        doctorId, doctorDayId, date, AppointmentStatus.BOOKED)
                .stream()
                .map(Appointment::getSlotId)
                .toList();

        System.out.println(bookedSlotIds + "*****************");

        // ✅ 3. Return only the slots NOT in bookedSlotIds
        return allSlots.stream()
                .filter(slot -> !bookedSlotIds.contains(slot.getId()))
                .collect(Collectors.toList());
    }

    /**
     * ✅ STEP 3: Book an appointment
     * Creates a new appointment entry
     */
    @Transactional
    public Appointment bookAppointment(Long userId, Long petId, Long doctorId,
                                       Long doctorDayId, Long slotId, LocalDate appointmentDate) {
        // Validate all required fields
        validateBookingRequest(userId, petId, doctorId, doctorDayId, slotId, appointmentDate);

        // Check if slot is already booked
        if (appointmentRepository.isSlotBooked(slotId, appointmentDate)) {
            throw new RuntimeException("This slot is already booked for the selected date");
        }

        // Create appointment
        Appointment appointment = new Appointment(
                userId, petId, doctorId, doctorDayId, slotId, appointmentDate
        );

        return appointmentRepository.save(appointment);
    }

    /**
     * Get all appointments for a user
     */
    public List<Appointment> getUserAppointments(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }
        return appointmentRepository.findByUserId(userId);
    }

    /**
     * Get all appointments for a doctor (existing single-arg version)
     */
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        if (doctorId == null) {
            throw new RuntimeException("Doctor ID cannot be null");
        }
        return appointmentRepository.findByDoctorId(doctorId);
    }

    /**
     * NEW: Flexible method with optional date and status filters
     * Does not change existing behavior; just an overload that callers can use
     */
    public List<Appointment> getDoctorAppointments(Long doctorId, LocalDate date, AppointmentStatus status) {
        if (doctorId == null) {
            throw new RuntimeException("Doctor ID cannot be null");
        }
        // validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }

        // No filters -> return all
        if (date == null && status == null) {
            return appointmentRepository.findByDoctorId(doctorId);
        }
        // Only date filter
        if (date != null && status == null) {
            return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        }
        // Only status filter
        if (date == null) { // status != null
            return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
        }
        // Both date and status present
        return appointmentRepository.findByDoctorIdAndAppointmentDateAndStatus(doctorId, date, status);
    }

    /**
     * Get all appointments for a doctor (existing)
     */
    public List<Appointment> getDoctorAppointmentsById(Long doctorId) {
        if (doctorId == null) {
            throw new RuntimeException("Doctor ID cannot be null");
        }
        return appointmentRepository.findByDoctorId(doctorId);
    }

    /**
     * Get appointments by status
     */
    public List<Appointment> getAppointmentsByStatus(Long userId, AppointmentStatus status) {
        if (userId == null || status == null) {
            throw new RuntimeException("User ID and Status are required");
        }
        return appointmentRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Cancel an appointment
     */
    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment is already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    /**
     * ✅ NEW: Delete an appointment permanently
     * Deletes the appointment from database
     */
    @Transactional
    public void deleteAppointment(Long appointmentId) {
        if (appointmentId == null) {
            throw new RuntimeException("Appointment ID cannot be null");
        }

        // Check if appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Delete the appointment
        appointmentRepository.delete(appointment);
    }

    /**
     * ✅ NEW: Delete appointment by ID and verify ownership
     * This ensures only the owner can delete their appointment
     */
    @Transactional
    public void deleteAppointmentByUser(Long appointmentId, Long userId) {
        if (appointmentId == null) {
            throw new RuntimeException("Appointment ID cannot be null");
        }
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }

        // Check if appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Verify ownership
        if (!appointment.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this appointment");
        }

        // Delete the appointment
        appointmentRepository.delete(appointment);
    }

    /**
     * Update appointment status
     */
    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    // ==================== VALIDATION HELPER METHODS ====================

    private void validateBookingRequest(Long userId, Long petId, Long doctorId,
                                        Long doctorDayId, Long slotId, LocalDate appointmentDate) {
        if (userId == null) {
            throw new RuntimeException("User ID cannot be null");
        }
        if (petId == null) {
            throw new RuntimeException("Pet ID cannot be null");
        }
        if (doctorId == null) {
            throw new RuntimeException("Doctor ID cannot be null");
        }
        if (doctorDayId == null) {
            throw new RuntimeException("Doctor Day ID cannot be null");
        }
        if (slotId == null) {
            throw new RuntimeException("Slot ID cannot be null");
        }
        if (appointmentDate == null) {
            throw new RuntimeException("Appointment date cannot be null");
        }

        // Validate date is not in the past
        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book appointment for past dates");
        }

        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }

        // Validate slot exists
        if (!doctorSlotRepository.existsById(slotId)) {
            throw new RuntimeException("Slot not found with id: " + slotId);
        }
    }

    public List<Appointment> getBookedAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        if (doctorId == null || date == null) {
            throw new IllegalArgumentException("Doctor ID and Date are required");
        }

        System.out.println("*^&%$#$%^&*(");
        // Validate doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor not found with id: " + doctorId);
        }

        // Use the improved query with JOIN FETCH
        return appointmentRepository.findBookedAppointmentsByDoctorAndDateWithDetails(doctorId, date);
    }
}
