package com.example.demo.Entities;

import java.time.LocalTime;

import com.example.demo.Enum.DayOfWeek;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;


@Entity
@Table(name = "doctor_availability")
public class DoctorAvailabilityEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    	
    // One availability can have multiple slots
    @OneToMany(mappedBy = "doctorAvailability", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DoctorSlot> slots;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<DoctorSlot> getSlots() {
		return slots;
	}

	public void setSlots(List<DoctorSlot> slots) {
		this.slots = slots;
	}
    
    
   

}
