package com.example.demo.Controller;

import com.example.demo.Config.SpringSecurityAuditorAware;
import com.example.demo.Entities.Appointment;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Enum.AppointmentStatus;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SpringSecurityAuditorAware auditorAware;

    /**
     * ✅ API 1: Get all doctors available on a specific day
     * GET /api/appointments/doctors/by-day/{day}
     */
    @GetMapping("/doctors/by-day/{day}")
    public ResponseEntity<?> getDoctorsByDay(@PathVariable DayOfWeek day) {
        try {
            List<DoctorsEntity> doctors = appointmentService.getDoctorsByDay(day);
            return ResponseEntity.ok(doctors);
        } catch (RuntimeException ex) {
            logger.warn("getDoctorsByDay error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * ✅ API 2: Get available slots for a doctor on a specific date
     * GET /api/appointments/available-slots?doctorId=1&doctorDayId=5&date=2025-11-10
     */
    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam Long doctorDayId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<DoctorSlots> availableSlots = appointmentService.getAvailableSlots(doctorId, doctorDayId, date);
            return ResponseEntity.ok(availableSlots);
        } catch (RuntimeException ex) {
            logger.warn("getAvailableSlots error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }


    /**
     * ✅ API 3: Book an appointment
     * POST /api/appointments/book
     *
     * Frontend should NOT send userId. Backend will extract logged-in user from token/auditor.
     *
     * Request Body example:
     * {
     *   "petId": 1,
     *   "doctorId": 5,
     *   "doctorDayId": 5,
     *   "slotId": 24,
     *   "appointmentDate": "2025-11-05"
     * }
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> request) {
        try {
            // 1) Get current logged-in user from auditorAware
            Optional<UsersEntity> currentUserOpt = auditorAware.getCurrentAuditor();
            if (currentUserOpt.isEmpty()) {
                logger.info("Unauthenticated attempt to book appointment");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }
            UsersEntity currentUser = currentUserOpt.get();
            Long userId = currentUser.getUid(); // use uid as userId

            // Optional: ensure this user is client (userType == 1)
            if (currentUser.getUserType() != 1) {
                logger.info("User {} attempted booking but is not a client. userType={}", userId, currentUser.getUserType());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only clients can book appointments"));
            }

            // 2) Extract other required fields from request body
            if (request.get("petId") == null || request.get("doctorId") == null
                    || request.get("doctorDayId") == null || request.get("slotId") == null
                    || request.get("appointmentDate") == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Missing required fields. Required: petId, doctorId, doctorDayId, slotId, appointmentDate"));
            }

            Long petId = Long.parseLong(request.get("petId").toString());
            Long doctorId = Long.parseLong(request.get("doctorId").toString());
            Long doctorDayId = Long.parseLong(request.get("doctorDayId").toString());
            Long slotId = Long.parseLong(request.get("slotId").toString());
            LocalDate appointmentDate = LocalDate.parse(request.get("appointmentDate").toString());

            // 3) Call service with extracted userId
            Appointment appointment = appointmentService.bookAppointment(
                    userId, petId, doctorId, doctorDayId, slotId, appointmentDate
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (RuntimeException ex) {
            logger.warn("bookAppointment runtime error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("bookAppointment unexpected error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + ex.getMessage()));
        }
    }

    /**
     * API 4: Get all appointments for a user
     * GET /api/appointments/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAppointments(@PathVariable Long userId) {
        try {
            List<Appointment> appointments = appointmentService.getUserAppointments(userId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException ex) {
            logger.warn("getUserAppointments error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * API 5: Get all appointments for a doctor
     * GET /api/appointments/doctor/{doctorId}
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable Long doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException ex) {
            logger.warn("getDoctorAppointments error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * API 6: Get appointments by status for a user
     * GET /api/appointments/user/{userId}/status/{status}
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getAppointmentsByStatus(
            @PathVariable Long userId,
            @PathVariable AppointmentStatus status) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByStatus(userId, status);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException ex) {
            logger.warn("getAppointmentsByStatus error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * API 7: Cancel an appointment
     * PUT /api/appointments/{appointmentId}/cancel
     */
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            Appointment appointment = appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException ex) {
            logger.warn("cancelAppointment error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * API 8: Update appointment status
     * PUT /api/appointments/{appointmentId}/status
     */
    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> request) {
        try {
            AppointmentStatus status = AppointmentStatus.valueOf(request.get("status"));
            Appointment appointment = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException ex) {
            logger.warn("updateAppointmentStatus error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/booked")
    public ResponseEntity<?> getBookedAppointments(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Appointment> appointments = appointmentService
                    .getBookedAppointmentsByDoctorAndDate(doctorId, date);

            return ResponseEntity.ok(Map.of(
                    "doctorId", doctorId,
                    "date", date,
                    "totalAppointments", appointments.size(),
                    "appointments", appointments
            ));
        } catch (RuntimeException ex) {
            logger.warn("getBookedAppointments error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
