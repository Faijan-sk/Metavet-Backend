package com.example.demo.Entities;

import com.example.demo.Enum.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "pet_id", nullable = false)
    private Long petId;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @Column(name = "doctor_day_id", nullable = false)
    private Long doctorDayId;
    
    @Column(name = "slot_id", nullable = false)
    private Long slotId;
    
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AppointmentStatus status = AppointmentStatus.BOOKED;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Optional: Relationships for fetching complete data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsersEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private DoctorsEntity doctor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DoctorSlots slot;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Appointment() {}
    
    public Appointment(Long userId, Long petId, Long doctorId, Long doctorDayId, 
                      Long slotId, LocalDate appointmentDate) {
        this.userId = userId;
        this.petId = petId;
        this.doctorId = doctorId;
        this.doctorDayId = doctorDayId;
        this.slotId = slotId;
        this.appointmentDate = appointmentDate;
        this.status = AppointmentStatus.BOOKED;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getPetId() {
        return petId;
    }
    
    public void setPetId(Long petId) {
        this.petId = petId;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public Long getDoctorDayId() {
        return doctorDayId;
    }
    
    public void setDoctorDayId(Long doctorDayId) {
        this.doctorDayId = doctorDayId;
    }
    
    public Long getSlotId() {
        return slotId;
    }
    
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }
    
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public UsersEntity getUser() {
        return user;
    }
    
    public void setUser(UsersEntity user) {
        this.user = user;
    }
    
    public DoctorsEntity getDoctor() {
        return doctor;
    }
    
    public void setDoctor(DoctorsEntity doctor) {
        this.doctor = doctor;
    }
    
    public DoctorSlots getSlot() {
        return slot;
    }
    
    public void setSlot(DoctorSlots slot) {
        this.slot = slot;
    }
}