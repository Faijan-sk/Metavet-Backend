package com.example.demo.Controller;

import java.util.Optional;

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
}