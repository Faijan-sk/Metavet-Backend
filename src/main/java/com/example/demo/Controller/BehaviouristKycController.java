package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.BehaviouristKycRequestDto;
import com.example.demo.Entities.BehaviouristKyc;
import com.example.demo.Entities.BehaviouristKyc.ApprovalStatus;
import com.example.demo.Repository.BehaviouristKycRepo;
import com.example.demo.Service.BehaviouristKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/api/behaviouristkyc")
public class BehaviouristKycController {
    
    private static final Logger logger = LoggerFactory.getLogger(BehaviouristKycController.class);
    
    @Autowired
    private BehaviouristKycService behaviouristKycService;
    
    @Autowired
    private BehaviouristKycRepo behaviouristKycRepo;
    
    private static final String DOCUMENT_ROOT = System.getProperty("user.dir");
    private static final String FOLDER_NAME = "behaviourist_kyc";
    private static final String FILE_DIR = DOCUMENT_ROOT + File.separator + FOLDER_NAME + File.separator;

    // ===================== CREATE KYC =====================
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBehaviouristKyc(
            @Valid @ModelAttribute BehaviouristKycRequestDto dto,
            BindingResult bindingResult) {
        
        try {
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for BehaviouristKycRequestDto: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }

            behaviouristKycService.createBehaviouristKyc(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Behaviourist KYC created successfully.");
        } catch (ValidationException ve) {
            logger.warn("ValidationException while creating Behaviourist KYC: {}", ve.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getMessage());
        } catch (IOException ioe) {
            logger.error("IOException while creating Behaviourist KYC", ioe);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process uploaded files.");
        } catch (Exception ex) {
            logger.error("Unexpected error while creating Behaviourist KYC", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // ===================== GET ALL KYC =====================
    @GetMapping("/all")
    public ResponseEntity<List<BehaviouristKycRequestDto>> getAllBehaviouristKyc() {
        return ResponseEntity.ok(behaviouristKycService.getAll());
    }

    // ===================== GET SINGLE KYC BY UID =====================
    @GetMapping("/uid/{uid}")
    public ResponseEntity<?> getBehaviouristKycByUid(@PathVariable UUID uid) {
        try {
            logger.info("Fetching Behaviourist KYC record with uid: {}", uid);
            BehaviouristKycRequestDto kyc = behaviouristKycService.getBehaviouristKycByUid(uid);
            return ResponseEntity.ok(kyc);
        } catch (ValidationException ve) {
            logger.warn("Behaviourist KYC not found with uid: {}", uid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
        } catch (Exception ex) {
            logger.error("Error fetching Behaviourist KYC with uid: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch KYC record: " + ex.getMessage());
        }
    }

    // ===================== DELETE KYC BY UID =====================
    @DeleteMapping("/uid/{uid}")
    public ResponseEntity<?> deleteBehaviouristKycByUid(@PathVariable UUID uid) {
        try {
            logger.info("Deleting Behaviourist KYC record with uid: {}", uid);
            behaviouristKycService.deleteBehaviouristKycByUid(uid);
            return ResponseEntity.ok("Behaviourist KYC deleted successfully.");
        } catch (ValidationException ve) {
            logger.warn("Behaviourist KYC not found for deletion with uid: {}", uid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
        } catch (Exception ex) {
            logger.error("Error deleting Behaviourist KYC with uid: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete KYC record: " + ex.getMessage());
        }
    }

    // ===================== GET STATUS BY UID =====================
    @GetMapping("/uid/{uid}/status")
    public ResponseEntity<?> getStatusByUid(@PathVariable UUID uid) {
        try {
            logger.info("Fetching status for Behaviourist KYC with uid: {}", uid);
            ApprovalStatus status = behaviouristKycService.getStatusByUid(uid);
            
            Map<String, Object> response = new HashMap<>();
            response.put("uid", uid);
            response.put("status", status);
            
            return ResponseEntity.ok(response);
        } catch (ValidationException ve) {
            logger.warn("Behaviourist KYC not found with uid: {}", uid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
        } catch (Exception ex) {
            logger.error("Error fetching status for Behaviourist KYC with uid: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch status: " + ex.getMessage());
        }
    }

    // ===================== UPDATE APPLICATION STATUS BY UID =====================
    @PatchMapping("/uid/{uid}/status")
    public ResponseEntity<?> updateApplicationStatusByUid(
            @PathVariable UUID uid,
            @RequestParam String status) {
        try {
            logger.info("Updating status for Behaviourist KYC with uid: {} to {}", uid, status);
            BehaviouristKycRequestDto updatedKyc = behaviouristKycService.updateApplicationStatusByUid(uid, status);
            return ResponseEntity.ok(updatedKyc);
        } catch (ValidationException ve) {
            logger.warn("Behaviourist KYC not found for status update with uid: {}", uid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
        } catch (Exception ex) {
            logger.error("Error updating status for Behaviourist KYC with uid: {}", uid, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update application status: " + ex.getMessage());
        }
    }

    // ===================== GET UPLOADED FILES =====================
    @GetMapping("/uploaded_files/{uid}/{fileType}")
    public ResponseEntity<Resource> getDocument(@PathVariable UUID uid, @PathVariable String fileType)
            throws MalformedURLException, ValidationException {

        BehaviouristKyc document = behaviouristKycRepo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Document not found"));

        // choose which stored file to return based on fileType
        byte[] fileBytes = null;
        String fileName = null;

        switch (fileType.toLowerCase()) {
        case "behavioural_certificate":
            fileBytes = document.getBehaviouralCertificateDoc();
            fileName = document.getBehaviouralCertificateFilePath();
            break;
        case "insurance":
            fileBytes = document.getInsuranceDoc();
            fileName = document.getInsuranceDocPath();
            break;
        case "criminal_record":
            fileBytes = document.getCriminalRecordDoc();
            fileName = document.getCriminalDocPath();
            break;
        case "liability_insurance":
            fileBytes = document.getLiabilityInsuranceDoc();
            fileName = document.getLiabilityDocPath();
            break;
        case "business_license":
            fileBytes = document.getBusinessLicenseDoc();
            fileName = document.getBusinessLicenseFilePath();
            break;
        default:
            throw new ValidationException("Unknown file type: " + fileType);
        }

        // Validate filename
        if ((fileName == null || fileName.isBlank()) && (fileBytes == null || fileBytes.length == 0)) {
            throw new ValidationException("Requested file not available for this UID and type.");
        }

        // If bytes exist in DB, stream those
        if (fileBytes != null && fileBytes.length > 0) {
            ByteArrayResource resource = new ByteArrayResource(fileBytes);

            // determine media type from filename if available, else default
            MediaType mediaType = MediaTypeFactory.getMediaType(fileName != null ? fileName : "file")
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            String dispositionName = (fileName != null && !fileName.isBlank())
                    ? Paths.get(fileName).getFileName().toString()
                    : fileType + "-" + uid.toString();

            return ResponseEntity.ok().contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dispositionName + "\"")
                    .body(resource);
        }

        // else read from disk using DOCUMENT_ROOT + fileName
        Path filePath = Paths.get(DOCUMENT_ROOT).resolve(fileName).normalize();

        // Prevent path traversal
        Path rootPath = Paths.get(DOCUMENT_ROOT).toAbsolutePath().normalize();
        if (!filePath.toAbsolutePath().startsWith(rootPath)) {
            throw new ValidationException("Invalid file path");
        }

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new ValidationException("File not found on disk or not readable");
        }

        Resource resource = new UrlResource(filePath.toUri());
        MediaType mediaType = MediaTypeFactory.getMediaType(filePath.getFileName().toString())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().contentType(mediaType).header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filePath.getFileName().toString() + "\"").body(resource);
    }
}