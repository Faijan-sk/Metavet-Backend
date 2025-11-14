package com.example.demo.Entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "walker_to_client_kyc")
public class WalkerToClientKycEntity extends BaseEntity {
    
    // Store petUid for reference (optional, for debugging)
    @Column(length = 100)
    private String petUid;

    // ==================== Pet & Routine Overview ====================
    
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private PetsEntity pet;
    
    @Column(length = 500)
    private String petNames;
    
    @Column(length = 200)
    private String breedType;
    
    private Integer age;
    
    @Column(length = 100)
    private String petSpecies;
    
    @Enumerated(EnumType.STRING)
    private EnergyLevel energyLevel;
    
    @Enumerated(EnumType.STRING)
    private WalkingExperience walkingExperience;
    
    @Enumerated(EnumType.STRING)
    private PreferredWalkType preferredWalkType;
    
    @Column(length = 20)
    private String preferredWalkDuration; // Store as string: "15", "30", "60", "Custom"
    
    private Integer customWalkDuration;
    
    @Column(length = 50)
    private String frequency; // Store as string: "Daily", "Weekly", "As needed", "Other"
    
    @Column(length = 200)
    private String frequencyOther;
    
    @Column(length = 50)
    private String preferredTimeOfDay; // Store as string: "Morning", "Midday", "Evening", "Flexible"
    
    private LocalDate preferredStartDate;

    // ==================== Behavior & Handling ====================
    
    // CHANGED: Now stores comma-separated string instead of List
    @Column(length = 500)
    private String leashBehavior; // Store as comma-separated: "Pulls,Walks nicely,Reactive to other dogs"
    
    @Column(length = 300)
    private String leashBehaviorOther;
    
    @Column(length = 1000)
    private String knownTriggers;
    
    @Column(length = 50)
    private String socialCompatibility; // Store as string: "Friendly", "Solo only", "Unsure"
    
    // CHANGED: Now stores comma-separated string instead of List
    @Column(length = 500)
    private String handlingNotes; // Store as comma-separated: "Needs harness,Wears muzzle"
    
    @Column(length = 300)
    private String handlingNotesOther;
    
    @Column(length = 1000)
    private String comfortingMethods;

    // ==================== Health & Safety ====================
    
    private Boolean medicalConditions;
    
    @Column(length = 1000)
    private String medicalConditionsDetails;
    
    private Boolean medications;
    
    @Column(length = 1000)
    private String medicationsDetails;
    
    @Column(length = 500)
    private String emergencyVetInfo;

    // ==================== Access & Logistics ====================
    
    @Column(length = 50)
    private String startingLocation; // Store as string: "Home", "Apartment", "Workplace", "Other"
    
    @Column(length = 500)
    private String addressMeetingPoint;
    
    @Column(length = 1000)
    private String accessInstructions;
    
    @Column(length = 200)
    private String backupContact;
    
    // CHANGED: Now stores comma-separated string instead of List
    @Column(length = 500)
    private String postWalkPreferences; // Store as comma-separated: "Text update,Photo update,Detailed report"

    // ==================== Services & Add-ons ====================
    
    // CHANGED: Now stores comma-separated string instead of List
    @Column(length = 500)
    private String additionalServices; // Store as comma-separated: "Feeding,Water,Playtime"
    
    @Column(length = 500)
    private String additionalServicesOther;

    // ==================== Consent & Signature ====================
    
    @Column(nullable = false)
    private Boolean consent = false;
    
    @NotBlank
    @Column(length = 200)
    private String signature;
    
    private LocalDate signatureDate;

    // ==================== Enums (Kept for backward compatibility) ====================
    
    public enum EnergyLevel {
        LOW, MEDIUM, HIGH
    }
    
    public enum WalkingExperience {
        BEGINNER, INTERMEDIATE, WELL_TRAINED, REACTIVE
    }
    
    public enum PreferredWalkType {
        SOLO, GROUP, EITHER
    }
    
    // ==================== Getters & Setters ====================

    public PetsEntity getPet() {
        return pet;
    }

    public void setPet(PetsEntity pet) {
        this.pet = pet;
    }

    public String getPetNames() {
        return petNames;
    }

    public void setPetNames(String petNames) {
        this.petNames = petNames;
    }

    public String getBreedType() {
        return breedType;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(EnergyLevel energyLevel) {
        this.energyLevel = energyLevel;
    }

    public WalkingExperience getWalkingExperience() {
        return walkingExperience;
    }

    public void setWalkingExperience(WalkingExperience walkingExperience) {
        this.walkingExperience = walkingExperience;
    }

    public PreferredWalkType getPreferredWalkType() {
        return preferredWalkType;
    }

    public void setPreferredWalkType(PreferredWalkType preferredWalkType) {
        this.preferredWalkType = preferredWalkType;
    }

    public String getPreferredWalkDuration() {
        return preferredWalkDuration;
    }

    public void setPreferredWalkDuration(String preferredWalkDuration) {
        this.preferredWalkDuration = preferredWalkDuration;
    }

    public Integer getCustomWalkDuration() {
        return customWalkDuration;
    }

    public void setCustomWalkDuration(Integer customWalkDuration) {
        this.customWalkDuration = customWalkDuration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyOther() {
        return frequencyOther;
    }

    public void setFrequencyOther(String frequencyOther) {
        this.frequencyOther = frequencyOther;
    }

    public String getPreferredTimeOfDay() {
        return preferredTimeOfDay;
    }

    public void setPreferredTimeOfDay(String preferredTimeOfDay) {
        this.preferredTimeOfDay = preferredTimeOfDay;
    }

    public LocalDate getPreferredStartDate() {
        return preferredStartDate;
    }

    public void setPreferredStartDate(LocalDate preferredStartDate) {
        this.preferredStartDate = preferredStartDate;
    }

    // CHANGED: Now String instead of List<String>
    public String getLeashBehavior() {
        return leashBehavior;
    }

    public void setLeashBehavior(String leashBehavior) {
        this.leashBehavior = leashBehavior;
    }

    public String getLeashBehaviorOther() {
        return leashBehaviorOther;
    }

    public void setLeashBehaviorOther(String leashBehaviorOther) {
        this.leashBehaviorOther = leashBehaviorOther;
    }

    public String getKnownTriggers() {
        return knownTriggers;
    }

    public void setKnownTriggers(String knownTriggers) {
        this.knownTriggers = knownTriggers;
    }

    public String getSocialCompatibility() {
        return socialCompatibility;
    }

    public void setSocialCompatibility(String socialCompatibility) {
        this.socialCompatibility = socialCompatibility;
    }

    // CHANGED: Now String instead of List<String>
    public String getHandlingNotes() {
        return handlingNotes;
    }

    public void setHandlingNotes(String handlingNotes) {
        this.handlingNotes = handlingNotes;
    }

    public String getHandlingNotesOther() {
        return handlingNotesOther;
    }

    public void setHandlingNotesOther(String handlingNotesOther) {
        this.handlingNotesOther = handlingNotesOther;
    }

    public String getComfortingMethods() {
        return comfortingMethods;
    }

    public void setComfortingMethods(String comfortingMethods) {
        this.comfortingMethods = comfortingMethods;
    }

    public Boolean getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(Boolean medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getMedicalConditionsDetails() {
        return medicalConditionsDetails;
    }

    public void setMedicalConditionsDetails(String medicalConditionsDetails) {
        this.medicalConditionsDetails = medicalConditionsDetails;
    }

    public Boolean getMedications() {
        return medications;
    }

    public void setMedications(Boolean medications) {
        this.medications = medications;
    }

    public String getMedicationsDetails() {
        return medicationsDetails;
    }

    public void setMedicationsDetails(String medicationsDetails) {
        this.medicationsDetails = medicationsDetails;
    }

    public String getEmergencyVetInfo() {
        return emergencyVetInfo;
    }

    public void setEmergencyVetInfo(String emergencyVetInfo) {
        this.emergencyVetInfo = emergencyVetInfo;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getAddressMeetingPoint() {
        return addressMeetingPoint;
    }

    public void setAddressMeetingPoint(String addressMeetingPoint) {
        this.addressMeetingPoint = addressMeetingPoint;
    }

    public String getAccessInstructions() {
        return accessInstructions;
    }

    public void setAccessInstructions(String accessInstructions) {
        this.accessInstructions = accessInstructions;
    }

    public String getBackupContact() {
        return backupContact;
    }

    public void setBackupContact(String backupContact) {
        this.backupContact = backupContact;
    }

    // CHANGED: Now String instead of List<String>
    public String getPostWalkPreferences() {
        return postWalkPreferences;
    }

    public void setPostWalkPreferences(String postWalkPreferences) {
        this.postWalkPreferences = postWalkPreferences;
    }

    // CHANGED: Now String instead of List<String>
    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getAdditionalServicesOther() {
        return additionalServicesOther;
    }

    public void setAdditionalServicesOther(String additionalServicesOther) {
        this.additionalServicesOther = additionalServicesOther;
    }

    public Boolean getConsent() {
        return consent;
    }

    public void setConsent(Boolean consent) {
        this.consent = consent;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDate getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(LocalDate signatureDate) {
        this.signatureDate = signatureDate;
    }

    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
    }
}