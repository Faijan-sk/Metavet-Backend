package com.example.demo.Dto;

import java.time.LocalTime;

import com.example.demo.Enum.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CreateSlotsRequest {
    private DayOfWeek day;

    // JSON expects "HH:mm" strings
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    // slot duration in minutes (e.g. 60)
    private Integer slotDurationMinutes;

    // optional: deleteExistingSlots boolean (if true, old slots for same doctor/day will be cleared)
    private Boolean deleteExisting = true;

    public CreateSlotsRequest() {}

    // getters / setters
    public DayOfWeek getDay() { return day; }
    public void setDay(DayOfWeek day) { this.day = day; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Integer getSlotDurationMinutes() { return slotDurationMinutes; }
    public void setSlotDurationMinutes(Integer slotDurationMinutes) { this.slotDurationMinutes = slotDurationMinutes; }

    public Boolean getDeleteExisting() { return deleteExisting; }
    public void setDeleteExisting(Boolean deleteExisting) { this.deleteExisting = deleteExisting; }
}
