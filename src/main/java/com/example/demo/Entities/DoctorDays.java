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
public class DoctorDays extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"availableDays", "hibernateLazyInitializer", "handler"})
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

    // Custom getter for doctorDayId in JSON response (use getId() from BaseEntity)
    @JsonProperty("doctorDayId")
    public Long getDoctorDayIdForJson() {
        return this.getId();
    }

    // Custom getter for doctorId in JSON response
    @JsonProperty("doctorId")
    public Long getDoctorIdForJson() {
        return doctor != null ? doctor.getId() : null;
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
    // note: do NOT redeclare id/uid here; inherited from BaseEntity

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

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
