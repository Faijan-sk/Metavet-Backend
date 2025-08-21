package com.example.demo.Dto;

import java.time.LocalTime;
import java.util.List;

public class DoctorAvailabilityResponse {

    private Long id;
    private Long doctorId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;
    private Integer totalSlots;
    private List<SlotResponse> slots;

    // Default constructor
    public DoctorAvailabilityResponse() {
    }

    // All-args constructor
    public DoctorAvailabilityResponse(Long id, Long doctorId, String dayOfWeek, LocalTime startTime, LocalTime endTime,
                                      Integer slotDurationMinutes, Integer totalSlots, List<SlotResponse> slots) {
        this.id = id;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
        this.totalSlots = totalSlots;
        this.slots = slots;
    }

    // Getters and Setters
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
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

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public List<SlotResponse> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotResponse> slots) {
        this.slots = slots;
    }

    // toString
    @Override
    public String toString() {
        return "DoctorAvailabilityResponse{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", slotDurationMinutes=" + slotDurationMinutes +
                ", totalSlots=" + totalSlots +
                ", slots=" + slots +
                '}';
    }

    // Inner static class
    public static class SlotResponse {
        private Long id;
        private LocalTime slotStartTime;
        private LocalTime slotEndTime;
        private String slotStatus;
        private Long patientId;

        // Default constructor
        public SlotResponse() {
        }

        // All-args constructor
        public SlotResponse(Long id, LocalTime slotStartTime, LocalTime slotEndTime, String slotStatus, Long patientId) {
            this.id = id;
            this.slotStartTime = slotStartTime;
            this.slotEndTime = slotEndTime;
            this.slotStatus = slotStatus;
            this.patientId = patientId;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public String getSlotStatus() {
            return slotStatus;
        }

        public void setSlotStatus(String slotStatus) {
            this.slotStatus = slotStatus;
        }

        public Long getPatientId() {
            return patientId;
        }

        public void setPatientId(Long patientId) {
            this.patientId = patientId;
        }

        // toString
        @Override
        public String toString() {
            return "SlotResponse{" +
                    "id=" + id +
                    ", slotStartTime=" + slotStartTime +
                    ", slotEndTime=" + slotEndTime +
                    ", slotStatus='" + slotStatus + '\'' +
                    ", patientId=" + patientId +
                    '}';
        }
    }
}
