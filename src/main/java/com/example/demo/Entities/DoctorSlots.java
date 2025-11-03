package com.example.demo.Entities;

import com.example.demo.Enum.SlotStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_slot")
public class DoctorSlots {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_day_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "slots", "doctor"})
    private DoctorDays doctorDay;

    @Column(name = "slot_start_time", nullable = false)
    private LocalTime slotStartTime;

    @Column(name = "slot_end_time", nullable = false)
    private LocalTime slotEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SlotStatus status = SlotStatus.AVAILABLE;

    // Custom getter for JSON response
    @JsonProperty("doctorDayId")
    public Long getDoctorDayIdForJson() {
        return doctorDay != null ? doctorDay.getId() : null;
    }

    @JsonProperty("doctorId")
    public Long getDoctorIdForJson() {
        return doctorDay != null && doctorDay.getDoctor() != null ? 
               doctorDay.getDoctor().getDoctorId() : null;
    }

    @JsonProperty("dayOfWeek")
    public String getDayOfWeekForJson() {
        return doctorDay != null && doctorDay.getDayOfWeek() != null ? 
               doctorDay.getDayOfWeek().name() : null;
    }

    // Constructors
    public void DoctorSlots() {}

    public DoctorSlots(DoctorDays doctorDay, LocalTime slotStartTime, LocalTime slotEndTime) {
        this.doctorDay = doctorDay;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.status = SlotStatus.AVAILABLE;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoctorDays getDoctorDay() {
        return doctorDay;
    }

    public void setDoctorDay(DoctorDays doctorDay) {
        this.doctorDay = doctorDay;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(LocalTime slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public LocalTime getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(LocalTime slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }
}