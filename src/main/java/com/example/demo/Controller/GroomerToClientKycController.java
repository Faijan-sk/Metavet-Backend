package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.GroomerToClientKycRequestDto;
import com.example.demo.Entities.GroomerToClientKycEntity;
import com.example.demo.Entities.GroomerToClientKycEntity.KycStatus;
import com.example.demo.Service.GroomerToClientKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/api/groomer-kyc")
public class GroomerToClientKycController {

    private static final Logger logger = LoggerFactory.getLogger(GroomerToClientKycController.class);

    @Autowired
    private GroomerToClientKycService groomerKycService;

    /**
     * Create new Groomer KYC
     * POST /api/groomer-kyc
     * 
     * Accepts JSON payload from frontend + Authorization header
     */
    @PostMapping
    public ResponseEntity<?> createGroomerKyc(
            @Valid @RequestBody GroomerToClientKycRequestDto dto,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            BindingResult bindingResult) {

        try {
            logger.info("Received Groomer KYC creation request for petUid: {}", dto.getPetUid());
            
            // Check for validation errors from @Valid
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for GroomerToClientKycRequestDto: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(buildErrorResponse("Validation failed", bindingResult.getAllErrors().toString()));
            }

            // Extract access token from Authorization header
            String accessToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            }

            if (accessToken == null || accessToken.trim().isEmpty()) {
                logger.warn("Access token missing in Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(buildErrorResponse("Unauthorized", "Authorization token is required"));
            }

            // Call service to create KYC
            GroomerToClientKycEntity createdKyc = groomerKycService.createGroomerKyc(dto, accessToken);
            
            logger.info("Groomer KYC created successfully with ID: {} for pet: {}", 
                       createdKyc.getId(), 
                       createdKyc.getPet() != null ? createdKyc.getPet().getPetName() : "unknown");
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(buildSuccessResponse("KYC created successfully", createdKyc));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while creating Groomer KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Validation error", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Unexpected error while creating Groomer KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    /**
     * Update existing Groomer KYC
     * PUT /api/groomer-kyc/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroomerKyc(
            @PathVariable Long id,
            @Valid @RequestBody GroomerToClientKycRequestDto dto,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            BindingResult bindingResult) {

        try {
            logger.info("Received Groomer KYC update request for ID: {}", id);
            
            // Check for validation errors
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for updating Groomer KYC: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(buildErrorResponse("Validation failed", bindingResult.getAllErrors().toString()));
            }

            // Extract access token
            String accessToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            }

            if (accessToken == null || accessToken.trim().isEmpty()) {
                logger.warn("Access token missing in Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(buildErrorResponse("Unauthorized", "Authorization token is required"));
            }

            // Call service to update KYC
            GroomerToClientKycEntity updatedKyc = groomerKycService.updateGroomerKyc(id, dto, accessToken);
            
            logger.info("Groomer KYC updated successfully with ID: {}", id);
            return ResponseEntity.ok(buildSuccessResponse("KYC updated successfully", updatedKyc));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while updating Groomer KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Validation error", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Unexpected error while updating Groomer KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    /**
     * NEW: Get All Groomer KYCs
     * GET /api/groomer-kyc
     */
    @GetMapping
    public ResponseEntity<?> getAllGroomerKycs() {
        try {
            List<GroomerToClientKycEntity> kycs = groomerKycService.getAllGroomerKycs();
            
            logger.info("Retrieved {} KYC records", kycs.size());
            return ResponseEntity.ok(buildSuccessResponse("KYCs retrieved successfully", kycs));

        } catch (Exception ex) {
            logger.error("Error fetching all Groomer KYCs", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching KYCs"));
        }
    }

    /**
     * NEW: Get Groomer KYC by UID
     * GET /api/groomer-kyc/uid/{uid}
     */
    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getGroomerKycByUid(@PathVariable String uid) {
        try {
            Optional<GroomerToClientKycEntity> kyc = groomerKycService.getGroomerKycByUid(uid);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(buildSuccessResponse("KYC found", kyc.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(buildErrorResponse("Not found", "Groomer KYC with UID " + uid + " not found"));
            }

        } catch (ValidationException ve) {
            logger.warn("ValidationException while fetching KYC by UID: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Validation error", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Error fetching Groomer KYC by UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching KYC"));
        }
    }

    /**
     * NEW: Get KYC Status by UID
     * GET /api/groomer-kyc/status/{uid}
     */
    @GetMapping("/status/{uid}")
    public ResponseEntity<?> getKycStatusByUid(@PathVariable String uid) {
        try {
            KycStatus status = groomerKycService.getKycStatusByUid(uid);
            
            Map<String, Object> response = new HashMap<>();
            response.put("uid", uid);
            response.put("status", status.toString());
            
            return ResponseEntity.ok(buildSuccessResponse("Status retrieved successfully", response));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while fetching KYC status: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Validation error", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Error fetching KYC status by UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching status"));
        }
    }

    /**
     * NEW: Update KYC Status by UID
     * PATCH /api/groomer-kyc/status/{uid}
     * Body: { "status": "APPROVED" }
     */
    @PatchMapping("/status/{uid}")
    public ResponseEntity<?> updateKycStatusByUid(
            @PathVariable String uid,
            @RequestBody Map<String, String> statusRequest) {
        
        try {
            String status = statusRequest.get("status");
            
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(buildErrorResponse("Bad request", "Status field is required"));
            }

            GroomerToClientKycEntity updatedKyc = groomerKycService.updateKycStatusByUid(uid, status);
            
            logger.info("KYC status updated successfully for UID: {} to {}", uid, status);
            return ResponseEntity.ok(buildSuccessResponse("Status updated successfully", updatedKyc));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while updating KYC status: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Validation error", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Error updating KYC status by UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while updating status"));
        }
    }

    /**
     * NEW: Delete Groomer KYC by UID
     * DELETE /api/groomer-kyc/uid/{uid}
     */
    @DeleteMapping("/uid/{uid}")
    public ResponseEntity<?> deleteGroomerKycByUid(@PathVariable String uid) {
        try {
            groomerKycService.deleteGroomerKycByUid(uid);
            
            logger.info("Groomer KYC deleted successfully with UID: {}", uid);
            return ResponseEntity.ok(buildSuccessResponse("KYC deleted successfully", null));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while deleting Groomer KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(buildErrorResponse("Not found", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Error deleting Groomer KYC with UID: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while deleting KYC"));
        }
    }

    /**
     * Get Groomer KYC by ID
     * GET /api/groomer-kyc/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroomerKycById(@PathVariable Long id) {
        try {
            Optional<GroomerToClientKycEntity> kyc = groomerKycService.getGroomerKycById(id);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(buildSuccessResponse("KYC found", kyc.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(buildErrorResponse("Not found", "Groomer KYC with ID " + id + " not found"));
            }

        } catch (Exception ex) {
            logger.error("Error fetching Groomer KYC by ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching KYC"));
        }
    }

    /**
     * Get Groomer KYC by Pet ID (Long ID)
     * GET /api/groomer-kyc/pet/{petId}
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getGroomerKycByPetId(@PathVariable Long petId) {
        try {
            Optional<GroomerToClientKycEntity> kyc = groomerKycService.getGroomerKycByPetId(petId);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(buildSuccessResponse("KYC found", kyc.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(buildErrorResponse("Not found", "Groomer KYC for Pet ID " + petId + " not found"));
            }

        } catch (Exception ex) {
            logger.error("Error fetching Groomer KYC by Pet ID: {}", petId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching KYC"));
        }
    }
    
    /**
     * Get Groomer KYC by Pet UID (UUID String)
     * GET /api/groomer-kyc/pet-uid/{petUid}
     */
    @GetMapping("/pet-uid/{petUid}")
    public ResponseEntity<?> getGroomerKycByPetUid(@PathVariable String petUid) {
        try {
            Optional<GroomerToClientKycEntity> kyc = groomerKycService.getGroomerKycByPetUid(petUid);
            
            if (kyc.isPresent()) {
                return ResponseEntity.ok(buildSuccessResponse("KYC found", kyc.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(buildErrorResponse("Not found", "Groomer KYC for Pet UID " + petUid + " not found"));
            }

        } catch (Exception ex) {
            logger.error("Error fetching Groomer KYC by Pet UID: {}", petUid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while fetching KYC"));
        }
    }

    /**
     * Delete Groomer KYC by ID
     * DELETE /api/groomer-kyc/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroomerKyc(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Extract access token
            String accessToken = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            }

            if (accessToken == null || accessToken.trim().isEmpty()) {
                logger.warn("Access token missing in Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(buildErrorResponse("Unauthorized", "Authorization token is required"));
            }

            groomerKycService.deleteGroomerKyc(id, accessToken);
            
            logger.info("Groomer KYC deleted successfully with ID: {}", id);
            return ResponseEntity.ok(buildSuccessResponse("KYC deleted successfully", null));

        } catch (ValidationException ve) {
            logger.warn("ValidationException while deleting Groomer KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(buildErrorResponse("Not found", ve.getMessage()));

        } catch (Exception ex) {
            logger.error("Error deleting Groomer KYC with ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildErrorResponse("Internal server error", "An error occurred while deleting KYC"));
        }
    }

    // ==================== Helper Methods ====================

    private Map<String, Object> buildSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }

    private Map<String, Object> buildErrorResponse(String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}