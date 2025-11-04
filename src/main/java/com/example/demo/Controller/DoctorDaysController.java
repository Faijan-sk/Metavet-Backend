package com.example.demo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.DoctorDayRequest;
import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Service.DoctorDaysService;

@RestController
@RequestMapping("/api/doctor-days")
public class DoctorDaysController {

    @Autowired
    private DoctorDaysService doctorDaysService;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<?> addDoctorDays(
            @PathVariable long doctorId,
            @RequestBody List<DoctorDayRequest> dayRequests) {
        try {
            List<DoctorDays> createdDays = doctorDaysService.createDaysForDoctor(doctorId, dayRequests);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDays);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorDays(@PathVariable long doctorId) {
        try {
            List<DoctorDays> days = doctorDaysService.getDoctorDaysFromDoctor(doctorId);
            return ResponseEntity.ok(days);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * ✅ NEW API: Get DoctorDays objects (with doctorDayId) for a specific day
     * GET /api/doctor-days/day/{day}/details
     * Example: /api/doctor-days/day/TUESDAY/details
     */
    @GetMapping("/day/{day}/details")
    public ResponseEntity<?> getDoctorDaysByDay(@PathVariable DayOfWeek day) {
        try {
            List<DoctorDays> doctorDays = doctorDaysService.getDoctorDaysByDay(day);
            return ResponseEntity.ok(doctorDays);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * API 3: Get all doctors available on a specific day (returns DoctorsEntity only)
     * GET /api/doctor-days/day/{day}
     * Example: /api/doctor-days/day/MONDAY
     */
    @GetMapping("/day/{day}")
    public ResponseEntity<?> getDoctorsByDay(@PathVariable DayOfWeek day) {
        try {
            List<DoctorsEntity> doctors = doctorDaysService.getDoctorsByDay(day);
            return ResponseEntity.ok(doctors);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/day/{day}/specializations")
    public ResponseEntity<?> getSpecializationsByDay(@PathVariable DayOfWeek day) {
        try {
            List<String> specializations = doctorDaysService.getSpecializationsByDay(day);
            return ResponseEntity.ok(specializations);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getDoctorsByDayAndSpecialization(
            @RequestParam DayOfWeek day,
            @RequestParam String specialization) {
        try {
            List<DoctorsEntity> doctors = doctorDaysService.getDoctorsByDayAndSpecialization(day, specialization);
            return ResponseEntity.ok(doctors);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/doctor/{doctorId}/slots")
    public ResponseEntity<?> getDoctorSlots(@PathVariable long doctorId) {
        try {
            List<DoctorSlots> slots = doctorDaysService.getDoctorSlots(doctorId);
            return ResponseEntity.ok(slots);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/doctor-day/{doctorDayId}/slots")
    public ResponseEntity<?> getSlotsForDoctorDay(@PathVariable long doctorDayId) {
        try {
            List<DoctorSlots> slots = doctorDaysService.getSlotsForDoctorDay(doctorDayId);
            return ResponseEntity.ok(slots);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/find-doctors")
    public ResponseEntity<?> findDoctorsByDay(@RequestParam DayOfWeek day) {
        try {
            List<DoctorsEntity> doctors = doctorDaysService.getDoctorsByDay(day);
            return ResponseEntity.ok(doctors);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
    
    @GetMapping("/getDayId/{doctorId}/{day}")
    public ResponseEntity<?> getDayIdByDoctorAndDay(
            @PathVariable("doctorId") long doctorId,
            @PathVariable("day") DayOfWeek day) {
        try {
            Map<String, Long> result = doctorDaysService.getDoctorDayIdByDoctorAndDay(doctorId, day);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}