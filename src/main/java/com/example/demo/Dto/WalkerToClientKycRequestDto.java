package com.example.demo.Dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WalkerToClientKycRequestDto {

    // ==================== Status Field ====================
    
    private String status; // "PENDING", "APPROVED", "REJECTED" - Optional

    // ==================== Pet & Routine Overview ====================
    
    private String petUid; // UUID string from frontend
    
    private String petNames;
    
    private String breedType;
    
    private Integer age;
    
    private String petSpecies;
    
    private String energyLevel; // "Low", "Medium", "High"
    
    private String walkingExperience; // "Beginner", "Intermediate", "Well-trained", "Reactive"
    
    private String preferredWalkType; // "Solo", "Group", "Either"
    
    private String preferredWalkDuration; // "15", "30", "60", "Custom"
    
    private Integer customWalkDuration;
    
    private String frequency; // "Daily", "Weekly", "As needed", "Other"
    
    private String frequencyOther;
    
    private String preferredTimeOfDay; // "Morning", "Midday", "Evening", "Flexible"
    
    private LocalDate preferredStartDate;

    // ==================== Behavior & Handling ====================
    
    private List<String> leashBehavior; // ["Pulls", "Walks nicely", etc.]
    
    private String leashBehaviorOther;
    
    private String knownTriggers;
    
    private String socialCompatibility; // "Friendly", "Solo only", "Unsure"
    
    private List<String> handlingNotes; // ["Needs harness", "Wears muzzle", etc.]
    
    private String handlingNotesOther;
    
    private String comfortingMethods;

    // ==================== Health & Safety ====================
    
    private Boolean medicalConditions;
    
    private String medicalConditionsDetails;
    
    private Boolean medications;
    
    private String medicationsDetails;
    
    private String emergencyVetInfo;

    // ==================== Access & Logistics ====================
    
    private String startingLocation; // "Home", "Apartment", "Workplace", "Other"
    
    private String addressMeetingPoint;
    
    private String accessInstructions;
    
    private String backupContact;
    
    private List<String> postWalkPreferences; // ["Text update", "Photo update", etc.]

    // ==================== Services & Add-ons ====================
    
    private List<String> additionalServices; // ["Feeding", "Water", etc.]
    
    private String additionalServicesOther;

    // ==================== Consent & Signature ====================
    
    @NotNull(message = "Consent is required")
    private Boolean consent;
    
    @NotBlank(message = "Signature is required")
    private String signature;
    
    private LocalDate signatureDate;

    // ==================== Getters & Setters ====================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
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

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getWalkingExperience() {
        return walkingExperience;
    }

    public void setWalkingExperience(String walkingExperience) {
        this.walkingExperience = walkingExperience;
    }

    public String getPreferredWalkType() {
        return preferredWalkType;
    }

    public void setPreferredWalkType(String preferredWalkType) {
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

    public List<String> getLeashBehavior() {
        return leashBehavior;
    }

    public void setLeashBehavior(List<String> leashBehavior) {
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

    public List<String> getHandlingNotes() {
        return handlingNotes;
    }

    public void setHandlingNotes(List<String> handlingNotes) {
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

    public List<String> getPostWalkPreferences() {
        return postWalkPreferences;
    }

    public void setPostWalkPreferences(List<String> postWalkPreferences) {
        this.postWalkPreferences = postWalkPreferences;
    }

    public List<String> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<String> additionalServices) {
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
}