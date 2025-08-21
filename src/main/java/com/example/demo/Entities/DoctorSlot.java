package com.example.demo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.time.LocalTime;

import com.example.demo.Enum.SlotStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_slots")
public class DoctorSlot {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "availability_id", nullable = false)
    private DoctorAvailabilityEntity doctorAvailability;
    
    @Column(name = "slot_start_time", nullable = false)
    private LocalTime slotStartTime;
    
    @Column(name = "slot_end_time", nullable = false)
    private LocalTime slotEndTime;
    
    @Column(name = "slot_date")
    private LocalDateTime slotDate; // For specific date slots
    
    @Enumerated(EnumType.STRING)
    @Column(name = "slot_status", nullable = false)
    private SlotStatus slotStatus = SlotStatus.AVAILABLE;
    
    @Column(name = "patient_id")
    private Long patientId; // When slot is booked

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DoctorAvailabilityEntity getDoctorAvailability() {
		return doctorAvailability;
	}

	public void setDoctorAvailability(DoctorAvailabilityEntity doctorAvailability) {
		this.doctorAvailability = doctorAvailability;
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

	public LocalDateTime getSlotDate() {
		return slotDate;
	}

	public void setSlotDate(LocalDateTime slotDate) {
		this.slotDate = slotDate;
	}

	public SlotStatus getSlotStatus() {
		return slotStatus;
	}

	public void setSlotStatus(SlotStatus slotStatus) {
		this.slotStatus = slotStatus;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
    
   

}
