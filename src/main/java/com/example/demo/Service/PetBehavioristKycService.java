package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.PetBehavioristKycRequestDto;
import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.PetBehavioristKycEntity;
import com.example.demo.Entities.PetBehavioristKycEntity.KycStatus;
import com.example.demo.Repository.PetRepo;
import com.example.demo.Repository.PetBehavioristKycRepo;

import jakarta.validation.ValidationException;

@Service
public class PetBehavioristKycService {

    @Autowired
    private PetBehavioristKycRepo behavioristKycRepo;

    @Autowired
    private PetRepo petsRepo;

    @Transactional
    public PetBehavioristKycEntity createBehavioristKyc(PetBehavioristKycRequestDto dto) throws ValidationException {

        PetBehavioristKycEntity kyc = new PetBehavioristKycEntity();

        // ==================== Pet Mapping via UUID ====================
        
        if (dto.getPetUid() != null && !dto.getPetUid().trim().isEmpty()) {
            try {
                UUID petUuid = UUID.fromString(dto.getPetUid());
                Optional<PetsEntity> petOpt = petsRepo.findByUid(petUuid);
                
                if (petOpt.isPresent()) {
                    kyc.setPet(petOpt.get());
                    kyc.setPetUid(dto.getPetUid());
                } else {
                    throw new ValidationException("Pet with UID " + dto.getPetUid() + " not found.");
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid pet UID format: " + dto.getPetUid());
            }
        }

        // ==================== Step 1: Behavioral Concern Overview ====================
        
        // Validate behavioral challenges
        if (dto.getBehavioralChallenges() == null || dto.getBehavioralChallenges().isEmpty()) {
            throw new ValidationException("Please select at least one behavioral challenge.");
        }
        
        // Convert List to comma-separated String
        kyc.setBehavioralChallenges(String.join(",", dto.getBehavioralChallenges()));
        
        // Validate aggression description if "Aggression" is selected
        if (dto.getBehavioralChallenges().contains("Aggression")) {
            if (dto.getAggressionBiteDescription() == null || dto.getAggressionBiteDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe aggression incidents when 'Aggression' is selected.");
            }
            kyc.setAggressionBiteDescription(dto.getAggressionBiteDescription());
        }
        
        // Validate other description if "Other" is selected
        if (dto.getBehavioralChallenges().contains("Other")) {
            if (dto.getOtherBehaviorDescription() == null || dto.getOtherBehaviorDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe other behavioral challenge when 'Other' is selected.");
            }
            kyc.setOtherBehaviorDescription(dto.getOtherBehaviorDescription());
        }
        
        kyc.setBehaviorStartTime(dto.getBehaviorStartTime());
        kyc.setBehaviorFrequency(dto.getBehaviorFrequency());
        
        // Validate specific situations if selected
        if ("Only in specific situations".equalsIgnoreCase(dto.getBehaviorFrequency())) {
            if (dto.getSpecificSituationsDescription() == null || dto.getSpecificSituationsDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe the specific situations when 'Only in specific situations' is selected.");
            }
            kyc.setSpecificSituationsDescription(dto.getSpecificSituationsDescription());
        }

        // ==================== Step 2: Triggers & Context ====================
        
        kyc.setKnownTriggers(dto.getKnownTriggers());
        kyc.setBehaviorProgress(dto.getBehaviorProgress());
        kyc.setBehaviorProgressContext(dto.getBehaviorProgressContext());
        
        // Convert aggressive behaviors List to comma-separated String
        if (dto.getAggressiveBehaviors() != null && !dto.getAggressiveBehaviors().isEmpty()) {
            kyc.setAggressiveBehaviors(String.join(",", dto.getAggressiveBehaviors()));
        }
        
        kyc.setSeriousIncidents(dto.getSeriousIncidents());

        // ==================== Step 3: Training & Tools History ====================
        
        kyc.setWorkedWithTrainer(dto.getWorkedWithTrainer());
        
        // Validate trainer approaches if worked with trainer
        if (dto.getWorkedWithTrainer() != null && dto.getWorkedWithTrainer() == true) {
            kyc.setTrainerApproaches(dto.getTrainerApproaches());
        }
        
        // Convert training tools List to comma-separated String
        if (dto.getCurrentTrainingTools() != null && !dto.getCurrentTrainingTools().isEmpty()) {
            kyc.setCurrentTrainingTools(String.join(",", dto.getCurrentTrainingTools()));
            
            // Validate other training tool if "Other" is selected
            if (dto.getCurrentTrainingTools().contains("Other")) {
                if (dto.getOtherTrainingTool() == null || dto.getOtherTrainingTool().trim().isEmpty()) {
                    throw new ValidationException("Please specify other training tool when 'Other' is selected.");
                }
                kyc.setOtherTrainingTool(dto.getOtherTrainingTool());
            }
        }
        
        kyc.setPetMotivation(dto.getPetMotivation());
        
        // Validate favorite rewards if pet is motivated
        if ("Yes".equalsIgnoreCase(dto.getPetMotivation())) {
            kyc.setFavoriteRewards(dto.getFavoriteRewards());
        }

        // ==================== Step 4: Routine & Environment ====================
        
        kyc.setWalksPerDay(dto.getWalksPerDay());
        kyc.setOffLeashTime(dto.getOffLeashTime());
        kyc.setTimeAlone(dto.getTimeAlone());
        kyc.setExerciseStimulation(dto.getExerciseStimulation());
        
        kyc.setOtherPets(dto.getOtherPets());
        
        // Validate other pets details if other pets exist
        if (dto.getOtherPets() != null && dto.getOtherPets() == true) {
            kyc.setOtherPetsDetails(dto.getOtherPetsDetails());
        }
        
        kyc.setChildrenInHome(dto.getChildrenInHome());
        
        // Validate children details if children are in home
        if (dto.getChildrenInHome() != null && dto.getChildrenInHome() == true) {
            kyc.setChildrenAges(dto.getChildrenAges());
            kyc.setPetResponseWithChildren(dto.getPetResponseWithChildren());
        }
        
        kyc.setHomeEnvironment(dto.getHomeEnvironment());
        
        // Validate home environment other if "Other" is selected
        if ("Other".equalsIgnoreCase(dto.getHomeEnvironment())) {
            if (dto.getHomeEnvironmentOther() == null || dto.getHomeEnvironmentOther().trim().isEmpty()) {
                throw new ValidationException("Please describe home environment when 'Other' is selected.");
            }
            kyc.setHomeEnvironmentOther(dto.getHomeEnvironmentOther());
        }

        // ==================== Step 5: Goals & Expectations ====================
        
        kyc.setSuccessOutcome(dto.getSuccessOutcome());
        kyc.setOpenToAdjustments(dto.getOpenToAdjustments());
        kyc.setPreferredSessionType(dto.getPreferredSessionType());
        kyc.setAdditionalNotes(dto.getAdditionalNotes());

        // ==================== Consent ====================
        
        if (dto.getConsentAccuracy() == null || dto.getConsentAccuracy() != true) {
            throw new ValidationException("Consent is required to proceed.");
        }
        kyc.setConsentAccuracy(dto.getConsentAccuracy());

        // Save and return
        return behavioristKycRepo.save(kyc);
    }

    @Transactional
    public PetBehavioristKycEntity updateBehavioristKyc(UUID uid, PetBehavioristKycRequestDto dto) throws ValidationException {
        
        Optional<PetBehavioristKycEntity> existingKycOpt = behavioristKycRepo.findByUid(uid);
        
        if (!existingKycOpt.isPresent()) {
            throw new ValidationException("Behaviorist KYC with UID " + uid + " not found.");
        }

        PetBehavioristKycEntity kyc = existingKycOpt.get();

        // ==================== Pet Mapping via UUID ====================
        
        if (dto.getPetUid() != null && !dto.getPetUid().trim().isEmpty()) {
            try {
                UUID petUuid = UUID.fromString(dto.getPetUid());
                Optional<PetsEntity> petOpt = petsRepo.findByUid(petUuid);
                
                if (petOpt.isPresent()) {
                    kyc.setPet(petOpt.get());
                    kyc.setPetUid(dto.getPetUid());
                } else {
                    throw new ValidationException("Pet with UID " + dto.getPetUid() + " not found.");
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid pet UID format: " + dto.getPetUid());
            }
        }

        // ==================== Step 1: Behavioral Concern Overview ====================
        
        if (dto.getBehavioralChallenges() == null || dto.getBehavioralChallenges().isEmpty()) {
            throw new ValidationException("Please select at least one behavioral challenge.");
        }
        
        kyc.setBehavioralChallenges(String.join(",", dto.getBehavioralChallenges()));
        
        if (dto.getBehavioralChallenges().contains("Aggression")) {
            if (dto.getAggressionBiteDescription() == null || dto.getAggressionBiteDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe aggression incidents when 'Aggression' is selected.");
            }
            kyc.setAggressionBiteDescription(dto.getAggressionBiteDescription());
        }
        
        if (dto.getBehavioralChallenges().contains("Other")) {
            if (dto.getOtherBehaviorDescription() == null || dto.getOtherBehaviorDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe other behavioral challenge when 'Other' is selected.");
            }
            kyc.setOtherBehaviorDescription(dto.getOtherBehaviorDescription());
        }
        
        kyc.setBehaviorStartTime(dto.getBehaviorStartTime());
        kyc.setBehaviorFrequency(dto.getBehaviorFrequency());
        
        if ("Only in specific situations".equalsIgnoreCase(dto.getBehaviorFrequency())) {
            if (dto.getSpecificSituationsDescription() == null || dto.getSpecificSituationsDescription().trim().isEmpty()) {
                throw new ValidationException("Please describe the specific situations.");
            }
            kyc.setSpecificSituationsDescription(dto.getSpecificSituationsDescription());
        }

        // ==================== Step 2: Triggers & Context ====================
        
        kyc.setKnownTriggers(dto.getKnownTriggers());
        kyc.setBehaviorProgress(dto.getBehaviorProgress());
        kyc.setBehaviorProgressContext(dto.getBehaviorProgressContext());
        
        if (dto.getAggressiveBehaviors() != null && !dto.getAggressiveBehaviors().isEmpty()) {
            kyc.setAggressiveBehaviors(String.join(",", dto.getAggressiveBehaviors()));
        }
        
        kyc.setSeriousIncidents(dto.getSeriousIncidents());

        // ==================== Step 3: Training & Tools History ====================
        
        kyc.setWorkedWithTrainer(dto.getWorkedWithTrainer());
        
        if (dto.getWorkedWithTrainer() != null && dto.getWorkedWithTrainer() == true) {
            kyc.setTrainerApproaches(dto.getTrainerApproaches());
        }
        
        if (dto.getCurrentTrainingTools() != null && !dto.getCurrentTrainingTools().isEmpty()) {
            kyc.setCurrentTrainingTools(String.join(",", dto.getCurrentTrainingTools()));
            
            if (dto.getCurrentTrainingTools().contains("Other")) {
                if (dto.getOtherTrainingTool() == null || dto.getOtherTrainingTool().trim().isEmpty()) {
                    throw new ValidationException("Please specify other training tool.");
                }
                kyc.setOtherTrainingTool(dto.getOtherTrainingTool());
            }
        }
        
        kyc.setPetMotivation(dto.getPetMotivation());
        
        if ("Yes".equalsIgnoreCase(dto.getPetMotivation())) {
            kyc.setFavoriteRewards(dto.getFavoriteRewards());
        }

        // ==================== Step 4: Routine & Environment ====================
        
        kyc.setWalksPerDay(dto.getWalksPerDay());
        kyc.setOffLeashTime(dto.getOffLeashTime());
        kyc.setTimeAlone(dto.getTimeAlone());
        kyc.setExerciseStimulation(dto.getExerciseStimulation());
        
        kyc.setOtherPets(dto.getOtherPets());
        
        if (dto.getOtherPets() != null && dto.getOtherPets() == true) {
            kyc.setOtherPetsDetails(dto.getOtherPetsDetails());
        }
        
        kyc.setChildrenInHome(dto.getChildrenInHome());
        
        if (dto.getChildrenInHome() != null && dto.getChildrenInHome() == true) {
            kyc.setChildrenAges(dto.getChildrenAges());
            kyc.setPetResponseWithChildren(dto.getPetResponseWithChildren());
        }
        
        kyc.setHomeEnvironment(dto.getHomeEnvironment());
        
        if ("Other".equalsIgnoreCase(dto.getHomeEnvironment())) {
            if (dto.getHomeEnvironmentOther() == null || dto.getHomeEnvironmentOther().trim().isEmpty()) {
                throw new ValidationException("Please describe home environment.");
            }
            kyc.setHomeEnvironmentOther(dto.getHomeEnvironmentOther());
        }

        // ==================== Step 5: Goals & Expectations ====================
        
        kyc.setSuccessOutcome(dto.getSuccessOutcome());
        kyc.setOpenToAdjustments(dto.getOpenToAdjustments());
        kyc.setPreferredSessionType(dto.getPreferredSessionType());
        kyc.setAdditionalNotes(dto.getAdditionalNotes());

        // ==================== Consent ====================
        
        if (dto.getConsentAccuracy() == null || dto.getConsentAccuracy() != true) {
            throw new ValidationException("Consent is required to proceed.");
        }
        kyc.setConsentAccuracy(dto.getConsentAccuracy());

        return behavioristKycRepo.save(kyc);
    }
    
    @Transactional
    public PetBehavioristKycEntity updateKycStatus(UUID uid, String statusStr) throws ValidationException {
    Optional<PetBehavioristKycEntity> existing = behavioristKycRepo.findByUid(uid);
    if (!existing.isPresent()) {
    throw new ValidationException("Behaviorist KYC with UID " + uid + " not found.");
    }


    PetBehavioristKycEntity kyc = existing.get();


    if (statusStr == null) {
    throw new ValidationException("Status value is required.");
    }
    


    try {
    KycStatus status = KycStatus.valueOf(statusStr.trim().toUpperCase());
    kyc.setKycStatus(status);
    return behavioristKycRepo.save(kyc);
    } catch (IllegalArgumentException iae) {
    throw new ValidationException("Invalid status. Allowed values: PENDING, APPROVED, REJECTED");
    }
    }
    
    

    public Optional<PetBehavioristKycEntity> getBehavioristKycByUid(UUID uid) {
        return behavioristKycRepo.findByUid(uid);
    }

    public List<PetBehavioristKycEntity> getAllBehavioristKycs() {
        return behavioristKycRepo.findAllByOrderByCreatedAtDesc();
    }

    public Optional<PetBehavioristKycEntity> getBehavioristKycByPetId(Long petId) {
        return behavioristKycRepo.findByPetId(petId).stream().findFirst();
    }
    
    public Optional<PetBehavioristKycEntity> getBehavioristKycByPetUid(String petUid) {
        return behavioristKycRepo.findByPetUid(petUid);
    }

    @Transactional
    public void deleteBehavioristKyc(UUID uid) throws ValidationException {
        Optional<PetBehavioristKycEntity> kycOpt = behavioristKycRepo.findByUid(uid);
        
        if (!kycOpt.isPresent()) {
            throw new ValidationException("Behaviorist KYC with UID " + uid + " not found.");
        }
        
        behavioristKycRepo.delete(kycOpt.get());
    }

    // ==================== Helpful Search Methods ====================

    public List<PetBehavioristKycEntity> getKycsWithAggression() {
        return behavioristKycRepo.findKycsWithAggression();
    }

    public List<PetBehavioristKycEntity> getKycsWithBiteHistory() {
        return behavioristKycRepo.findKycsWithBiteHistory();
    }

    public List<PetBehavioristKycEntity> getWorseningBehaviors() {
        return behavioristKycRepo.findWorseningBehaviors();
    }

    public List<PetBehavioristKycEntity> getKycsWithChildren() {
        return behavioristKycRepo.findKycsWithChildren();
    }

    public List<PetBehavioristKycEntity> getVirtualSessionPreferences() {
        return behavioristKycRepo.findVirtualSessionPreferences();
    }

    public List<PetBehavioristKycEntity> getInPersonSessionPreferences() {
        return behavioristKycRepo.findInPersonSessionPreferences();
    }
    
    public List<PetBehavioristKycEntity> getKycsByStatus(KycStatus status) {
    	return behavioristKycRepo.findByKycStatus(status);
    	}
}