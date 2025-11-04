package com.example.demo.Dto;


import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class AppointmentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Doctor Day ID is required")
    private Long doctorDayId;

    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    private String notes;

    // Constructors
    public AppointmentRequest() {}

    public AppointmentRequest(Long userId, Long doctorId, Long doctorDayId, 
                            Long slotId, LocalDate appointmentDate) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorDayId = doctorDayId;
        this.slotId = slotId;
        this.appointmentDate = appointmentDate;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}