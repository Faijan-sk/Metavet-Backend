package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.GroomerToClientKycRequestDto;
import com.example.demo.Entities.GroomerToClientKycEntity;
import com.example.demo.Entities.GroomerToClientKycEntity.KycStatus;
import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.GroomerToClientKycRepo;
import com.example.demo.Repository.PetRepo;
import com.example.demo.Repository.UserRepo;

import jakarta.validation.ValidationException;

@Service
public class GroomerToClientKycService {

    @Autowired
    private GroomerToClientKycRepo groomerKycRepo;

    @Autowired
    private PetRepo petsRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public GroomerToClientKycEntity createGroomerKyc(GroomerToClientKycRequestDto dto, String accessToken) throws ValidationException {

        GroomerToClientKycEntity kyc = new GroomerToClientKycEntity();

        // ==================== Extract User UID from Access Token ====================
        
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            try {
                // Remove "Bearer " prefix if present
                String token = accessToken.startsWith("Bearer ") 
                    ? accessToken.substring(7) 
                    : accessToken;

                // Extract user UUID from token
                UUID userUuid = jwtService.extractUserIdAsUUID(token, true);
                
                if (userUuid != null) {
                    // Verify user exists
                    Optional<UsersEntity> userOpt = userRepo.findByUid(userUuid);
                    if (userOpt.isPresent()) {
                        kyc.setUserUid(userUuid.toString());
                    } else {
                        throw new ValidationException("User not found for the provided token.");
                    }
                } else {
                    throw new ValidationException("Invalid user ID in token.");
                }
            } catch (Exception e) {
                throw new ValidationException("Failed to extract user information from token: " + e.getMessage());
            }
        } else {
            throw new ValidationException("Access token is required.");
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

        // Set default status as PENDING
        kyc.setStatus(KycStatus.PENDING);

        // ==================== Step 1: Grooming Preferences ====================
        
        if (dto.getGroomingFrequency() == null || dto.getGroomingFrequency().trim().isEmpty()) {
            throw new ValidationException("Grooming frequency is required.");
        }
        kyc.setGroomingFrequency(dto.getGroomingFrequency());
        
        kyc.setLastGroomingDate(dto.getLastGroomingDate());
        kyc.setPreferredStyle(dto.getPreferredStyle());
        kyc.setAvoidFocusAreas(dto.getAvoidFocusAreas());

        // ==================== Step 2: Health & Safety ====================
        
        // Convert List<String> to comma-separated String
        if (dto.getHealthConditions() != null && !dto.getHealthConditions().isEmpty()) {
            kyc.setHealthConditions(String.join(",", dto.getHealthConditions()));
            
            // Validate "Other" health condition
            if (dto.getHealthConditions().contains("Other")) {
                if (dto.getOtherHealthCondition() == null || dto.getOtherHealthCondition().trim().isEmpty()) {
                    throw new ValidationException("Please specify the health condition when selecting 'Other'.");
                }
                kyc.setOtherHealthCondition(dto.getOtherHealthCondition());
            }
        }

        if (dto.getOnMedication() != null) {
            kyc.setOnMedication(dto.getOnMedication());
            
            // Validate medication details if YES
            if (dto.getOnMedication() == true) {
                if (dto.getMedicationDetails() == null || dto.getMedicationDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medication details.");
                }
                kyc.setMedicationDetails(dto.getMedicationDetails());
            }
        } else {
            throw new ValidationException("Medication status is required.");
        }

        if (dto.getHadInjuriesSurgery() != null) {
            kyc.setHadInjuriesSurgery(dto.getHadInjuriesSurgery());
            
            // Validate injury/surgery details if YES
            if (dto.getHadInjuriesSurgery() == true) {
                if (dto.getInjurySurgeryDetails() == null || dto.getInjurySurgeryDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide injury or surgery details.");
                }
                kyc.setInjurySurgeryDetails(dto.getInjurySurgeryDetails());
            }
        } else {
            throw new ValidationException("Injury/surgery history is required.");
        }

        // ==================== Step 3: Behavior & Handling ====================
        
        // Convert List<String> to comma-separated String
        if (dto.getBehaviorIssues() != null && !dto.getBehaviorIssues().isEmpty()) {
            kyc.setBehaviorIssues(String.join(",", dto.getBehaviorIssues()));
        }

        kyc.setCalmingMethods(dto.getCalmingMethods());
        kyc.setTriggers(dto.getTriggers());

        // ==================== Step 4: Services & Scheduling ====================
        
        // Convert List<String> to comma-separated String
        if (dto.getServices() != null && !dto.getServices().isEmpty()) {
            kyc.setServices(String.join(",", dto.getServices()));
            
            // Validate "Other" service
            if (dto.getServices().contains("Other")) {
                if (dto.getOtherService() == null || dto.getOtherService().trim().isEmpty()) {
                    throw new ValidationException("Please specify the service when selecting 'Other'.");
                }
                kyc.setOtherService(dto.getOtherService());
            }
        }

        if (dto.getGroomingLocation() == null || dto.getGroomingLocation().trim().isEmpty()) {
            throw new ValidationException("Grooming location preference is required.");
        }
        kyc.setGroomingLocation(dto.getGroomingLocation());
        
        kyc.setAppointmentDate(dto.getAppointmentDate());
        kyc.setAppointmentTime(dto.getAppointmentTime());
        kyc.setAdditionalNotes(dto.getAdditionalNotes());
        
        // Convert List<String> to comma-separated String
        if (dto.getAddOns() != null && !dto.getAddOns().isEmpty()) {
            kyc.setAddOns(String.join(",", dto.getAddOns()));
        }

        // Save and return
        return groomerKycRepo.save(kyc);
    }

    @Transactional
    public GroomerToClientKycEntity updateGroomerKyc(Long kycId, GroomerToClientKycRequestDto dto, String accessToken) throws ValidationException {
        
        Optional<GroomerToClientKycEntity> existingKycOpt = groomerKycRepo.findById(kycId);
        
        if (!existingKycOpt.isPresent()) {
            throw new ValidationException("Groomer KYC with ID " + kycId + " not found.");
        }

        GroomerToClientKycEntity kyc = existingKycOpt.get();

        // ==================== Verify User UID from Access Token ====================
        
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            try {
                String token = accessToken.startsWith("Bearer ") 
                    ? accessToken.substring(7) 
                    : accessToken;

                UUID userUuid = jwtService.extractUserIdAsUUID(token, true);
                
                if (userUuid != null) {
                    // Verify this KYC belongs to this user
                    if (!kyc.getUserUid().equals(userUuid.toString())) {
                        throw new ValidationException("You are not authorized to update this KYC.");
                    }
                } else {
                    throw new ValidationException("Invalid user ID in token.");
                }
            } catch (Exception e) {
                throw new ValidationException("Failed to verify user from token: " + e.getMessage());
            }
        } else {
            throw new ValidationException("Access token is required.");
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

        // ==================== Step 1: Grooming Preferences ====================
        
        if (dto.getGroomingFrequency() != null && !dto.getGroomingFrequency().trim().isEmpty()) {
            kyc.setGroomingFrequency(dto.getGroomingFrequency());
        }
        
        kyc.setLastGroomingDate(dto.getLastGroomingDate());
        kyc.setPreferredStyle(dto.getPreferredStyle());
        kyc.setAvoidFocusAreas(dto.getAvoidFocusAreas());

        // ==================== Step 2: Health & Safety ====================
        
        if (dto.getHealthConditions() != null) {
            kyc.setHealthConditions(String.join(",", dto.getHealthConditions()));
            
            if (dto.getHealthConditions().contains("Other")) {
                if (dto.getOtherHealthCondition() == null || dto.getOtherHealthCondition().trim().isEmpty()) {
                    throw new ValidationException("Please specify the health condition when selecting 'Other'.");
                }
                kyc.setOtherHealthCondition(dto.getOtherHealthCondition());
            }
        }

        if (dto.getOnMedication() != null) {
            kyc.setOnMedication(dto.getOnMedication());
            
            if (dto.getOnMedication() == true) {
                if (dto.getMedicationDetails() == null || dto.getMedicationDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide medication details.");
                }
                kyc.setMedicationDetails(dto.getMedicationDetails());
            }
        }

        if (dto.getHadInjuriesSurgery() != null) {
            kyc.setHadInjuriesSurgery(dto.getHadInjuriesSurgery());
            
            if (dto.getHadInjuriesSurgery() == true) {
                if (dto.getInjurySurgeryDetails() == null || dto.getInjurySurgeryDetails().trim().isEmpty()) {
                    throw new ValidationException("Please provide injury or surgery details.");
                }
                kyc.setInjurySurgeryDetails(dto.getInjurySurgeryDetails());
            }
        }

        // ==================== Step 3: Behavior & Handling ====================
        
        if (dto.getBehaviorIssues() != null) {
            kyc.setBehaviorIssues(String.join(",", dto.getBehaviorIssues()));
        }

        kyc.setCalmingMethods(dto.getCalmingMethods());
        kyc.setTriggers(dto.getTriggers());

        // ==================== Step 4: Services & Scheduling ====================
        
        if (dto.getServices() != null) {
            kyc.setServices(String.join(",", dto.getServices()));
            
            if (dto.getServices().contains("Other")) {
                if (dto.getOtherService() == null || dto.getOtherService().trim().isEmpty()) {
                    throw new ValidationException("Please specify the service when selecting 'Other'.");
                }
                kyc.setOtherService(dto.getOtherService());
            }
        }

        if (dto.getGroomingLocation() != null && !dto.getGroomingLocation().trim().isEmpty()) {
            kyc.setGroomingLocation(dto.getGroomingLocation());
        }
        
        kyc.setAppointmentDate(dto.getAppointmentDate());
        kyc.setAppointmentTime(dto.getAppointmentTime());
        kyc.setAdditionalNotes(dto.getAdditionalNotes());
        
        if (dto.getAddOns() != null) {
            kyc.setAddOns(String.join(",", dto.getAddOns()));
        }

        return groomerKycRepo.save(kyc);
    }

    // ==================== NEW: Get All KYCs ====================
    
    public List<GroomerToClientKycEntity> getAllGroomerKycs() {
        return groomerKycRepo.findAllByOrderByCreatedAtDesc();
    }

    // ==================== NEW: Get KYC by UID ====================
    
    public Optional<GroomerToClientKycEntity> getGroomerKycByUid(String uid) throws ValidationException {
        try {
            UUID uuid = UUID.fromString(uid);
            return groomerKycRepo.findByUid(uuid);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid UID format: " + uid);
        }
    }

    // ==================== NEW: Get Status by UID ====================
    
    public KycStatus getKycStatusByUid(String uid) throws ValidationException {
        Optional<GroomerToClientKycEntity> kycOpt = getGroomerKycByUid(uid);
        
        if (kycOpt.isPresent()) {
            return kycOpt.get().getStatus();
        } else {
            throw new ValidationException("KYC with UID " + uid + " not found.");
        }
    }

    // ==================== NEW: Update Status by UID ====================
    
    @Transactional
    public GroomerToClientKycEntity updateKycStatusByUid(String uid, String status) throws ValidationException {
        Optional<GroomerToClientKycEntity> kycOpt = getGroomerKycByUid(uid);
        
        if (!kycOpt.isPresent()) {
            throw new ValidationException("KYC with UID " + uid + " not found.");
        }

        GroomerToClientKycEntity kyc = kycOpt.get();
        
        try {
            KycStatus newStatus = KycStatus.valueOf(status.toUpperCase());
            kyc.setStatus(newStatus);
            return groomerKycRepo.save(kyc);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status value. Allowed values: PENDING, APPROVED, REJECTED");
        }
    }

    // ==================== NEW: Delete KYC by UID ====================
    
    @Transactional
    public void deleteGroomerKycByUid(String uid) throws ValidationException {
        Optional<GroomerToClientKycEntity> kycOpt = getGroomerKycByUid(uid);
        
        if (!kycOpt.isPresent()) {
            throw new ValidationException("KYC with UID " + uid + " not found.");
        }

        groomerKycRepo.delete(kycOpt.get());
    }

    // ==================== Existing Methods ====================

    public Optional<GroomerToClientKycEntity> getGroomerKycById(Long id) {
        return groomerKycRepo.findById(id);
    }

    public Optional<GroomerToClientKycEntity> getGroomerKycByPetId(Long petId) {
        return groomerKycRepo.findByPetId(petId).stream().findFirst();
    }
    
    public Optional<GroomerToClientKycEntity> getGroomerKycByPetUid(String petUid) {
        return groomerKycRepo.findByPetUid(petUid);
    }

    @Transactional
    public void deleteGroomerKyc(Long id, String accessToken) throws ValidationException {
        
        Optional<GroomerToClientKycEntity> kycOpt = groomerKycRepo.findById(id);
        
        if (!kycOpt.isPresent()) {
            throw new ValidationException("Groomer KYC with ID " + id + " not found.");
        }

        GroomerToClientKycEntity kyc = kycOpt.get();

        // Verify ownership
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            try {
                String token = accessToken.startsWith("Bearer ") 
                    ? accessToken.substring(7) 
                    : accessToken;

                UUID userUuid = jwtService.extractUserIdAsUUID(token, true);
                
                if (userUuid != null) {
                    if (!kyc.getUserUid().equals(userUuid.toString())) {
                        throw new ValidationException("You are not authorized to delete this KYC.");
                    }
                } else {
                    throw new ValidationException("Invalid user ID in token.");
                }
            } catch (Exception e) {
                throw new ValidationException("Failed to verify user from token: " + e.getMessage());
            }
        } else {
            throw new ValidationException("Access token is required.");
        }

        groomerKycRepo.deleteById(id);
    }
}