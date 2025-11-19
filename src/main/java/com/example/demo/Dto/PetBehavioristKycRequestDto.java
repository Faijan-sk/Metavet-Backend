package com.example.demo.Dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class PetBehavioristKycRequestDto {

    // ==================== Pet Reference ====================
    
    private String petUid; // UUID string from frontend

    // ==================== Step 1: Behavioral Concern Overview ====================
    
    private List<String> behavioralChallenges; // ["Separation anxiety", "Aggression", etc.]
    
    private String aggressionBiteDescription;
    
    private String otherBehaviorDescription;
    
    private String behaviorStartTime; // "As a puppy", "Within the last year", etc.
    
    private String behaviorFrequency; // "Daily", "Weekly", etc.
    
    private String specificSituationsDescription;

    // ==================== Step 2: Triggers & Context ====================
    
    private String knownTriggers;
    
    private String behaviorProgress; // "Improved", "Worsened", "Stayed the same"
    
    private String behaviorProgressContext;
    
    private List<String> aggressiveBehaviors; // ["Growling", "Snapping", etc.]
    
    private String seriousIncidents;

    // ==================== Step 3: Training & Tools History ====================
    
    private Boolean workedWithTrainer;
    
    private String trainerApproaches;
    
    private List<String> currentTrainingTools; // ["Clicker", "Muzzle", etc.]
    
    private String otherTrainingTool;
    
    private String petMotivation; // "Yes", "No", "Unsure"
    
    private String favoriteRewards;

    // ==================== Step 4: Routine & Environment ====================
    
    private String walksPerDay;
    
    private String offLeashTime;
    
    private String timeAlone;
    
    private String exerciseStimulation;
    
    private Boolean otherPets;
    
    private String otherPetsDetails;
    
    private Boolean childrenInHome;
    
    private String childrenAges;
    
    private String petResponseWithChildren;
    
    private String homeEnvironment; // "Apartment", "House with yard", etc.
    
    private String homeEnvironmentOther;

    // ==================== Step 5: Goals & Expectations ====================
    
    private String successOutcome;
    
    private String openToAdjustments; // "Yes", "No", "Not sure"
    
    private String preferredSessionType; // "In-person", "Virtual", "Either is fine"
    
    private String additionalNotes;

    // ==================== Consent ====================
    
    @NotNull(message = "Consent is required")
    private Boolean consentAccuracy;

    // ==================== Getters & Setters ====================

    public String getPetUid() {
        return petUid;
    }

    public void setPetUid(String petUid) {
        this.petUid = petUid;
    }

    public List<String> getBehavioralChallenges() {
        return behavioralChallenges;
    }

    public void setBehavioralChallenges(List<String> behavioralChallenges) {
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

    public List<String> getAggressiveBehaviors() {
        return aggressiveBehaviors;
    }

    public void setAggressiveBehaviors(List<String> aggressiveBehaviors) {
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

    public List<String> getCurrentTrainingTools() {
        return currentTrainingTools;
    }

    public void setCurrentTrainingTools(List<String> currentTrainingTools) {
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