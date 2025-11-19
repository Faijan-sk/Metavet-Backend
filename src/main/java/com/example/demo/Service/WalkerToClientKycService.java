package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.WalkerToClientKycRequestDto;
import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.WalkerToClientKycEntity;
import com.example.demo.Entities.WalkerToClientKycEntity.EnergyLevel;
import com.example.demo.Entities.WalkerToClientKycEntity.KycStatus;
import com.example.demo.Entities.WalkerToClientKycEntity.PreferredWalkType;
import com.example.demo.Entities.WalkerToClientKycEntity.WalkingExperience;
import com.example.demo.Repository.PetRepo;
import com.example.demo.Repository.WalkerToClientKycRepo;

import jakarta.validation.ValidationException;

@Service
public class WalkerToClientKycService {

    @Autowired
    private WalkerToClientKycRepo walkerKycRepo;

    @Autowired
    private PetRepo petsRepo;

    @Transactional
    public WalkerToClientKycEntity createWalkerKyc(WalkerToClientKycRequestDto dto) throws ValidationException {

        WalkerToClientKycEntity kyc = new WalkerToClientKycEntity();

        // ==================== Status Mapping (Optional) ====================
        
        // If status is provided in DTO, map it; otherwise, default PENDING will be used
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            kyc.setStatus(mapKycStatus(dto.getStatus()));
        }
        // If not provided, entity default (PENDING) will be used automatically

        // ==================== Pet Mapping via UUID ====================
        
        if (dto.getPetUid() != null && !dto.getPetUid().trim().isEmpty()) {
            try {
                UUID petUuid = UUID.fromString(dto.getPetUid());
                Optional<PetsEntity> petOpt = petsRepo.findByUid(petUuid);
                
                if (petOpt.isPresent()) {
                    kyc.setPet(petOpt.get());
                    kyc.setPetUid(dto.getPetUid()); // Store UID reference
                } else {
                    throw new ValidationException("Pet with UID " + dto.getPetUid() + " not found.");
                }
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid pet UID format: " + dto.getPetUid());
            }
        }

        // ==================== Pet & Routine Overview ====================
        
        kyc.setPetNames(dto.getPetNames());
        kyc.setBreedType(dto.getBreedType());
        kyc.setAge(dto.getAge());
        kyc.setPetSpecies(dto.getPetSpecies());
        
        // Map string enums to Java enums
        if (dto.getEnergyLevel() != null) {
            kyc.setEnergyLevel(mapEnergyLevel(dto.getEnergyLevel()));
        }
        
        if (dto.getWalkingExperience() != null) {
            kyc.setWalkingExperience(mapWalkingExperience(dto.getWalkingExperience()));
        }
        
        if (dto.getPreferredWalkType() != null) {
            kyc.setPreferredWalkType(mapPreferredWalkType(dto.getPreferredWalkType()));
        }
        
        // Store walk duration as string
        kyc.setPreferredWalkDuration(dto.getPreferredWalkDuration());
        
        // Validate custom duration if "Custom" is selected
        if ("Custom".equalsIgnoreCase(dto.getPreferredWalkDuration())) {
            if (dto.getCustomWalkDuration() == null || dto.getCustomWalkDuration() <= 0) {
                throw new ValidationException("Custom walk duration is required and must be greater than 0.");
            }
            kyc.setCustomWalkDuration(dto.getCustomWalkDuration());
        }

        // Store frequency as string
        kyc.setFrequency(dto.getFrequency());
        
        // Validate "Other" frequency
        if ("Other".equalsIgnoreCase(dto.getFrequency())) {
            if (dto.getFrequencyOther() == null || dto.getFrequencyOther().trim().isEmpty()) {
                throw new ValidationException("Please specify the frequency when selecting 'Other'.");
            }
            kyc.setFrequencyOther(dto.getFrequencyOther());
        }

        kyc.setPreferredTimeOfDay(dto.getPreferredTimeOfDay());
        kyc.setPreferredStartDate(dto.getPreferredStartDate());

        // ==================== Behavior & Handling ====================
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getLeashBehavior() != null && !dto.getLeashBehavior().isEmpty()) {
            kyc.setLeashBehavior(String.join(",", dto.getLeashBehavior()));
            
            // Validate "Other" leash behavior
            if (dto.getLeashBehavior().contains("Other")) {
                if (dto.getLeashBehaviorOther() == null || dto.getLeashBehaviorOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify leash behavior when selecting 'Other'.");
                }
                kyc.setLeashBehaviorOther(dto.getLeashBehaviorOther());
            }
        }

        kyc.setKnownTriggers(dto.getKnownTriggers());
        kyc.setSocialCompatibility(dto.getSocialCompatibility());
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getHandlingNotes() != null && !dto.getHandlingNotes().isEmpty()) {
            kyc.setHandlingNotes(String.join(",", dto.getHandlingNotes()));
            
            // Validate "Other" handling notes
            if (dto.getHandlingNotes().contains("Other")) {
                if (dto.getHandlingNotesOther() == null || dto.getHandlingNotesOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify handling notes when selecting 'Other'.");
                }
                kyc.setHandlingNotesOther(dto.getHandlingNotesOther());
            }
        }

        kyc.setComfortingMethods(dto.getComfortingMethods());

        // ==================== Health & Safety ====================
        
        if (dto.getMedicalConditions() != null) {
            kyc.setMedicalConditions(dto.getMedicalConditions());
            
            // Validate medical conditions details if YES
            if (dto.getMedicalConditions() == true) {
                if (dto.getMedicalConditionsDetails() == null || dto.getMedicalConditionsDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medical condition details.");
                }
                kyc.setMedicalConditionsDetails(dto.getMedicalConditionsDetails());
            }
        }

        if (dto.getMedications() != null) {
            kyc.setMedications(dto.getMedications());
            
            // Validate medication details if YES
            if (dto.getMedications() == true) {
                if (dto.getMedicationsDetails() == null || dto.getMedicationsDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medication details (name, dosage, timing).");
                }
                kyc.setMedicationsDetails(dto.getMedicationsDetails());
            }
        }

        kyc.setEmergencyVetInfo(dto.getEmergencyVetInfo());

        // ==================== Access & Logistics ====================
        
        kyc.setStartingLocation(dto.getStartingLocation());
        kyc.setAddressMeetingPoint(dto.getAddressMeetingPoint());
        kyc.setAccessInstructions(dto.getAccessInstructions());
        kyc.setBackupContact(dto.getBackupContact());
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getPostWalkPreferences() != null && !dto.getPostWalkPreferences().isEmpty()) {
            kyc.setPostWalkPreferences(String.join(",", dto.getPostWalkPreferences()));
        }

        // ==================== Services & Add-ons ====================
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getAdditionalServices() != null && !dto.getAdditionalServices().isEmpty()) {
            kyc.setAdditionalServices(String.join(",", dto.getAdditionalServices()));
            
            // Validate "Other" additional services
            if (dto.getAdditionalServices().contains("Other")) {
                if (dto.getAdditionalServicesOther() == null || dto.getAdditionalServicesOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify additional services when selecting 'Other'.");
                }
                kyc.setAdditionalServicesOther(dto.getAdditionalServicesOther());
            }
        }

        // ==================== Consent & Signature ====================
        
        if (dto.getConsent() == null || dto.getConsent() != true) {
            throw new ValidationException("Consent is required to proceed.");
        }
        kyc.setConsent(dto.getConsent());

        if (dto.getSignature() == null || dto.getSignature().trim().isEmpty()) {
            throw new ValidationException("Signature is required.");
        }
        kyc.setSignature(dto.getSignature());

        if (dto.getSignatureDate() == null) {
            throw new ValidationException("Signature date is required.");
        }
        kyc.setSignatureDate(dto.getSignatureDate());

        // Save and return
        return walkerKycRepo.save(kyc);
    }

    @Transactional
    public WalkerToClientKycEntity updateWalkerKyc(Long kycId, WalkerToClientKycRequestDto dto) throws ValidationException {
        
        Optional<WalkerToClientKycEntity> existingKycOpt = walkerKycRepo.findById(kycId);
        
        if (!existingKycOpt.isPresent()) {
            throw new ValidationException("Walker KYC with ID " + kycId + " not found.");
        }

        WalkerToClientKycEntity kyc = existingKycOpt.get();

        // ==================== Status Mapping (Optional) ====================
        
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            kyc.setStatus(mapKycStatus(dto.getStatus()));
        }

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

        // ==================== Pet & Routine Overview ====================
        
        kyc.setPetNames(dto.getPetNames());
        kyc.setBreedType(dto.getBreedType());
        kyc.setAge(dto.getAge());
        kyc.setPetSpecies(dto.getPetSpecies());
        
        if (dto.getEnergyLevel() != null) {
            kyc.setEnergyLevel(mapEnergyLevel(dto.getEnergyLevel()));
        }
        
        if (dto.getWalkingExperience() != null) {
            kyc.setWalkingExperience(mapWalkingExperience(dto.getWalkingExperience()));
        }
        
        if (dto.getPreferredWalkType() != null) {
            kyc.setPreferredWalkType(mapPreferredWalkType(dto.getPreferredWalkType()));
        }
        
        kyc.setPreferredWalkDuration(dto.getPreferredWalkDuration());
        
        if ("Custom".equalsIgnoreCase(dto.getPreferredWalkDuration())) {
            if (dto.getCustomWalkDuration() == null || dto.getCustomWalkDuration() <= 0) {
                throw new ValidationException("Custom walk duration is required and must be greater than 0.");
            }
            kyc.setCustomWalkDuration(dto.getCustomWalkDuration());
        }

        kyc.setFrequency(dto.getFrequency());
        
        if ("Other".equalsIgnoreCase(dto.getFrequency())) {
            if (dto.getFrequencyOther() == null || dto.getFrequencyOther().trim().isEmpty()) {
                throw new ValidationException("Please specify the frequency when selecting 'Other'.");
            }
            kyc.setFrequencyOther(dto.getFrequencyOther());
        }

        kyc.setPreferredTimeOfDay(dto.getPreferredTimeOfDay());
        kyc.setPreferredStartDate(dto.getPreferredStartDate());

        // ==================== Behavior & Handling ====================
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getLeashBehavior() != null) {
            kyc.setLeashBehavior(String.join(",", dto.getLeashBehavior()));
            
            if (dto.getLeashBehavior().contains("Other")) {
                if (dto.getLeashBehaviorOther() == null || dto.getLeashBehaviorOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify leash behavior when selecting 'Other'.");
                }
                kyc.setLeashBehaviorOther(dto.getLeashBehaviorOther());
            }
        }

        kyc.setKnownTriggers(dto.getKnownTriggers());
        kyc.setSocialCompatibility(dto.getSocialCompatibility());
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getHandlingNotes() != null) {
            kyc.setHandlingNotes(String.join(",", dto.getHandlingNotes()));
            
            if (dto.getHandlingNotes().contains("Other")) {
                if (dto.getHandlingNotesOther() == null || dto.getHandlingNotesOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify handling notes when selecting 'Other'.");
                }
                kyc.setHandlingNotesOther(dto.getHandlingNotesOther());
            }
        }

        kyc.setComfortingMethods(dto.getComfortingMethods());

        // ==================== Health & Safety ====================
        
        if (dto.getMedicalConditions() != null) {
            kyc.setMedicalConditions(dto.getMedicalConditions());
            
            if (dto.getMedicalConditions() == true) {
                if (dto.getMedicalConditionsDetails() == null || dto.getMedicalConditionsDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medical condition details.");
                }
                kyc.setMedicalConditionsDetails(dto.getMedicalConditionsDetails());
            }
        }

        if (dto.getMedications() != null) {
            kyc.setMedications(dto.getMedications());
            
            if (dto.getMedications() == true) {
                if (dto.getMedicationsDetails() == null || dto.getMedicationsDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medication details.");
                }
                kyc.setMedicationsDetails(dto.getMedicationsDetails());
            }
        }

        kyc.setEmergencyVetInfo(dto.getEmergencyVetInfo());

        // ==================== Access & Logistics ====================
        
        kyc.setStartingLocation(dto.getStartingLocation());
        kyc.setAddressMeetingPoint(dto.getAddressMeetingPoint());
        kyc.setAccessInstructions(dto.getAccessInstructions());
        kyc.setBackupContact(dto.getBackupContact());
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getPostWalkPreferences() != null) {
            kyc.setPostWalkPreferences(String.join(",", dto.getPostWalkPreferences()));
        }

        // ==================== Services & Add-ons ====================
        
        // CHANGED: Convert List<String> to comma-separated String
        if (dto.getAdditionalServices() != null) {
            kyc.setAdditionalServices(String.join(",", dto.getAdditionalServices()));
            
            if (dto.getAdditionalServices().contains("Other")) {
                if (dto.getAdditionalServicesOther() == null || dto.getAdditionalServicesOther().trim().isEmpty()) {
                    throw new ValidationException("Please specify additional services when selecting 'Other'.");
                }
                kyc.setAdditionalServicesOther(dto.getAdditionalServicesOther());
            }
        }

        // ==================== Consent & Signature ====================
        
        if (dto.getConsent() == null || dto.getConsent() != true) {
            throw new ValidationException("Consent is required to proceed.");
        }
        kyc.setConsent(dto.getConsent());

        if (dto.getSignature() == null || dto.getSignature().trim().isEmpty()) {
            throw new ValidationException("Signature is required.");
        }
        kyc.setSignature(dto.getSignature());

        if (dto.getSignatureDate() == null) {
            throw new ValidationException("Signature date is required.");
        }
        kyc.setSignatureDate(dto.getSignatureDate());

        return walkerKycRepo.save(kyc);
    }

    // ==================== NEW: Get All KYCs ====================
    
    public List<WalkerToClientKycEntity> getAllWalkerKycs() {
        return walkerKycRepo.findAllByOrderByCreatedAtDesc();
    }

    // ==================== NEW: Get KYC by UID ====================
    
    public Optional<WalkerToClientKycEntity> getWalkerKycByUid(UUID uid) {
        return walkerKycRepo.findByUid(uid);
    }

    public Optional<WalkerToClientKycEntity> getWalkerKycById(Long id) {
        return walkerKycRepo.findById(id);
    }

    public Optional<WalkerToClientKycEntity> getWalkerKycByPetId(Long petId) {
        return walkerKycRepo.findByPetId(petId).stream().findFirst();
    }
    
    public Optional<WalkerToClientKycEntity> getWalkerKycByPetUid(String petUid) {
        return walkerKycRepo.findByPetUid(petUid);
    }

    // ==================== NEW: Update Status by UID ====================
    
    @Transactional
    public WalkerToClientKycEntity updateStatusByUid(UUID uid, String status) throws ValidationException {
        Optional<WalkerToClientKycEntity> kycOpt = walkerKycRepo.findByUid(uid);
        
        if (!kycOpt.isPresent()) {
            throw new ValidationException("Walker KYC with UID " + uid + " not found.");
        }
        
        WalkerToClientKycEntity kyc = kycOpt.get();
        KycStatus mappedStatus = mapKycStatus(status);
        
        if (mappedStatus == null) {
            throw new ValidationException("Invalid status value: " + status + ". Allowed values: PENDING, APPROVED, REJECTED");
        }
        
        kyc.setStatus(mappedStatus);
        return walkerKycRepo.save(kyc);
    }

    // ==================== NEW: Delete by UID ====================
    
    @Transactional
    public void deleteWalkerKycByUid(UUID uid) throws ValidationException {
        if (!walkerKycRepo.existsByUid(uid)) {
            throw new ValidationException("Walker KYC with UID " + uid + " not found.");
        }
        
        Optional<WalkerToClientKycEntity> kycOpt = walkerKycRepo.findByUid(uid);
        kycOpt.ifPresent(kyc -> walkerKycRepo.delete(kyc));
    }

    @Transactional
    public void deleteWalkerKyc(Long id) throws ValidationException {
        if (!walkerKycRepo.existsById(id)) {
            throw new ValidationException("Walker KYC with ID " + id + " not found.");
        }
        walkerKycRepo.deleteById(id);
    }
    
    // ==================== Helper Methods for Enum Mapping ====================
    
    private KycStatus mapKycStatus(String status) {
        if (status == null) return null;
        
        switch (status.toUpperCase()) {
            case "PENDING":
                return KycStatus.PENDING;
            case "APPROVED":
                return KycStatus.APPROVED;
            case "REJECTED":
                return KycStatus.REJECTED;
            default:
                return null;
        }
    }
    
    private EnergyLevel mapEnergyLevel(String level) {
        if (level == null) return null;
        
        switch (level.toUpperCase()) {
            case "LOW":
                return EnergyLevel.LOW;
            case "MEDIUM":
                return EnergyLevel.MEDIUM;
            case "HIGH":
                return EnergyLevel.HIGH;
            default:
                return null;
        }
    }
    
    private WalkingExperience mapWalkingExperience(String experience) {
        if (experience == null) return null;
        
        switch (experience.toUpperCase()) {
            case "BEGINNER":
                return WalkingExperience.BEGINNER;
            case "INTERMEDIATE":
                return WalkingExperience.INTERMEDIATE;
            case "WELL-TRAINED":
            case "WELL_TRAINED":
                return WalkingExperience.WELL_TRAINED;
            case "REACTIVE":
                return WalkingExperience.REACTIVE;
            default:
                return null;
        }
    }
    
    private PreferredWalkType mapPreferredWalkType(String type) {
        if (type == null) return null;
        
        switch (type.toUpperCase()) {
            case "SOLO":
                return PreferredWalkType.SOLO;
            case "GROUP":
                return PreferredWalkType.GROUP;
            case "EITHER":
                return PreferredWalkType.EITHER;
            default:
                return null;
        }
    }
}