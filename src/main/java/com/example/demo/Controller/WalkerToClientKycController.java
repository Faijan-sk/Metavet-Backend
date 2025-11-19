package com.example.demo.Controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.WalkerToClientKycRequestDto;
import com.example.demo.Entities.WalkerToClientKycEntity;
import com.example.demo.Service.WalkerToClientKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/api/walker-kyc")
public class WalkerToClientKycController {

    private static final Logger logger = LoggerFactory.getLogger(WalkerToClientKycController.class);

    @Autowired
    private WalkerToClientKycService walkerKycService;

    /**
     * Create new Walker KYC
     * POST /api/walker-kyc
     * 
     * Accepts JSON payload from frontend
     */
    @PostMapping
    public ResponseEntity<?> createWalkerKyc(
            @Valid @RequestBody WalkerToClientKycRequestDto dto,
            BindingResult bindingResult) {

        try {
            logger.info("Received Walker KYC creation request for petUid: {}", dto.getPetUid());
            
            // Check for validation errors from @Valid
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for WalkerToClientKycRequestDto: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(bindingResult.getAllErrors());
            }

            // Call service to create KYC
            WalkerToClientKycEntity createdKyc = walkerKycService.createWalkerKyc(dto);
            
            logger.info("Walker KYC created successfully with ID: {} for pet: {}", 
                       createdKyc.getId(), 
                       createdKyc.getPet() != null ? createdKyc.getPet().getPetName() : "unknown");
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdKyc);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while creating Walker KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Unexpected error while creating Walker KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Update existing Walker KYC
     * PUT /api/walker-kyc/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWalkerKyc(
            @PathVariable Long id,
            @Valid @RequestBody WalkerToClientKycRequestDto dto,
            BindingResult bindingResult) {

        try {
            logger.info("Received Walker KYC update request for ID: {}", id);
            
            // Check for validation errors
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for updating Walker KYC: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(bindingResult.getAllErrors());
            }

            // Call service to update KYC
            WalkerToClientKycEntity updatedKyc = walkerKycService.updateWalkerKyc(id, dto);
            
            logger.info("Walker KYC updated successfully with ID: {}", id);
            return ResponseEntity.ok(updatedKyc);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while updating Walker KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Unexpected error while updating Walker KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * ==================== NEW: Get All Walker KYCs ====================
     * GET /api/walker-kyc
     */
    @GetMapping
    public ResponseEntity<?> getAllWalkerKycs() {
        try {
            List<WalkerToClientKycEntity> kycs = walkerKycService.getAllWalkerKycs();
            
            logger.info("Retrieved {} Walker KYCs", kycs.size());
            return ResponseEntity.ok(kycs);

        } catch (Exception ex) {
            logger.error("Error fetching all Walker KYCs", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYCs.");
        }
    }

    /**
     * Get Walker KYC by ID
     * GET /api/walker-kyc/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWalkerKycById(@PathVariable Long id) {
        try {
            Optional<WalkerToClientKycEntity> kyc = walkerKycService.getWalkerKycById(id);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Walker KYC with ID " + id + " not found.");
            }

        } catch (Exception ex) {
            logger.error("Error fetching Walker KYC by ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }

    /**
     * ==================== NEW: Get Walker KYC by UID ====================
     * GET /api/walker-kyc/uid/{uid}
     */
    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getWalkerKycByUid(@PathVariable String uid) {
        try {
            UUID uuid = UUID.fromString(uid);
            Optional<WalkerToClientKycEntity> kyc = walkerKycService.getWalkerKycByUid(uuid);
            
            if (kyc.isPresent()) {
                logger.info("Retrieved Walker KYC by UID: {}", uid);
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Walker KYC with UID " + uid + " not found.");
            }

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid UID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (Exception ex) {
            logger.error("Error fetching Walker KYC by UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }

    /**
     * Get Walker KYC by Pet ID (Long ID)
     * GET /api/walker-kyc/pet/{petId}
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getWalkerKycByPetId(@PathVariable Long petId) {
        try {
            Optional<WalkerToClientKycEntity> kyc = walkerKycService.getWalkerKycByPetId(petId);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Walker KYC for Pet ID " + petId + " not found.");
            }

        } catch (Exception ex) {
            logger.error("Error fetching Walker KYC by Pet ID: {}", petId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }
    
    /**
     * Get Walker KYC by Pet UID (UUID String)
     * GET /api/walker-kyc/pet-uid/{petUid}
     */
    @GetMapping("/pet-uid/{petUid}")
    public ResponseEntity<?> getWalkerKycByPetUid(@PathVariable String petUid) {
        try {
            Optional<WalkerToClientKycEntity> kyc = walkerKycService.getWalkerKycByPetUid(petUid);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(kyc.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Walker KYC for Pet UID " + petUid + " not found.");
            }

        } catch (Exception ex) {
            logger.error("Error fetching Walker KYC by Pet UID: {}", petUid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching KYC.");
        }
    }

    /**
     * ==================== NEW: Update Status by UID ====================
     * PATCH /api/walker-kyc/uid/{uid}/status
     * 
     * Request Body: { "status": "APPROVED" }
     */
    @PatchMapping("/uid/{uid}/status")
    public ResponseEntity<?> updateStatusByUid(
            @PathVariable String uid,
            @RequestBody Map<String, String> statusUpdate) {
        
        try {
            UUID uuid = UUID.fromString(uid);
            String status = statusUpdate.get("status");
            
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Status field is required in request body.");
            }
            
            WalkerToClientKycEntity updatedKyc = walkerKycService.updateStatusByUid(uuid, status);
            
            logger.info("Walker KYC status updated successfully for UID: {} to status: {}", uid, status);
            return ResponseEntity.ok(updatedKyc);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid UID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while updating status: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Unexpected error while updating status for UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Delete Walker KYC
     * DELETE /api/walker-kyc/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWalkerKyc(@PathVariable Long id) {
        try {
            walkerKycService.deleteWalkerKyc(id);
            
            logger.info("Walker KYC deleted successfully with ID: {}", id);
            return ResponseEntity.ok("Walker KYC deleted successfully.");

        } catch (ValidationException ve) {
            logger.warn("ValidationException while deleting Walker KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Error deleting Walker KYC with ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting KYC.");
        }
    }

    /**
     * ==================== NEW: Delete Walker KYC by UID ====================
     * DELETE /api/walker-kyc/uid/{uid}
     */
    @DeleteMapping("/uid/{uid}")
    public ResponseEntity<?> deleteWalkerKycByUid(@PathVariable String uid) {
        try {
            UUID uuid = UUID.fromString(uid);
            walkerKycService.deleteWalkerKycByUid(uuid);
            
            logger.info("Walker KYC deleted successfully with UID: {}", uid);
            return ResponseEntity.ok("Walker KYC deleted successfully.");

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid UID format: {}", uid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid UID format: " + uid);

        } catch (ValidationException ve) {
            logger.warn("ValidationException while deleting Walker KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ve.getMessage());

        } catch (Exception ex) {
            logger.error("Error deleting Walker KYC with UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting KYC.");
        }
    }
}