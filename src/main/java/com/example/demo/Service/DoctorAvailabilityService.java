package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.DoctorAvailabilityRequest;
import com.example.demo.Dto.DoctorAvailabilityResponse;
import com.example.demo.Entities.DoctorAvailabilityEntity;
import com.example.demo.Entities.DoctorSlot;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.SlotStatus;
import com.example.demo.Repository.DoctorAvailabilityRepository;
import com.example.demo.Repository.DoctorSlotRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorAvailabilityService {
    
    @Autowired
    private DoctorAvailabilityRepository availabilityRepo;
    
    @Autowired
    private DoctorSlotRepository slotRepo;
    
    public List<DoctorAvailabilityResponse> createDoctorAvailability(DoctorAvailabilityRequest request) {
        // Input validation
        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        
        if (request.getAvailabilityDays() == null || request.getAvailabilityDays().isEmpty()) {
            throw new IllegalArgumentException("Availability days cannot be null or empty");
        }
        
        if (request.getSlotDurationMinutes() == null || request.getSlotDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Slot duration must be greater than 0");
        }
        
        // Delete existing availability for the doctor
        deleteExistingAvailability(request.getDoctorId());
        
        List<DoctorAvailabilityResponse> responses = new ArrayList<>();
        
        // Fixed: Using correct inner class reference
        for (DoctorAvailabilityRequest.AvailabilityDay day : request.getAvailabilityDays()) {
            // Validate day data
            if (day.getStartTime() == null || day.getEndTime() == null) {
                throw new IllegalArgumentException("Start time and end time cannot be null");
            }
            
            if (day.getStartTime().isAfter(day.getEndTime())) {
                throw new IllegalArgumentException("Start time cannot be after end time");
            }
            
            // Create availability record
            DoctorAvailabilityEntity availability = new DoctorAvailabilityEntity();
            availability.setDoctorId(request.getDoctorId());
            
            try {
                availability.setDayOfWeek(DayOfWeek.valueOf(day.getDayOfWeek().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid day of week: " + day.getDayOfWeek());
            }
            
            availability.setStartTime(day.getStartTime());
            availability.setEndTime(day.getEndTime());
            availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
            availability.setIsActive(true);
            
            availability = availabilityRepo.save(availability);
            
            // Generate slots for this availability
            List<DoctorSlot> slots = generateSlots(availability);
            if (!slots.isEmpty()) {
                slotRepo.saveAll(slots);
            }
            
            // Create response
            DoctorAvailabilityResponse response = mapToResponse(availability, slots);
            responses.add(response);
        }
        
        return responses;
    }
    
    private void deleteExistingAvailability(Long doctorId) {
        try {
            List<DoctorAvailabilityEntity> existingAvailabilities = availabilityRepo.findByDoctorId(doctorId);
            for (DoctorAvailabilityEntity availability : existingAvailabilities) {
                slotRepo.deleteByDoctorAvailabilityId(availability.getId());
            }
            availabilityRepo.deleteByDoctorId(doctorId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting existing availability for doctor: " + doctorId, e);
        }
    }
    
    private List<DoctorSlot> generateSlots(DoctorAvailabilityEntity availability) {
        List<DoctorSlot> slots = new ArrayList<>();
        
        LocalTime currentTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        Integer duration = availability.getSlotDurationMinutes();
        
        // Additional validation
        if (duration <= 0) {
            throw new IllegalArgumentException("Slot duration must be positive");
        }
        
        while (currentTime.isBefore(endTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(duration);
            
            // Check if slot end time exceeds the availability end time
            if (slotEndTime.isAfter(endTime)) {
                break;
            }
            
            DoctorSlot slot = new DoctorSlot();
            slot.setDoctorAvailability(availability);
            slot.setSlotStartTime(currentTime);
            slot.setSlotEndTime(slotEndTime);
            slot.setSlotStatus(SlotStatus.AVAILABLE);
            
            slots.add(slot);
            currentTime = slotEndTime;
        }
        
        return slots;
    }
    
    public List<DoctorAvailabilityResponse> getDoctorAvailability(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        
        try {
            List<DoctorAvailabilityEntity> availabilities = availabilityRepo.findByDoctorIdAndIsActive(doctorId, true);
            
            return availabilities.stream()
                    .map(availability -> {
                        List<DoctorSlot> slots = slotRepo.findByDoctorAvailabilityId(availability.getId());
                        return mapToResponse(availability, slots);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching doctor availability for doctor: " + doctorId, e);
        }
    }
    
    public List<DoctorSlot> getAvailableSlots(Long doctorId, DayOfWeek dayOfWeek) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        
        try {
            return slotRepo.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
                    .stream()
                    .filter(slot -> slot.getSlotStatus() == SlotStatus.AVAILABLE)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching available slots for doctor: " + doctorId + " on " + dayOfWeek, e);
        }
    }
    
    public DoctorSlot bookSlot(Long slotId, Long patientId) {
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID cannot be null");
        }
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        
        try {
            DoctorSlot slot = slotRepo.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Slot not found with ID: " + slotId));
            
            if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
                throw new RuntimeException("Slot is not available. Current status: " + slot.getSlotStatus());
            }
            
            slot.setSlotStatus(SlotStatus.BOOKED);
            slot.setPatientId(patientId);
            
            return slotRepo.save(slot);
        } catch (Exception e) {
            throw new RuntimeException("Error booking slot: " + slotId + " for patient: " + patientId, e);
        }
    }
    
    // Add method to cancel booking
    public DoctorSlot cancelSlot(Long slotId) {
        if (slotId == null) {
            throw new IllegalArgumentException("Slot ID cannot be null");
        }
        
        try {
            DoctorSlot slot = slotRepo.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Slot not found with ID: " + slotId));
            
            if (slot.getSlotStatus() != SlotStatus.BOOKED) {
                throw new RuntimeException("Slot is not booked. Current status: " + slot.getSlotStatus());
            }
            
            slot.setSlotStatus(SlotStatus.AVAILABLE);
            slot.setPatientId(null);
            
            return slotRepo.save(slot);
        } catch (Exception e) {
            throw new RuntimeException("Error cancelling slot: " + slotId, e);
        }
    }
    
    private DoctorAvailabilityResponse mapToResponse(DoctorAvailabilityEntity availability, List<DoctorSlot> slots) {
        DoctorAvailabilityResponse response = new DoctorAvailabilityResponse();
        response.setId(availability.getId());
        response.setDoctorId(availability.getDoctorId());
        response.setDayOfWeek(availability.getDayOfWeek().name());
        response.setStartTime(availability.getStartTime());
        response.setEndTime(availability.getEndTime());
        response.setSlotDurationMinutes(availability.getSlotDurationMinutes());
        response.setTotalSlots(slots != null ? slots.size() : 0);
        
        List<DoctorAvailabilityResponse.SlotResponse> slotResponses = new ArrayList<>();
        if (slots != null) {
            slotResponses = slots.stream()
                    .map(slot -> {
                        DoctorAvailabilityResponse.SlotResponse slotResp = new DoctorAvailabilityResponse.SlotResponse();
                        slotResp.setId(slot.getId());
                        slotResp.setSlotStartTime(slot.getSlotStartTime());
                        slotResp.setSlotEndTime(slot.getSlotEndTime());
                        slotResp.setSlotStatus(slot.getSlotStatus().name());
                        slotResp.setPatientId(slot.getPatientId());
                        return slotResp;
                    })
                    .collect(Collectors.toList());
        }
        
        response.setSlots(slotResponses);
        return response;
    }
}