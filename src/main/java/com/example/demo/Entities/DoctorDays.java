package com.example.demo.Entities;

import com.example.demo.Enum.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "doctor_days_availability")
public class DoctorDays {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;
    
    @ManyToOne(fetch = FetchType.EAGER)  // ✅ LAZY se EAGER banaya (details ke liye)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"availableDays", "hibernateLazyInitializer", "handler"})  // ✅ @JsonIgnore remove kiya
    private DoctorsEntity doctor;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes;
    
    @OneToMany(mappedBy = "doctorDay", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DoctorSlots> slots;
    
    // ✅ Custom getter for doctorDayId in JSON response
    @JsonProperty("doctorDayId")
    public Long getDoctorDayIdForJson() {
        return this.id;
    }
    
    // ✅ Custom getter for doctorId in JSON response
    @JsonProperty("doctorId")
    public Long getDoctorIdForJson() {
        return doctor != null ? doctor.getDoctorId() : null;
    }
    
    // Constructors
    public DoctorDays() {}
    
    public DoctorDays(DayOfWeek dayOfWeek, DoctorsEntity doctor, LocalTime startTime, 
                      LocalTime endTime, Integer slotDurationMinutes) {
        this.dayOfWeek = dayOfWeek;
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    // ✅ @JsonIgnore REMOVE kiya - ab doctor details aayengi
    public DoctorsEntity getDoctor() {
        return doctor;
    }
    
    public void setDoctor(DoctorsEntity doctor) {
        this.doctor = doctor;
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
    
    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }
    
    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }
    
    public List<DoctorSlots> getSlots() {
        return slots;
    }
    
    public void setSlots(List<DoctorSlots> slots) {
        this.slots = slots;
    }
}