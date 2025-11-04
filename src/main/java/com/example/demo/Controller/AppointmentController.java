package com.example.demo.Controller;

import com.example.demo.Config.SpringSecurityAuditorAware;
import com.example.demo.Entities.Appointment;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Enum.AppointmentStatus;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Service.AppointmentService;
import com.example.demo.Service.DoctorService;

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
	
    @Autowired
    private DoctorService doctorService;

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
            if (!isClient(currentUser)) {
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
     * NEW API: Get appointments for a doctor with optional date and status filters
     * GET /api/appointments/doctor/{doctorId}/filter?date=2025-11-10&status=BOOKED
     */
    @GetMapping("/doctor/{doctorId}/filter")
    public ResponseEntity<?> getDoctorAppointmentsFiltered(
            @PathVariable Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status) {
        try {
            List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId, date, status);
            return ResponseEntity.ok(Map.of(
                    "doctorId", doctorId,
                    "date", date,
                    "status", status,
                    "totalAppointments", appointments.size(),
                    "appointments", appointments
            ));
        } catch (RuntimeException ex) {
            logger.warn("getDoctorAppointmentsFiltered error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("getDoctorAppointmentsFiltered unexpected error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + ex.getMessage()));
        }
    }

    /**
     * ✅ FIXED API: Get appointments for the logged-in doctor (from access token)
     * GET /api/appointments/my-appointments-doctor?date=2025-11-10&status=BOOKED
     *
     * This extracts doctor UID from the access token via SpringSecurityAuditorAware,
     * then converts it to doctorId using DoctorService
     */
    @GetMapping("/my-appointments-doctor")
    public ResponseEntity<?> getMyAppointmentsDoctor(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status) {
        try {
            // 1) Get current logged-in user from token
            Optional<UsersEntity> currentUserOpt = auditorAware.getCurrentAuditor();
            if (currentUserOpt.isEmpty()) {
                logger.warn("Unauthenticated attempt to access doctor appointments");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }
            
            UsersEntity currentUser = currentUserOpt.get();
            Long userUid = currentUser.getUid();
            logger.debug("Fetching appointments for user uid: {}", userUid);
            
            // 2) Convert user uid to doctor id using DoctorService
            Long doctorId = doctorService.getDoctorIdByUserUid(userUid);
            
            if (doctorId == null) {
                logger.warn("Doctor profile not found for user uid: {}", userUid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Doctor profile not found for user uid: " + userUid));
            }
            
            logger.debug("Resolved doctorId {} for userUid {}", doctorId, userUid);
            
            // 3) Fetch appointments using doctorId
            List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId, date, status);
            
            return ResponseEntity.ok(Map.of(
                    "userUid", userUid,
                    "doctorId", doctorId,
                    "date", date != null ? date.toString() : "all",
                    "status", status != null ? status.toString() : "all",
                    "totalAppointments", appointments.size(),
                    "appointments", appointments
            ));
            
        } catch (RuntimeException ex) {
            logger.error("getMyAppointmentsDoctor runtime error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("getMyAppointmentsDoctor unexpected error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error: " + ex.getMessage()));
        }
    }

    /**
     * API 7: Get appointments by status for a user
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
     * API 8: Cancel an appointment
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
     * API 9: Update appointment status
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

    /**
     * ✅ API 10: Get booked appointments
     * GET /api/appointments/booked?doctorId=1&date=2025-11-10
     */
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

    /**
     * ✅ API 11: Get all appointments for logged-in user
     * GET /api/appointments/my-appointments
     */
    @GetMapping("/my-appointments")
    public ResponseEntity<?> getMyAppointments() {
        try {
            // 1) Get current logged-in user from token
            Optional<UsersEntity> currentUserOpt = auditorAware.getCurrentAuditor();
            if (currentUserOpt.isEmpty()) {
                logger.info("Unauthenticated attempt to fetch appointments");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            UsersEntity currentUser = currentUserOpt.get();
            Long userId = currentUser.getUid();

            // 2) Fetch appointments for this user
            List<Appointment> appointments = appointmentService.getUserAppointments(userId);

            // 3) Return all appointments
            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "totalAppointments", appointments.size(),
                    "appointments", appointments
            ));

        } catch (RuntimeException ex) {
            logger.warn("getMyAppointments error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * ✅ API 12: Delete an appointment (Admin/Direct delete)
     * DELETE /api/appointments/{appointmentId}
     *
     * This permanently deletes the appointment from database
     */
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok(Map.of(
                    "message", "Appointment deleted successfully",
                    "appointmentId", appointmentId
            ));
        } catch (RuntimeException ex) {
            logger.warn("deleteAppointment error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * ✅ API 13: Delete own appointment (User delete with ownership verification)
     * DELETE /api/appointments/my-appointments/{appointmentId}
     *
     * This allows users to delete only their own appointments
     * Automatically gets userId from logged-in user
     */
    @DeleteMapping("/my-appointments/{appointmentId}")
    public ResponseEntity<?> deleteMyAppointment(@PathVariable Long appointmentId) {
        try {
            // 1) Get current logged-in user from token
            Optional<UsersEntity> currentUserOpt = auditorAware.getCurrentAuditor();
            if (currentUserOpt.isEmpty()) {
                logger.info("Unauthenticated attempt to delete appointment");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            UsersEntity currentUser = currentUserOpt.get();
            Long userId = currentUser.getUid();

            // 2) Delete appointment with ownership verification
            appointmentService.deleteAppointmentByUser(appointmentId, userId);

            return ResponseEntity.ok(Map.of(
                    "message", "Appointment deleted successfully",
                    "appointmentId", appointmentId,
                    "userId", userId
            ));

        } catch (RuntimeException ex) {
            logger.warn("deleteMyAppointment error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // ----------------- Helper methods -----------------

    /**
     * ✅ IMPROVED: Null-safe check if the user is a doctor.
     * Assumes doctor can be represented by:
     *  - numeric value 2 (Integer/Long) OR
     *  - String "DOCTOR" (case-insensitive)
     */
    private boolean isDoctor(UsersEntity user) {
        if (user == null) {
            logger.debug("isDoctor check: user is null");
            return false;
        }
        
        Object userType = user.getUserType();
        if (userType == null) {
            logger.debug("isDoctor check: userType is null for user {}", user.getUid());
            return false;
        }

        logger.debug("isDoctor check: user {} has userType {} (class: {})", 
                user.getUid(), userType, userType.getClass().getSimpleName());

        // Integer / Long checks
        if (userType instanceof Integer) {
            boolean result = ((Integer) userType).intValue() == 2;
            logger.debug("isDoctor (Integer): {}", result);
            return result;
        }
        if (userType instanceof Long) {
            boolean result = ((Long) userType).longValue() == 2L;
            logger.debug("isDoctor (Long): {}", result);
            return result;
        }

        // String check (e.g., "DOCTOR", "doctor", or "2")
        if (userType instanceof String) {
            String s = ((String) userType).trim();
            if (s.equalsIgnoreCase("doctor")) {
                logger.debug("isDoctor (String match): true");
                return true;
            }
            try {
                boolean result = Integer.parseInt(s) == 2;
                logger.debug("isDoctor (String numeric): {}", result);
                return result;
            } catch (NumberFormatException ignore) {
                logger.debug("isDoctor (String): not a doctor string");
                return false;
            }
        }

        // Enum or other type: try toString match
        try {
            String s = userType.toString();
            if (s.equalsIgnoreCase("doctor")) {
                logger.debug("isDoctor (toString match): true");
                return true;
            }
            try {
                boolean result = Integer.parseInt(s) == 2;
                logger.debug("isDoctor (toString numeric): {}", result);
                return result;
            } catch (NumberFormatException ignore) {
                logger.debug("isDoctor (toString): not a doctor");
                return false;
            }
        } catch (Exception e) {
            logger.error("isDoctor check exception: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Null-safe check if the user is a client (userType == 1)
     */
    private boolean isClient(UsersEntity user) {
        if (user == null) return false;
        Object userType = user.getUserType();
        if (userType == null) return false;

        if (userType instanceof Integer) {
            return ((Integer) userType).intValue() == 1;
        }
        if (userType instanceof Long) {
            return ((Long) userType).longValue() == 1L;
        }
        if (userType instanceof String) {
            String s = ((String) userType).trim();
            if (s.equalsIgnoreCase("client") || s.equalsIgnoreCase("user")) return true;
            try {
                return Integer.parseInt(s) == 1;
            } catch (NumberFormatException ignore) {
                return false;
            }
        }
        try {
            String s = userType.toString();
            if (s.equalsIgnoreCase("client") || s.equalsIgnoreCase("user")) return true;
            try {
                return Integer.parseInt(s) == 1;
            } catch (NumberFormatException ignore) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}