package com.example.demo.Dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class GroomerToClientKycRequestDto {

    // ==================== Pet Reference ====================
    
    private String petUid; // UUID string from frontend

    // ==================== Step 1: Grooming Preferences ====================
    
    @NotBlank(message = "Grooming frequency is required")
    private String groomingFrequency; // "Every 4 weeks", "Every 6–8 weeks", etc.
    
    private LocalDate lastGroomingDate;
    
    private String preferredStyle;
    
    private String avoidFocusAreas;

    // ==================== Step 2: Health & Safety ====================
    
    private List<String> healthConditions; // ["Skin issues", "Ear infections", etc.]
    
    private String otherHealthCondition;
    
    private Boolean onMedication;
    
    private String medicationDetails;
    
    private Boolean hadInjuriesSurgery;
    
    private String injurySurgeryDetails;

    // ==================== Step 3: Behavior & Handling ====================
    
    private List<String> behaviorIssues; // ["Nervousness/anxiety", "Fear of loud tools", etc.]
    
    private String calmingMethods;
    
    private String triggers;

    // ==================== Step 4: Services & Scheduling ====================
    
    private List<String> services; // ["Full groom (bath + cut)", "Nail trim", etc.]
    
    private String otherService;
    
    @NotBlank(message = "Grooming location preference is required")
    private String groomingLocation; // "Mobile/in-home grooming", etc.
    
    private LocalDate appointmentDate;
    
    private LocalTime appointmentTime;
    
    private String additionalNotes;
    
    private List<String> addOns; // ["Scented finish", "De-matting", etc.]

    // ==================== Getters & Setters ====================

    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
    }

    public String getGroomingFrequency() {
        return groomingFrequency;
    }

    public void setGroomingFrequency(String groomingFrequency) {
        this.groomingFrequency = groomingFrequency;
    }

    public LocalDate getLastGroomingDate() {
        return lastGroomingDate;
    }

    public void setLastGroomingDate(LocalDate lastGroomingDate) {
        this.lastGroomingDate = lastGroomingDate;
    }

    public String getPreferredStyle() {
        return preferredStyle;
    }

    public void setPreferredStyle(String preferredStyle) {
        this.preferredStyle = preferredStyle;
    }

    public String getAvoidFocusAreas() {
        return avoidFocusAreas;
    }

    public void setAvoidFocusAreas(String avoidFocusAreas) {
        this.avoidFocusAreas = avoidFocusAreas;
    }

    public List<String> getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(List<String> healthConditions) {
        this.healthConditions = healthConditions;
    }

    public String getOtherHealthCondition() {
        return otherHealthCondition;
    }

    public void setOtherHealthCondition(String otherHealthCondition) {
        this.otherHealthCondition = otherHealthCondition;
    }

    public Boolean getOnMedication() {
        return onMedication;
    }

    public void setOnMedication(Boolean onMedication) {
        this.onMedication = onMedication;
    }

    public String getMedicationDetails() {
        return medicationDetails;
    }

    public void setMedicationDetails(String medicationDetails) {
        this.medicationDetails = medicationDetails;
    }

    public Boolean getHadInjuriesSurgery() {
        return hadInjuriesSurgery;
    }

    public void setHadInjuriesSurgery(Boolean hadInjuriesSurgery) {
        this.hadInjuriesSurgery = hadInjuriesSurgery;
    }

    public String getInjurySurgeryDetails() {
        return injurySurgeryDetails;
    }

    public void setInjurySurgeryDetails(String injurySurgeryDetails) {
        this.injurySurgeryDetails = injurySurgeryDetails;
    }

    public List<String> getBehaviorIssues() {
        return behaviorIssues;
    }

    public void setBehaviorIssues(List<String> behaviorIssues) {
        this.behaviorIssues = behaviorIssues;
    }

    public String getCalmingMethods() {
        return calmingMethods;
    }

    public void setCalmingMethods(String calmingMethods) {
        this.calmingMethods = calmingMethods;
    }

    public String getTriggers() {
        return triggers;
    }

    public void setTriggers(String triggers) {
        this.triggers = triggers;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }

    public String getGroomingLocation() {
        return groomingLocation;
    }

    public void setGroomingLocation(String groomingLocation) {
        this.groomingLocation = groomingLocation;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public List<String> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<String> addOns) {
        this.addOns = addOns;
    }
}