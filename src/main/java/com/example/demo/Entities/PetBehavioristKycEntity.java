package com.example.demo.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "behaviorist_to_client_kyc")
public class PetBehavioristKycEntity extends BaseEntity {
    
    // Store petUid for reference (optional, for debugging)
    @Column(length = 100)
    private String petUid;

    // ==================== Pet Reference ====================
    
    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    private PetsEntity pet;

    // ==================== Step 1: Behavioral Concern Overview ====================
    
    @Column(length = 1000)
    private String behavioralChallenges; // Comma-separated: "Separation anxiety,Aggression,Excessive barking"
    
    @Column(length = 2000)
    private String aggressionBiteDescription;
    
    @Column(length = 1000)
    private String otherBehaviorDescription;
    
    @Column(length = 100)
    private String behaviorStartTime; // "As a puppy", "Within the last year", "Recently (last 3 months)"
    
    @Column(length = 100)
    private String behaviorFrequency; // "Daily", "Weekly", "Occasionally", "Only in specific situations"
    
    @Column(length = 1000)
    private String specificSituationsDescription;

    // ==================== Step 2: Triggers & Context ====================
    
    @Column(length = 2000)
    private String knownTriggers;
    
    @Column(length = 100)
    private String behaviorProgress; // "Improved", "Worsened", "Stayed the same"
    
    @Column(length = 1000)
    private String behaviorProgressContext;
    
    @Column(length = 500)
    private String aggressiveBehaviors; // Comma-separated: "Growling,Snapping,Lunging"
    
    @Column(length = 2000)
    private String seriousIncidents;

    // ==================== Step 3: Training & Tools History ====================
    
    private Boolean workedWithTrainer;
    
    @Column(length = 2000)
    private String trainerApproaches;
    
    @Column(length = 500)
    private String currentTrainingTools; // Comma-separated: "Clicker,Muzzle,Harness"
    
    @Column(length = 500)
    private String otherTrainingTool;
    
    @Column(length = 50)
    private String petMotivation; // "Yes", "No", "Unsure"
    
    @Column(length = 500)
    private String favoriteRewards;

    // ==================== Step 4: Routine & Environment ====================
    
    @Column(length = 50)
    private String walksPerDay;
    
    @Column(length = 100)
    private String offLeashTime;
    
    @Column(length = 100)
    private String timeAlone;
    
    @Column(length = 500)
    private String exerciseStimulation;
    
    private Boolean otherPets;
    
    @Column(length = 2000)
    private String otherPetsDetails;
    
    private Boolean childrenInHome;
    
    @Column(length = 200)
    private String childrenAges;
    
    @Column(length = 2000)
    private String petResponseWithChildren;
    
    @Column(length = 100)
    private String homeEnvironment; // "Apartment", "House with yard", "Shared/communal", "Other"
    
    @Column(length = 500)
    private String homeEnvironmentOther;

    // ==================== Step 5: Goals & Expectations ====================
    
    @Column(length = 3000)
    private String successOutcome;
    
    @Column(length = 50)
    private String openToAdjustments; // "Yes", "No", "Not sure"
    
    @Column(length = 50)
    private String preferredSessionType; // "In-person", "Virtual", "Either is fine"
    
    @Column(length = 3000)
    private String additionalNotes;

    public enum KycStatus {
    	PENDING,
    	APPROVED,
    	REJECTED
    	}
    
    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", length = 20, nullable = false)
    private KycStatus kycStatus = KycStatus.PENDING; // default = PENDING
    
    // ==================== Consent ====================
    
    @Column(nullable = false)
    private Boolean consentAccuracy = false;
    
    

    // ==================== Getters & Setters ====================

    public KycStatus getKycStatus() {
    	return kycStatus;
    	}


    	public void setKycStatus(KycStatus kycStatus) {
    	this.kycStatus = kycStatus;
    	}
    	
    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
    }

    public PetsEntity getPet() {
        return pet;
    }

    public void setPet(PetsEntity pet) {
        this.pet = pet;
    }

    public String getBehavioralChallenges() {
        return behavioralChallenges;
    }

    public void setBehavioralChallenges(String behavioralChallenges) {
        this.behavioralChallenges = behavioralChallenges;
    }

    public String getAggressionBiteDescription() {
        return aggressionBiteDescription;
    }

    public void setAggressionBiteDescription(String aggressionBiteDescription) {
        this.aggressionBiteDescription = aggressionBiteDescription;
    }

    public String getOtherBehaviorDescription() {
        return otherBehaviorDescription;
    }

    public void setOtherBehaviorDescription(String otherBehaviorDescription) {
        this.otherBehaviorDescription = otherBehaviorDescription;
    }

    public String getBehaviorStartTime() {
        return behaviorStartTime;
    }

    public void setBehaviorStartTime(String behaviorStartTime) {
        this.behaviorStartTime = behaviorStartTime;
    }

    public String getBehaviorFrequency() {
        return behaviorFrequency;
    }

    public void setBehaviorFrequency(String behaviorFrequency) {
        this.behaviorFrequency = behaviorFrequency;
    }

    public String getSpecificSituationsDescription() {
        return specificSituationsDescription;
    }

    public void setSpecificSituationsDescription(String specificSituationsDescription) {
        this.specificSituationsDescription = specificSituationsDescription;
    }

    public String getKnownTriggers() {
        return knownTriggers;
    }

    public void setKnownTriggers(String knownTriggers) {
        this.knownTriggers = knownTriggers;
    }

    public String getBehaviorProgress() {
        return behaviorProgress;
    }

    public void setBehaviorProgress(String behaviorProgress) {
        this.behaviorProgress = behaviorProgress;
    }

    public String getBehaviorProgressContext() {
        return behaviorProgressContext;
    }

    public void setBehaviorProgressContext(String behaviorProgressContext) {
        this.behaviorProgressContext = behaviorProgressContext;
    }

    public String getAggressiveBehaviors() {
        return aggressiveBehaviors;
    }

    public void setAggressiveBehaviors(String aggressiveBehaviors) {
        this.aggressiveBehaviors = aggressiveBehaviors;
    }

    public String getSeriousIncidents() {
        return seriousIncidents;
    }

    public void setSeriousIncidents(String seriousIncidents) {
        this.seriousIncidents = seriousIncidents;
    }

    public Boolean getWorkedWithTrainer() {
        return workedWithTrainer;
    }

    public void setWorkedWithTrainer(Boolean workedWithTrainer) {
        this.workedWithTrainer = workedWithTrainer;
    }

    public String getTrainerApproaches() {
        return trainerApproaches;
    }

    public void setTrainerApproaches(String trainerApproaches) {
        this.trainerApproaches = trainerApproaches;
    }

    public String getCurrentTrainingTools() {
        return currentTrainingTools;
    }

    public void setCurrentTrainingTools(String currentTrainingTools) {
        this.currentTrainingTools = currentTrainingTools;
    }

    public String getOtherTrainingTool() {
        return otherTrainingTool;
    }

    public void setOtherTrainingTool(String otherTrainingTool) {
        this.otherTrainingTool = otherTrainingTool;
    }

    public String getPetMotivation() {
        return petMotivation;
    }

    public void setPetMotivation(String petMotivation) {
        this.petMotivation = petMotivation;
    }

    public String getFavoriteRewards() {
        return favoriteRewards;
    }

    public void setFavoriteRewards(String favoriteRewards) {
        this.favoriteRewards = favoriteRewards;
    }

    public String getWalksPerDay() {
        return walksPerDay;
    }

    public void setWalksPerDay(String walksPerDay) {
        this.walksPerDay = walksPerDay;
    }

    public String getOffLeashTime() {
        return offLeashTime;
    }

    public void setOffLeashTime(String offLeashTime) {
        this.offLeashTime = offLeashTime;
    }

    public String getTimeAlone() {
        return timeAlone;
    }

    public void setTimeAlone(String timeAlone) {
        this.timeAlone = timeAlone;
    }

    public String getExerciseStimulation() {
        return exerciseStimulation;
    }

    public void setExerciseStimulation(String exerciseStimulation) {
        this.exerciseStimulation = exerciseStimulation;
    }

    public Boolean getOtherPets() {
        return otherPets;
    }

    public void setOtherPets(Boolean otherPets) {
        this.otherPets = otherPets;
    }

    public String getOtherPetsDetails() {
        return otherPetsDetails;
    }

    public void setOtherPetsDetails(String otherPetsDetails) {
        this.otherPetsDetails = otherPetsDetails;
    }

    public Boolean getChildrenInHome() {
        return childrenInHome;
    }

    public void setChildrenInHome(Boolean childrenInHome) {
        this.childrenInHome = childrenInHome;
    }

    public String getChildrenAges() {
        return childrenAges;
    }

    public void setChildrenAges(String childrenAges) {
        this.childrenAges = childrenAges;
    }

    public String getPetResponseWithChildren() {
        return petResponseWithChildren;
    }

    public void setPetResponseWithChildren(String petResponseWithChildren) {
        this.petResponseWithChildren = petResponseWithChildren;
    }

    public String getHomeEnvironment() {
        return homeEnvironment;
    }

    public void setHomeEnvironment(String homeEnvironment) {
        this.homeEnvironment = homeEnvironment;
    }

    public String getHomeEnvironmentOther() {
        return homeEnvironmentOther;
    }

    public void setHomeEnvironmentOther(String homeEnvironmentOther) {
        this.homeEnvironmentOther = homeEnvironmentOther;
    }

    public String getSuccessOutcome() {
        return successOutcome;
    }

    public void setSuccessOutcome(String successOutcome) {
        this.successOutcome = successOutcome;
    }

    public String getOpenToAdjustments() {
        return openToAdjustments;
    }

    public void setOpenToAdjustments(String openToAdjustments) {
        this.openToAdjustments = openToAdjustments;
    }

    public String getPreferredSessionType() {
        return preferredSessionType;
    }

    public void setPreferredSessionType(String preferredSessionType) {
        this.preferredSessionType = preferredSessionType;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public Boolean getConsentAccuracy() {
        return consentAccuracy;
    }

    public void setConsentAccuracy(Boolean consentAccuracy) {
        this.consentAccuracy = consentAccuracy;
    }
}