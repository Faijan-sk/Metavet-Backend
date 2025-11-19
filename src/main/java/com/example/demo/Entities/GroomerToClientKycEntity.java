package com.example.demo.Entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "groomer_to_client_kyc")
public class GroomerToClientKycEntity extends BaseEntity {
    
    // Store petUid for reference
    @Column(length = 100)
    private String petUid;
    
    // Store userUid (extracted from access token)
    @Column(length = 100)
    private String userUid;

    // ==================== KYC Status ====================
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private KycStatus status = KycStatus.PENDING;

    // ==================== Pet Relationship ====================
    
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private PetsEntity pet;

    // ==================== Step 1: Grooming Preferences ====================
    
    @Column(length = 100)
    private String groomingFrequency; // "Every 4 weeks", "Every 6–8 weeks", etc.
    
    private LocalDate lastGroomingDate;
    
    @Column(length = 500)
    private String preferredStyle;
    
    @Column(length = 1000)
    private String avoidFocusAreas;

    // ==================== Step 2: Health & Safety ====================
    
    @Column(length = 500)
    private String healthConditions; // Comma-separated: "Skin issues,Ear infections,Allergies"
    
    @Column(length = 300)
    private String otherHealthCondition;
    
    private Boolean onMedication;
    
    @Column(length = 1000)
    private String medicationDetails;
    
    private Boolean hadInjuriesSurgery;
    
    @Column(length = 1000)
    private String injurySurgeryDetails;

    // ==================== Step 3: Behavior & Handling ====================
    
    @Column(length = 500)
    private String behaviorIssues; // Comma-separated: "Nervousness/anxiety,Fear of loud tools"
    
    @Column(length = 1000)
    private String calmingMethods;
    
    @Column(length = 1000)
    private String triggers;

    // ==================== Step 4: Services & Scheduling ====================
    
    @Column(length = 500)
    private String services; // Comma-separated: "Full groom (bath + cut),Nail trim"
    
    @Column(length = 300)
    private String otherService;
    
    @Column(length = 100)
    private String groomingLocation; // "Mobile/in-home grooming", "I'll bring my pet to the groomer", etc.
    
    private LocalDate appointmentDate;
    
    private LocalTime appointmentTime;
    
    @Column(length = 2000)
    private String additionalNotes;
    
    @Column(length = 500)
    private String addOns; // Comma-separated: "Scented finish,De-matting,Seasonal accessories"

    // ==================== Enum for KYC Status ====================
    
    public enum KycStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    // ==================== Getters & Setters ====================

    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public KycStatus getStatus() {
        return status;
    }

    public void setStatus(KycStatus status) {
        this.status = status;
    }

    public PetsEntity getPet() {
        return pet;
    }

    public void setPet(PetsEntity pet) {
        this.pet = pet;
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

    public String getHealthConditions() {
        return healthConditions;
    }

    public void setHealthConditions(String healthConditions) {
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

    public String getBehaviorIssues() {
        return behaviorIssues;
    }

    public void setBehaviorIssues(String behaviorIssues) {
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

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
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

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }
}