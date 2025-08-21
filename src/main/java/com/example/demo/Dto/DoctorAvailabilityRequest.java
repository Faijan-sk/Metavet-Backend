package com.example.demo.Dto;

import java.time.LocalTime;
import java.util.List;

public class DoctorAvailabilityRequest {

    private Long doctorId;
    private List<AvailabilityDay> availabilityDays;
    private Integer slotDurationMinutes;

    // Default constructor
    public DoctorAvailabilityRequest() {
    }

    // All-args constructor
    public DoctorAvailabilityRequest(Long doctorId, List<AvailabilityDay> availabilityDays, Integer slotDurationMinutes) {
        this.doctorId = doctorId;
        this.availabilityDays = availabilityDays;
        this.slotDurationMinutes = slotDurationMinutes;
    }

    // Getters and Setters
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public List<AvailabilityDay> getAvailabilityDays() {
        return availabilityDays;
    }

    public void setAvailabilityDays(List<AvailabilityDay> availabilityDays) {
        this.availabilityDays = availabilityDays;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }

    // toString
    @Override
    public String toString() {
        return "DoctorAvailabilityRequest{" +
                "doctorId=" + doctorId +
                ", availabilityDays=" + availabilityDays +
                ", slotDurationMinutes=" + slotDurationMinutes +
                '}';
    }

    // Inner static class
    public static class AvailabilityDay {
        private String dayOfWeek; // MONDAY, TUESDAY, etc.
        private LocalTime startTime;
        private LocalTime endTime;

        // Default constructor
        public AvailabilityDay() {
        }

        // All-args constructor
        public AvailabilityDay(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters and Setters
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

        // toString
        @Override
        public String toString() {
            return "AvailabilityDay{" +
                    "dayOfWeek='" + dayOfWeek + '\'' +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    '}';
        }
    }
}
