package com.example.demo.Entities;

import com.example.demo.Enum.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Appointment entity — updated to extend BaseEntity with Pet relationship added.
 *
 * Important changes made:
 * - Removed duplicate @Id and timestamp fields (inherited from BaseEntity).
 * - Removed @PrePersist / @PreUpdate because BaseEntity handles lifecycle timestamps.
 * - Kept all existing relationships and column names so no endpoint or functionality changes.
 * - Added @ManyToOne relationship with PetsEntity to fetch pet details in responses.
 * - Provided small delegating getters/setters for id, createdAt and updatedAt to preserve old usage.
 */
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    // --- domain fields (no duplicate @Id) ---
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

    // --- relationships for fetching complete data (read-only joins) ---
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

    // ✅ NEW: Pet relationship added - fetches pet details using existing pet_id column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "owner", "treatingDoctor"})
    private PetsEntity pet;

    // --- Constructors ---
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

    // --- Delegating getters/setters for id and timestamps (keep backwards compatibility) ---
    // id is stored in BaseEntity
    @JsonProperty("id")
    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    // createdAt / updatedAt delegated to BaseEntity
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        super.setCreatedAt(createdAt);
    }

    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        super.setUpdatedAt(updatedAt);
    }

    // --- Getters and Setters for domain fields ---
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

    // ✅ NEW: Pet getter and setter
    public PetsEntity getPet() {
        return pet;
    }

    public void setPet(PetsEntity pet) {
        this.pet = pet;
    }
}