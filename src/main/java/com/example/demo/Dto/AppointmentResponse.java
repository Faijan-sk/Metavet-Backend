package com.example.demo.Dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO for appointment responses returned by the API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long appointmentId;
    private Long userId;
    private String userName;
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private DayOfWeek dayOfWeek;
    private LocalDate appointmentDate;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;

    // <-- NEW: slotId (this is what you wanted)
    private Long slotId;

    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private String notes;

    public AppointmentResponse() {}

    // getters & setters

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
