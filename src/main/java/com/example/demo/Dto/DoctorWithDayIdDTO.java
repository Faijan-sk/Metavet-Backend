package com.example.demo.Dto;

import com.example.demo.Entities.DoctorsEntity;

/**
 * DTO to include doctorDayId along with doctor details
 * Use this if you want to return dayId with doctor data
 */
public class DoctorWithDayIdDTO {
    
    private Long doctorDayId;
    private DoctorsEntity doctor;
    
    public DoctorWithDayIdDTO() {}
    
    public DoctorWithDayIdDTO(Long doctorDayId, DoctorsEntity doctor) {
        this.doctorDayId = doctorDayId;
        this.doctor = doctor;
    }
    
    // Getters and Setters
    public Long getDoctorDayId() {
        return doctorDayId;
    }
    
    public void setDoctorDayId(Long doctorDayId) {
        this.doctorDayId = doctorDayId;
    }
    
    public DoctorsEntity getDoctor() {
        return doctor;
    }
    
    public void setDoctor(DoctorsEntity doctor) {
        this.doctor = doctor;
    }
}