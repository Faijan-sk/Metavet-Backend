package com.example.demo.Entities;

import com.example.demo.Enum.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_days_availability")
public class DoctorDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"availableDays", "user", "hibernateLazyInitializer", "handler"})
    private DoctorsEntity doctor;

    // Custom getter for JSON response - sirf doctor ID return karega
    @JsonProperty("doctorId")
    public Long getDoctorIdForJson() {
        return doctor != null ? doctor.getDoctorId() : null;
    }

    public DoctorDays() {}

    public DoctorDays(DayOfWeek dayOfWeek, DoctorsEntity doctor) {
        this.dayOfWeek = dayOfWeek;
        this.doctor = doctor;
    }

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

    @JsonIgnore  // Yeh add karo
    public DoctorsEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorsEntity doctor) {
        this.doctor = doctor;
    }
}