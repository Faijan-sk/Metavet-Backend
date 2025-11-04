package com.example.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_slots")
public class DoctorSlots {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_day_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "slots", "doctor"})
    private DoctorDays doctorDay;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    // Custom getters for JSON response
    @JsonProperty("slotId")
    public Long getSlotIdForJson() {
        return id;
    }
    
    @JsonProperty("doctorDayId")
    public Long getDoctorDayIdForJson() {
        return doctorDay != null ? doctorDay.getId() : null;
    }
    
    // Constructors
    public DoctorSlots() {}
    
    public DoctorSlots(DoctorDays doctorDay, LocalTime startTime, LocalTime endTime) {
        this.doctorDay = doctorDay;
        this.doctorId = doctorDay.getDoctor().getDoctorId();
        this.startTime = startTime;
        this.endTime = endTime;
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
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}