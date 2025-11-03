package com.example.demo.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.DoctorDayRequest;
import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorSlots;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Repository.DoctorDaysRepo;
import com.example.demo.Repository.DoctorRepo;
import com.example.demo.Repository.DoctorSlotRepo;


@Service
public class DoctorDaysService {

    @Autowired
    private DoctorDaysRepo doctorDaysRepository;

    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private DoctorSlotRepo DoctorSlotsRepository;
    

    // Method to create days with time slots for a doctor
    @Transactional
    public List<DoctorDays> createDaysForDoctor(long doctorId, List<DoctorDayRequest> dayRequests) {
        DoctorsEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        List<DoctorDays> existing = doctorDaysRepository.findByDoctor_DoctorId(doctorId);
        List<DoctorDays> createdDays = new ArrayList<>();

        for (DoctorDayRequest request : dayRequests) {
            // Check if day already exists
            boolean exists = existing.stream()
                    .anyMatch(r -> r.getDayOfWeek() == request.getDayOfWeek());
            
            if (exists) {
                throw new RuntimeException("Day already assigned to doctor: " + request.getDayOfWeek());
            }

            // Validate time
            if (request.getEndTime().isBefore(request.getStartTime()) || 
                request.getEndTime().equals(request.getStartTime())) {
                throw new RuntimeException("End time must be after start time for day: " + request.getDayOfWeek());
            }

            // Create DoctorDay
            DoctorDays doctorDay = new DoctorDays(
                request.getDayOfWeek(), 
                doctor, 
                request.getStartTime(),
                request.getEndTime(), 
                request.getSlotDurationMinutes()
            );
            
            DoctorDays savedDay = doctorDaysRepository.save(doctorDay);

            // Create time slots
            List<DoctorSlots> slots = createTimeSlots(savedDay);
            DoctorSlotsRepository.saveAll(slots);

            createdDays.add(savedDay);
        }

        return createdDays;
    }

    // Helper method to create time slots
    private List<DoctorSlots> createTimeSlots(DoctorDays doctorDay) {
        List<DoctorSlots> slots = new ArrayList<>();
        
        LocalTime currentTime = doctorDay.getStartTime();
        LocalTime endTime = doctorDay.getEndTime();
        int duration = doctorDay.getSlotDurationMinutes();

        while (currentTime.isBefore(endTime)) {
            LocalTime slotEnd = currentTime.plusMinutes(duration);
            
            // Make sure slot doesn't exceed end time
            if (slotEnd.isAfter(endTime)) {
                break;
            }

            DoctorSlots slot = new DoctorSlots(doctorDay, currentTime, slotEnd);
            slots.add(slot);

            currentTime = slotEnd;
        }

        return slots;
    }

    public List<DoctorDays> getDoctorDaysFromDoctor(long doctorId) {
        return doctorDaysRepository.findByDoctor_DoctorId(doctorId);
    }

    public List<DoctorsEntity> getDoctorsByDay(DayOfWeek day) {
        return doctorDaysRepository.findDoctorsByDay(day);
    }

    // Get all slots for a doctor
    public List<DoctorSlots> getDoctorSlotss(long doctorId) {
        return DoctorSlotsRepository.findByDoctorId(doctorId);
    }

    // Get slots for a specific doctor day
    public List<DoctorSlots> getSlotsForDoctorDay(long doctorDayId) {
        return DoctorSlotsRepository.findByDoctorDay_Id(doctorDayId);
    }
}