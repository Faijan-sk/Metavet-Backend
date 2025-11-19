package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dto.BehaviouristKycRequestDto;
import com.example.demo.Entities.BehaviouristKyc;
import com.example.demo.Entities.BehaviouristKyc.ApprovalStatus;
import com.example.demo.Repository.BehaviouristKycRepo;

import jakarta.validation.ValidationException;

@Service
public class BehaviouristKycService {

    @Autowired
    private BehaviouristKycRepo behaviouristKycRepo;

    private static final String DOCUMENT_ROOT = System.getProperty("user.dir");
    private static final String FOLDER_NAME = "behaviourist_kyc";
    private static final String FILE_DIR = DOCUMENT_ROOT + File.separator + FOLDER_NAME + File.separator;

    public void createBehaviouristKyc(BehaviouristKycRequestDto dto) throws IOException, ValidationException {

        BehaviouristKyc kyc = behaviouristKycRepo.findByEmail(dto.getEmail()).orElse(new BehaviouristKyc());

        List<String> allowedExtensions = Arrays.asList("pdf", "jpeg", "jpg", "png", "doc", "docx");

        // ---------------- Personal & Business Information ----------------
        kyc.setFullLegalName(dto.getFullLegalName());
        kyc.setBusinessName(dto.getBusinessName());
        kyc.setEmail(dto.getEmail());
        kyc.setPhone(dto.getPhone());
        kyc.setAddress(dto.getAddress());
        kyc.setServiceArea(dto.getServiceArea());
        kyc.setYearsExperience(dto.getYearsExperience());

        // ---------------- Professional Credentials ----------------
        if (dto.getHasBehaviouralCertifications() != null && dto.getHasBehaviouralCertifications().equals(Boolean.TRUE)) {
            kyc.setHasBehaviouralCertifications(dto.getHasBehaviouralCertifications());
            
            if (dto.getBehaviouralCertificateDetails() != null) {
                kyc.setBehaviouralCertificateDetails(dto.getBehaviouralCertificateDetails());
            }
            
            if (dto.getBehaviouralCertificateDoc() != null && !dto.getBehaviouralCertificateDoc().isEmpty()) {
                byte[] fileBytes = dto.getBehaviouralCertificateDoc().getBytes();
                String path = saveFile(dto.getBehaviouralCertificateDoc(), "behavioural_certificate", 
                                      dto.getEmail(), allowedExtensions);
                kyc.setBehaviouralCertificateDoc(fileBytes);
                kyc.setBehaviouralCertificateFilePath(path);
            } else {
                throw new ValidationException("Behavioural Certificate Document is required.");
            }
        }

        if (dto.getEducationBackground() != null) {
            kyc.setEducationBackground(dto.getEducationBackground());
        }

        // ---------------- Insurance ----------------
        if (dto.getHasInsurance() != null && dto.getHasInsurance().equals(Boolean.TRUE)) {
            kyc.setHasInsurance(dto.getHasInsurance());
            
            if (dto.getInsuranceProvider() != null) {
                kyc.setInsuranceProvider(dto.getInsuranceProvider());
            } else {
                throw new ValidationException("Insurance Provider name is required.");
            }

            if (dto.getInsurancePolicyNumber() != null) {
                kyc.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
            } else {
                throw new ValidationException("Insurance Policy Number is required.");
            }

            if (dto.getInsuranceExpiry() != null) {
                kyc.setInsuranceExpiry(dto.getInsuranceExpiry());
            } else {
                throw new ValidationException("Insurance Expiry date is required.");
            }

            if (dto.getInsuranceDoc() != null && !dto.getInsuranceDoc().isEmpty()) {
                byte[] fileBytes = dto.getInsuranceDoc().getBytes();
                String path = saveFile(dto.getInsuranceDoc(), "insurance", dto.getEmail(), allowedExtensions);
                kyc.setInsuranceDoc(fileBytes);
                kyc.setInsuranceDocPath(path);
            } else {
                throw new ValidationException("Insurance Document is required.");
            }
        }

        // ---------------- Criminal Check ----------------
        if (dto.getHasCriminalCheck() != null && dto.getHasCriminalCheck().equals(Boolean.TRUE)) {
            kyc.setHasCriminalCheck(dto.getHasCriminalCheck());
            
            if (dto.getCriminalRecordDoc() != null && !dto.getCriminalRecordDoc().isEmpty()) {
                byte[] fileBytes = dto.getCriminalRecordDoc().getBytes();
                String path = saveFile(dto.getCriminalRecordDoc(), "criminal_record", 
                                      dto.getEmail(), allowedExtensions);
                kyc.setCriminalRecordDoc(fileBytes);
                kyc.setCriminalDocPath(path);
            } else {
                throw new ValidationException("Criminal Record Document is required.");
            }
        }

        // ---------------- Liability Insurance ----------------
        if (dto.getLiabilityInsurance() != null && dto.getLiabilityInsurance().equals(Boolean.TRUE)) {
            kyc.setLiabilityInsurance(dto.getLiabilityInsurance());
            
            if (dto.getLiabilityInsuranceDoc() != null && !dto.getLiabilityInsuranceDoc().isEmpty()) {
                byte[] fileBytes = dto.getLiabilityInsuranceDoc().getBytes();
                String path = saveFile(dto.getLiabilityInsuranceDoc(), "liability_insurance", 
                                      dto.getEmail(), allowedExtensions);
                kyc.setLiabilityInsuranceDoc(fileBytes);
                kyc.setLiabilityDocPath(path);
            } else {
                throw new ValidationException("Liability Insurance Document is required.");
            }
        }

        // ---------------- Business License ----------------
        if (dto.getHasBusinessLicense() != null && dto.getHasBusinessLicense().equals(Boolean.TRUE)) {
            kyc.setHasBusinessLicense(dto.getHasBusinessLicense());
            
            if (dto.getBusinessLicenseDoc() != null && !dto.getBusinessLicenseDoc().isEmpty()) {
                byte[] fileBytes = dto.getBusinessLicenseDoc().getBytes();
                String path = saveFile(dto.getBusinessLicenseDoc(), "business_license", 
                                      dto.getEmail(), allowedExtensions);
                kyc.setBusinessLicenseDoc(fileBytes);
                kyc.setBusinessLicenseFilePath(path);
            } else {
                throw new ValidationException("Business License Document is required.");
            }
        }

        // ---------------- Practice Details ----------------
        if (dto.getServicesOffered() != null && !dto.getServicesOffered().isEmpty()) {
            kyc.setServicesOffered(dto.getServicesOffered());
        }

        if (dto.getServicesOtherText() != null) {
            kyc.setServicesOtherText(dto.getServicesOtherText());
        }

        if (dto.getSpecializations() != null && !dto.getSpecializations().isEmpty()) {
            kyc.setSpecializations(dto.getSpecializations());
        }

        if (dto.getSpecializationOtherText() != null) {
            kyc.setSpecializationOtherText(dto.getSpecializationOtherText());
        }

        if (dto.getServiceRadius() != null) {
            kyc.setServiceRadius(dto.getServiceRadius());
        }

        // ---------------- Declarations ----------------
        if (dto.getInfoTrue() != null && dto.getInfoTrue().equals(Boolean.TRUE)) {
            kyc.setInfoTrue(dto.getInfoTrue());
        } else {
            throw new ValidationException("Declaration - Information Accuracy is required.");
        }

        if (dto.getVerifyOk() != null && dto.getVerifyOk().equals(Boolean.TRUE)) {
            kyc.setVerifyOk(dto.getVerifyOk());
        } else {
            throw new ValidationException("Declaration - Verification OK is required.");
        }

        if (dto.getAbideStandards() != null && dto.getAbideStandards().equals(Boolean.TRUE)) {
            kyc.setAbideStandards(dto.getAbideStandards());
        } else {
            throw new ValidationException("Declaration - Abide by Standards is required.");
        }

        if (dto.getSignature() != null) {
            kyc.setSignature(dto.getSignature());
        } else {
            throw new ValidationException("Signature is required.");
        }

        if (dto.getSignatureDate() != null) {
            kyc.setSignatureDate(dto.getSignatureDate());
        } else {
            throw new ValidationException("Signature Date is required.");
        }

        behaviouristKycRepo.save(kyc);
    }

    // ===================== GET ALL METHOD =====================
    public List<BehaviouristKycRequestDto> getAll() {
        List<BehaviouristKyc> allDocuments = behaviouristKycRepo.findAllByOrderByCreatedAtDesc();
        System.out.println("***************" + allDocuments.size());
        return allDocuments.stream().map(this::copyToDto).collect(Collectors.toList());
    }

    // ===================== GET BY UID METHOD =====================
    public BehaviouristKycRequestDto getBehaviouristKycByUid(UUID uid) throws ValidationException {
        BehaviouristKyc kyc = behaviouristKycRepo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Behaviourist KYC not found with uid: " + uid));
        return copyToDto(kyc);
    }

    // ===================== DELETE BY UID METHOD =====================
    public void deleteBehaviouristKycByUid(UUID uid) throws ValidationException {
        BehaviouristKyc kyc = behaviouristKycRepo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Behaviourist KYC not found with uid: " + uid));

        // Delete associated files from filesystem
        deleteAssociatedFiles(kyc);

        behaviouristKycRepo.delete(kyc);
    }

    // ===================== GET STATUS BY UID METHOD =====================
    public ApprovalStatus getStatusByUid(UUID uid) throws ValidationException {
        BehaviouristKyc kyc = behaviouristKycRepo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Behaviourist KYC not found with uid: " + uid));
        return kyc.getStatus();
    }

    // ===================== UPDATE STATUS BY UID METHOD =====================
    public BehaviouristKycRequestDto updateApplicationStatusByUid(UUID uid, String status) throws ValidationException {
        BehaviouristKyc kyc = behaviouristKycRepo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Behaviourist KYC not found with uid: " + uid));

        // Convert string to ApprovalStatus enum
        ApprovalStatus approvalStatus;
        try {
            approvalStatus = ApprovalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status value. Allowed values: PENDING, APPROVED, REJECTED, UNDER_REVIEW");
        }

        kyc.setStatus(approvalStatus);
        BehaviouristKyc updatedKyc = behaviouristKycRepo.save(kyc);

        return copyToDto(updatedKyc);
    }

    // ===================== HELPER METHODS =====================

    private BehaviouristKycRequestDto copyToDto(BehaviouristKyc entity) {
        BehaviouristKycRequestDto dto = new BehaviouristKycRequestDto();

        // Copy all properties using BeanUtils
        BeanUtils.copyProperties(entity, dto);

        // Explicitly set BaseEntity fields
        dto.setId(entity.getId());
        dto.setUid(entity.getUid());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // base endpoint (relative). If you want absolute URL include host via ServletUriComponentsBuilder.
        String base = "/api/behaviouristkyc/uploaded_files/" + entity.getUid();

        // For each stored file, set path (filename) and a type-specific URL
        if (entity.getBehaviouralCertificateFilePath() != null && !entity.getBehaviouralCertificateFilePath().isBlank()) {
            dto.setBehaviouralCertificateFilePath(entity.getBehaviouralCertificateFilePath());
            dto.setBehaviouralCertificateFileURL(base + "/behavioural_certificate");
        }
        if (entity.getInsuranceDocPath() != null && !entity.getInsuranceDocPath().isBlank()) {
            dto.setInsuranceDocPath(entity.getInsuranceDocPath());
            dto.setInsuranceDocURL(base + "/insurance");
        }
        if (entity.getCriminalDocPath() != null && !entity.getCriminalDocPath().isBlank()) {
            dto.setCriminalDocPath(entity.getCriminalDocPath());
            dto.setCriminalDocURL(base + "/criminal_record");
        }
        if (entity.getLiabilityDocPath() != null && !entity.getLiabilityDocPath().isBlank()) {
            dto.setLiabilityDocPath(entity.getLiabilityDocPath());
            dto.setLiabilityDocURL(base + "/liability_insurance");
        }
        if (entity.getBusinessLicenseFilePath() != null && !entity.getBusinessLicenseFilePath().isBlank()) {
            dto.setBusinessLicenseFilePath(entity.getBusinessLicenseFilePath());
            dto.setBusinessLicenseFileURL(base + "/business_license");
        }

        return dto;
    }

    private void deleteAssociatedFiles(BehaviouristKyc kyc) {
        // Delete all associated files from filesystem
        deleteFileIfExists(kyc.getBehaviouralCertificateFilePath());
        deleteFileIfExists(kyc.getInsuranceDocPath());
        deleteFileIfExists(kyc.getCriminalDocPath());
        deleteFileIfExists(kyc.getLiabilityDocPath());
        deleteFileIfExists(kyc.getBusinessLicenseFilePath());
    }

    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Log error but don't throw exception
                System.err.println("Failed to delete file: " + filePath);
            }
        }
    }

    // ============================================ File Save Helper ============================================
    private String saveFile(MultipartFile file, String type, String identifier, List<String> allowedExtensions)
            throws IOException, ValidationException {

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if (!allowedExtensions.contains(ext)) {
            throw new ValidationException("Invalid file type for: " + originalFilename);
        }

        // Create absolute path in project directory
        String folderPath = DOCUMENT_ROOT + File.separator + FOLDER_NAME + File.separator + identifier + File.separator;
        Path directory = Paths.get(folderPath);

        // Create directories if they don't exist
        Files.createDirectories(directory);

        // Create full file path
        String fileName = type + "_" + System.currentTimeMillis() + "." + ext;
        String filePath = folderPath + fileName;

        // Save file
        File destFile = new File(filePath);
        file.transferTo(destFile);

        return filePath;
    }
}