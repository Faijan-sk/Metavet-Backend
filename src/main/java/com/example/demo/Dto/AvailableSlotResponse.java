package com.example.demo.Dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Simple DTO to represent an available slot in API responses.
 *
 * <p>This class intentionally:
 * <ul>
 *   <li>Implements {@link Serializable} so it can safely travel across layers.</li>
 *   <li>Uses {@code @JsonInclude(JsonInclude.Include.NON_NULL)} to avoid serializing nulls.</li>
 *   <li>Provides a no-arg constructor (needed by Jackson) and an all-args constructor
 *       used by the service layer.</li>
 *   <li>Overrides {@code equals}/{@code hashCode} for easier testing and collection usage.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailableSlotResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long slotId;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private String status;
    private boolean booked;

    /**
     * No-arg constructor required by Jackson (deserialization).
     */
    public AvailableSlotResponse() {}

    /**
     * All-args constructor used by the service when creating the DTO.
     *
     * @param slotId         id of the slot
     * @param slotStartTime  start time of the slot
     * @param slotEndTime    end time of the slot
     * @param status         textual status (e.g. "AVAILABLE", "BOOKED")
     * @param booked         whether the slot is booked (true/false)
     */
    public AvailableSlotResponse(Long slotId, LocalTime slotStartTime, LocalTime slotEndTime, String status, boolean booked) {
        this.slotId = slotId;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.status = status;
        this.booked = booked;
    }

    // Getters and Setters

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public String getStatus() {
        return status;
    }

    /**
     * Set a short status string. Prefer using constants like "AVAILABLE" or "BOOKED".
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    // equals / hashCode (useful for tests or collection operations)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvailableSlotResponse that = (AvailableSlotResponse) o;
        return booked == that.booked &&
                Objects.equals(slotId, that.slotId) &&
                Objects.equals(slotStartTime, that.slotStartTime) &&
                Objects.equals(slotEndTime, that.slotEndTime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotId, slotStartTime, slotEndTime, status, booked);
    }

    @Override
    public String toString() {
        return "AvailableSlotResponse{" +
                "slotId=" + slotId +
                ", slotStartTime=" + slotStartTime +
                ", slotEndTime=" + slotEndTime +
                ", status='" + status + '\'' +
                ", booked=" + booked +
                '}';
    }
}
