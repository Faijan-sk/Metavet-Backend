package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.PetBehavioristKycRequestDto;
import com.example.demo.Entities.PetBehavioristKycEntity;
import com.example.demo.Entities.PetBehavioristKycEntity.KycStatus;
import com.example.demo.Service.PetBehavioristKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/api/behaviorist-kyc")
public class PetBehavioristKycController {

    private static final Logger logger = LoggerFactory.getLogger(PetBehavioristKycController.class);

    @Autowired
    private PetBehavioristKycService behavioristKycService;

    /**
     * Create new Behaviorist KYC
     * POST /api/behaviorist-kyc
     * 
     * Accepts JSON payload from frontend
     */
    @PostMapping
    public ResponseEntity<?> createBehavioristKyc(
            @Valid @RequestBody PetBehavioristKycRequestDto dto,
            BindingResult bindingResult) {

        try {
            logger.info("Received Behaviorist KYC creation request for petUid: {}", dto.getPetUid());
            
            // Check for validation errors from @Valid
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for PetBehavioristKycRequestDto: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(bindingResult.getAllErrors());
            }

            // Call service to create KYC
            PetBehavioristKycEntity createdKyc = behavioristKycService.createBehavioristKyc(dto);
            
            logger.info("Behaviorist KYC created successfully with UID: {} for pet: {}", 
                       createdKyc.getUid(), 
                       createdKyc.getPet() != null ? createdKyc.getPet().getPetName() : "unknown");
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdKyc);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while creating Behaviorist KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Unexpected error while creating Behaviorist KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Update existing Behaviorist KYC
     * PUT /api/behaviorist-kyc/{uid}
     */
    @PutMapping("/{uid}")
    public ResponseEntity<?> updateBehavioristKyc(
            @PathVariable String uid,
            @Valid @RequestBody PetBehavioristKycRequestDto dto,
            BindingResult bindingResult) {

        try {
            UUID kycUid = UUID.fromString(uid);
            logger.info("Received Behaviorist KYC update request for UID: {}", kycUid);
            
            // Check for validation errors
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for updating Behaviorist KYC: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(bindingResult.getAllErrors());
            }

            // Call service to update KYC
            PetBehavioristKycEntity updatedKyc = behavioristKycService.updateBehavioristKyc(kycUid, dto);
            
            logger.info("Behaviorist KYC updated successfully with UID: {}", kycUid);
            return ResponseEntity.ok(updatedKyc);

        } catch (IllegalArgumentException iae) {
            logger.warn("Invalid UUID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while updating Behaviorist KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Unexpected error while updating Behaviorist KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Get Behaviorist KYC by UID
     * GET /api/behaviorist-kyc/{uid}
     */
    @GetMapping("/{uid}")
    public ResponseEntity<?> getBehavioristKycByUid(@PathVariable String uid) {
        try {
            UUID kycUid = UUID.fromString(uid);
            Optional<PetBehavioristKycEntity> kyc = behavioristKycService.getBehavioristKycByUid(kycUid);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Behaviorist KYC with UID " + uid + " not found.");
            }

        } catch (IllegalArgumentException iae) {
            logger.warn("Invalid UUID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (Exception ex) {
            logger.error("Error fetching Behaviorist KYC by UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }

    /**
     * Get all Behaviorist KYCs
     * GET /api/behaviorist-kyc
     */
    @GetMapping
    public ResponseEntity<?> getAllBehavioristKycs() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getAllBehavioristKycs();
            return ResponseEntity.ok(kycs);

        } catch (Exception ex) {
            logger.error("Error fetching all Behaviorist KYCs", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get Behaviorist KYC by Pet ID (Long ID)
     * GET /api/behaviorist-kyc/pet/{petId}
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getBehavioristKycByPetId(@PathVariable Long petId) {
        try {
            Optional<PetBehavioristKycEntity> kyc = behavioristKycService.getBehavioristKycByPetId(petId);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Behaviorist KYC for Pet ID " + petId + " not found.");
            }

        } catch (Exception ex) {
            logger.error("Error fetching Behaviorist KYC by Pet ID: {}", petId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }
    
    /**
     * Get Behaviorist KYC by Pet UID (UUID String)
     * GET /api/behaviorist-kyc/pet-uid/{petUid}
     */
    @GetMapping("/pet-uid/{petUid}")
    public ResponseEntity<?> getBehavioristKycByPetUid(@PathVariable String petUid) {
        try {
            Optional<PetBehavioristKycEntity> kyc = behavioristKycService.getBehavioristKycByPetUid(petUid);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Behaviorist KYC for Pet UID " + petUid + " not found.");
            }

        } catch (Exception ex) {
            logger.error("Error fetching Behaviorist KYC by Pet UID: {}", petUid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }

    /**
     * Delete Behaviorist KYC by UID
     * DELETE /api/behaviorist-kyc/{uid}
     */
    @DeleteMapping("/{uid}")
    public ResponseEntity<?> deleteBehavioristKyc(@PathVariable String uid) {
        try {
            UUID kycUid = UUID.fromString(uid);
            behavioristKycService.deleteBehavioristKyc(kycUid);
            
            logger.info("Behaviorist KYC deleted successfully with UID: {}", kycUid);
            return ResponseEntity.ok("Behaviorist KYC deleted successfully.");

        } catch (IllegalArgumentException iae) {
            logger.warn("Invalid UUID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while deleting Behaviorist KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Error deleting Behaviorist KYC with UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting KYC.");
        }
    }

    // ==================== Helpful Search Endpoints ====================

    /**
     * Get KYCs with aggression concerns
     * GET /api/behaviorist-kyc/search/aggression
     */
    @GetMapping("/search/aggression")
    public ResponseEntity<?> getKycsWithAggression() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getKycsWithAggression();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching KYCs with aggression", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get KYCs with bite history
     * GET /api/behaviorist-kyc/search/bite-history
     */
    @GetMapping("/search/bite-history")
    public ResponseEntity<?> getKycsWithBiteHistory() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getKycsWithBiteHistory();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching KYCs with bite history", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get KYCs with worsening behaviors
     * GET /api/behaviorist-kyc/search/worsening
     */
    @GetMapping("/search/worsening")
    public ResponseEntity<?> getWorseningBehaviors() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getWorseningBehaviors();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching worsening behaviors", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get KYCs with children in home
     * GET /api/behaviorist-kyc/search/with-children
     */
    @GetMapping("/search/with-children")
    public ResponseEntity<?> getKycsWithChildren() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getKycsWithChildren();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching KYCs with children", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get KYCs preferring virtual sessions
     * GET /api/behaviorist-kyc/search/virtual-sessions
     */
    @GetMapping("/search/virtual-sessions")
    public ResponseEntity<?> getVirtualSessionPreferences() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getVirtualSessionPreferences();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching virtual session preferences", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get KYCs preferring in-person sessions
     * GET /api/behaviorist-kyc/search/in-person-sessions
     */
    @GetMapping("/search/in-person-sessions")
    public ResponseEntity<?> getInPersonSessionPreferences() {
        try {
            List<PetBehavioristKycEntity> kycs = behavioristKycService.getInPersonSessionPreferences();
            return ResponseEntity.ok(kycs);
        } catch (Exception ex) {
            logger.error("Error fetching in-person session preferences", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }
    
    @PutMapping("/{uid}/status")
    public ResponseEntity<?> updateKycStatus(@PathVariable String uid, @RequestParam String status) {
    try {
    UUID kycUid = UUID.fromString(uid);
    PetBehavioristKycEntity updated = behavioristKycService.updateKycStatus(kycUid, status);
    logger.info("KYC status updated for {} -> {}", kycUid, updated.getKycStatus());
    return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException iae) {
    logger.warn("Invalid UUID format: {}", uid);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UID format: " + uid);
    } catch (ValidationException ve) {
    logger.warn("ValidationException while updating KYC status: {}", ve.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getMessage());
    } catch (Exception ex) {
    logger.error("Error updating KYC status for UID: {}", uid, ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating status.");
    }
    }
    
    @GetMapping("/search/status")
    public ResponseEntity<?> getKycsByStatus(@RequestParam String status) {
    try {
    KycStatus s = KycStatus.valueOf(status.trim().toUpperCase());
    List<PetBehavioristKycEntity> kycs = behavioristKycService.getKycsByStatus(s);
    return ResponseEntity.ok(kycs);
    } catch (IllegalArgumentException iae) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status. Allowed: PENDING, APPROVED, REJECTED");
    } catch (Exception ex) {
    logger.error("Error fetching KYCs by status", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching KYCs.");
    }
    }
    
    
    
    
}