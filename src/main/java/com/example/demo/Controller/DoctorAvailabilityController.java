package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.DoctorAvailabilityRequest;
import com.example.demo.Dto.DoctorAvailabilityResponse;
import com.example.demo.Entities.DoctorSlot;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Service.DoctorAvailabilityService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor-availability")
@CrossOrigin("*")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService availabilityService;

    @PostMapping("/create")
    public ResponseEntity<?> createAvailability(@Valid @RequestBody DoctorAvailabilityRequest request) {
        try {
            List<DoctorAvailabilityResponse> response = availabilityService.createDoctorAvailability(request);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Doctor availability created successfully");
            successResponse.put("data", response);
            
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAvailability(@PathVariable Long doctorId) {
        try {
            List<DoctorAvailabilityResponse> response = availabilityService.getDoctorAvailability(doctorId);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Doctor availability fetched successfully");
            successResponse.put("data", response);
            
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/available-slots/{doctorId}/{dayOfWeek}")
    public ResponseEntity<?> getAvailableSlots(
            @PathVariable Long doctorId,
            @PathVariable String dayOfWeek) {
        try {
            DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            List<DoctorSlot> slots = availabilityService.getAvailableSlots(doctorId, day);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Available slots fetched successfully");
            successResponse.put("data", slots);
            
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid day of week: " + dayOfWeek);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/book-slot")
    public ResponseEntity<?> bookSlot(@RequestBody Map<String, Long> request) {
        try {
            Long slotId = request.get("slotId");
            Long patientId = request.get("patientId");
            
            if (slotId == null || patientId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "slotId and patientId are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            DoctorSlot bookedSlot = availabilityService.bookSlot(slotId, patientId);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Slot booked successfully");
            successResponse.put("data", bookedSlot);
            
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Additional endpoint for cancelling slots
    @PostMapping("/cancel-slot/{slotId}")
    public ResponseEntity<?> cancelSlot(@PathVariable Long slotId) {
        try {
            DoctorSlot cancelledSlot = availabilityService.cancelSlot(slotId);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Slot cancelled successfully");
            successResponse.put("data", cancelledSlot);
            
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Get all booked slots for a doctor
    @GetMapping("/booked-slots/{doctorId}")
    public ResponseEntity<?> getBookedSlots(@PathVariable Long doctorId) {
        try {
            // This would require adding method in service
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Booked slots fetched successfully");
            successResponse.put("data", "Implementation needed in service");
            
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}