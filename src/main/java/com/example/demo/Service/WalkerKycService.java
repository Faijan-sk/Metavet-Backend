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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dto.WalkerKycRequestDto;
import com.example.demo.Entities.WalkerKyc;
import com.example.demo.Repository.WalkerKycRepo;

import jakarta.validation.ValidationException;

@Service
public class WalkerKycService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private WalkerKycRepo walkerKycRepository;

    private static final String DOCUMENT_ROOT = System.getProperty("user.dir");
    private static final String QrFolder = "walker_kyc";
    private static final String Qr_FILE_DIR = DOCUMENT_ROOT + File.separator + QrFolder + File.separator;

    WalkerKycService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // ===================== CREATE METHOD =====================
    public void createWalkerKyc(WalkerKycRequestDto dto, List<String> documentNames,
            List<MultipartFile> documentFiles) throws IOException, ValidationException, java.io.IOException {

        WalkerKyc kyc = walkerKycRepository.findByEmail(dto.getEmail()).orElse(new WalkerKyc());

        kyc.setFullLegalName(dto.getFullLegalName());
        kyc.setBusinessName(dto.getBusinessName());

        List<String> allowedExtensions = Arrays.asList("pdf", "jpeg", "jpg", "png", "doc", "docx");

        kyc.setEmail(dto.getEmail());
        kyc.setPhone(dto.getPhone());
        kyc.setAddress(dto.getAddress());
        kyc.setServiceArea(dto.getServiceArea());
        kyc.setYearsExperience(dto.getYearsExperience());

        // ============================================ Professional Credentials ============================================

        if (dto.getHasPetCareCertifications() != null && dto.getHasPetCareCertifications().equals(Boolean.TRUE)) {
            kyc.setHasPetCareCertifications(dto.getHasPetCareCertifications());
            if (dto.getHasPetCareCertificationsDetails() != null) {
                kyc.setHasPetCareCertificationsDetails(dto.getHasPetCareCertificationsDetails());
            }
            if (dto.getPetCareCertificationDoc() != null && !dto.getPetCareCertificationDoc().isEmpty()) {
                byte[] fileBytes = dto.getPetCareCertificationDoc().getBytes();
                String path = saveFile(dto.getPetCareCertificationDoc(), "pet_care_certification", dto.getEmail(),
                        allowedExtensions);
                kyc.setPetCareCertificationDoc(fileBytes);
                kyc.setCertificationFilePath(path);
            } else {
                throw new ValidationException("Pet Care Certification Document is required.");
            }
        }

        if (dto.getBondedOrInsured() != null && dto.getBondedOrInsured().equals(Boolean.TRUE)) {
            kyc.setBondedOrInsured(dto.getBondedOrInsured());
            if (dto.getBondedOrInsuredDoc() != null && !dto.getBondedOrInsuredDoc().isEmpty()) {
                byte[] fileBytes = dto.getBondedOrInsuredDoc().getBytes();
                String path = saveFile(dto.getBondedOrInsuredDoc(), "bonded_or_insured", dto.getEmail(),
                        allowedExtensions);
                kyc.setBondedOrInsuredDoc(fileBytes);
                kyc.setBondedFilePath(path);
            } else {
                throw new ValidationException("Bonded or Insured Document is required.");
            }
        }

        if (dto.getHasFirstAid() != null && dto.getHasFirstAid().equals(Boolean.TRUE)) {
            kyc.setHasFirstAid(dto.getHasFirstAid());
            if (dto.getPetFirstAidCertificateDoc() != null && !dto.getPetFirstAidCertificateDoc().isEmpty()) {
                byte[] fileBytes = dto.getPetFirstAidCertificateDoc().getBytes();
                String path = saveFile(dto.getPetFirstAidCertificateDoc(), "pet_first_aid_certificate", dto.getEmail(),
                        allowedExtensions);
                kyc.setPetFirstAidCertificateDoc(fileBytes);
                kyc.setFirstAidFilePath(path);
            } else {
                throw new ValidationException("Pet First Aid Certificate Document is required.");
            }
        }

        if (dto.getCriminalCheck() != null && dto.getCriminalCheck().equals(Boolean.TRUE)) {
            kyc.setCriminalCheck(dto.getCriminalCheck());
            if (dto.getCrimialRecordDoc() != null && !dto.getCrimialRecordDoc().isEmpty()) {
                byte[] fileBytes = dto.getCrimialRecordDoc().getBytes();
                String path = saveFile(dto.getCrimialRecordDoc(), "criminal_record", dto.getEmail(), allowedExtensions);
                kyc.setCrimialRecordDoc(fileBytes);
                kyc.setCriminalCheckFilePath(path);
            } else {
                throw new ValidationException("Criminal Record Document is required.");
            }
        }

        // ============================================ Liability and Insurance ============================================

        if (dto.getLiabilityInsurance() != null && dto.getLiabilityInsurance().equals(Boolean.TRUE)) {
            kyc.setLiabilityInsurance(dto.getLiabilityInsurance());
            if (dto.getLiabilityProvider() != null) {
                kyc.setLiabilityProvider(dto.getLiabilityProvider());
            } else {
                throw new ValidationException("Liability Provider name is required.");
            }

            if (dto.getLiabilityPolicyNumber() != null) {
                kyc.setLiabilityPolicyNumber(dto.getLiabilityPolicyNumber());
            } else {
                throw new ValidationException("Liability Policy Number is required.");
            }

            if (dto.getInsuranceExpiry() != null) {
                kyc.setInsuranceExpiry(dto.getInsuranceExpiry());
            } else {
                throw new ValidationException("Insurance Expiry is required.");
            }

            if (dto.getLiabilityInsuaranceDoc() != null && !dto.getLiabilityInsuaranceDoc().isEmpty()) {
                byte[] fileBytes = dto.getLiabilityInsuaranceDoc().getBytes();
                String path = saveFile(dto.getLiabilityInsuaranceDoc(), "liability_insurance", dto.getEmail(),
                        allowedExtensions);
                kyc.setLiabilityInsuaranceDoc(fileBytes);
                kyc.setLiabilityFilePath(path);
            } else {
                throw new ValidationException("Liability Insurance Document is required.");
            }
        }

        if (dto.getHasBusinessLicenseDoc() != null && dto.getHasBusinessLicenseDoc().equals(Boolean.TRUE)) {
            kyc.setHasBusinessLicenseDoc(dto.getHasBusinessLicenseDoc());
            if (dto.getBusinessLicenseDoc() != null && !dto.getBusinessLicenseDoc().isEmpty()) {
                byte[] fileBytes = dto.getBusinessLicenseDoc().getBytes();
                String path = saveFile(dto.getBusinessLicenseDoc(), "business_license", dto.getEmail(),
                        allowedExtensions);
                kyc.setBusinessLicenseDoc(fileBytes);
                kyc.setBusinessLicenseFilePath(path);
            } else {
                throw new ValidationException("Business License Document is required.");
            }
        }

        // ============================================ Operations ============================================

        if (dto.getWalkRadius() != null && !dto.getWalkRadius().trim().isEmpty()) {
            kyc.setWalkRadius(dto.getWalkRadius());
            System.out.println("Walk Radius saved: " + dto.getWalkRadius());
        } else {
            System.out.println("Walk Radius is null or empty!");
        }

        if (dto.getMaxPetsPerWalk() != null) {
            kyc.setMaxPetsPerWalk(dto.getMaxPetsPerWalk());
        }

        if (dto.getPreferredCommunication() != null) {
            kyc.setPreferredCommunication(dto.getPreferredCommunication());
        }

        // ============================================ Declarations ============================================

        if (dto.getDeclarationAccurate() != null && dto.getDeclarationAccurate().equals(Boolean.TRUE)) {
            kyc.setDeclarationAccurate(dto.getDeclarationAccurate());
        } else {
            throw new ValidationException("Declaration Accuracy is required.");
        }

        if (dto.getDeclarationVerifyOk() != null && dto.getDeclarationVerifyOk().equals(Boolean.TRUE)) {
            kyc.setDeclarationVerifyOk(dto.getDeclarationVerifyOk());
        } else {
            throw new ValidationException("Declaration Verify OK is required.");
        }

        if (dto.getDeclarationComply() != null && dto.getDeclarationComply().equals(Boolean.TRUE)) {
            kyc.setDeclarationComply(dto.getDeclarationComply());
        } else {
            throw new ValidationException("Declaration Comply is required.");
        }

        if (dto.getSignature() != null) {
            kyc.setSignature(dto.getSignature());
        }

        if (dto.getSignatureDate() != null) {
            kyc.setSignatureDate(dto.getSignatureDate());
        }

        walkerKycRepository.save(kyc);
    }

    // ===================== GET ALL METHOD =====================
    public List<WalkerKycRequestDto> getAll() {
        List<WalkerKyc> allDocuments = walkerKycRepository.findAllByOrderByCreatedAtDesc();
        System.out.println("***************" + allDocuments.size());
        return allDocuments.stream().map(this::copyToDto).collect(Collectors.toList());
    }

    // ===================== GET BY UID METHOD =====================
    public WalkerKycRequestDto getWalkerKycByUid(UUID uid) throws ValidationException {
        WalkerKyc kyc = walkerKycRepository.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Walker KYC not found with uid: " + uid));
        return copyToDto(kyc);
    }

    // ===================== DELETE BY UID METHOD =====================
    public void deleteWalkerKycByUid(UUID uid) throws ValidationException {
        WalkerKyc kyc = walkerKycRepository.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Walker KYC not found with uid: " + uid));

        // Delete associated files from filesystem
        deleteAssociatedFiles(kyc);

        walkerKycRepository.delete(kyc);
    }

    // ===================== GET STATUS BY UID METHOD =====================
    public String getStatusByUid(UUID uid) throws ValidationException {
        WalkerKyc kyc = walkerKycRepository.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Walker KYC not found with uid: " + uid));
        return kyc.getStatus();
    }

    // ===================== UPDATE STATUS BY UID METHOD =====================
    public WalkerKycRequestDto updateApplicationStatusByUid(UUID uid, String status) throws ValidationException {
        WalkerKyc kyc = walkerKycRepository.findByUid(uid)
                .orElseThrow(() -> new ValidationException("Walker KYC not found with uid: " + uid));

        kyc.setStatus(status);
        WalkerKyc updatedKyc = walkerKycRepository.save(kyc);

        return copyToDto(updatedKyc);
    }

    // ===================== HELPER METHODS =====================

    private WalkerKycRequestDto copyToDto(WalkerKyc entity) {
        WalkerKycRequestDto dto = new WalkerKycRequestDto();

        // Copy all properties using BeanUtils
        BeanUtils.copyProperties(entity, dto);

        // Explicitly set BaseEntity fields (in case BeanUtils doesn't copy them)
        dto.setId(entity.getId());
        dto.setUid(entity.getUid());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // base endpoint (relative). If you want absolute URL include host via ServletUriComponentsBuilder.
        String base = "/api/walkerkyc/uploaded_files/" + entity.getUid();

        // For each stored file, set path (filename) and a type-specific URL
        if (entity.getCertificationFilePath() != null && !entity.getCertificationFilePath().isBlank()) {
            dto.setCertificationFilePath(entity.getCertificationFilePath());
            dto.setCertificationFileURL(base + "/pet_care_certification");
        }
        if (entity.getBondedFilePath() != null && !entity.getBondedFilePath().isBlank()) {
            dto.setBondedFilePath(entity.getBondedFilePath());
            dto.setBondedFileURL(base + "/bonded_or_insured");
        }
        if (entity.getFirstAidFilePath() != null && !entity.getFirstAidFilePath().isBlank()) {
            dto.setFirstAidFilePath(entity.getFirstAidFilePath());
            dto.setFirstAidFileURL(base + "/pet_first_aid_certificate");
        }
        if (entity.getCriminalCheckFilePath() != null && !entity.getCriminalCheckFilePath().isBlank()) {
            dto.setCriminalCheckFilePath(entity.getCriminalCheckFilePath());
            dto.setCriminalCheckFileURL(base + "/criminal_record");
        }
        if (entity.getLiabilityFilePath() != null && !entity.getLiabilityFilePath().isBlank()) {
            dto.setLiabilityFilePath(entity.getLiabilityFilePath());
            dto.setLiabilityFileURL(base + "/liability_insurance");
        }
        if (entity.getBusinessLicenseFilePath() != null && !entity.getBusinessLicenseFilePath().isBlank()) {
            dto.setBusinessLicenseFilePath(entity.getBusinessLicenseFilePath());
            dto.setBusinessLicenseFileURL(base + "/business_license");
        }

        return dto;
    }

    private void deleteAssociatedFiles(WalkerKyc kyc) {
        // Delete all associated files from filesystem
        deleteFileIfExists(kyc.getCertificationFilePath());
        deleteFileIfExists(kyc.getBondedFilePath());
        deleteFileIfExists(kyc.getFirstAidFilePath());
        deleteFileIfExists(kyc.getCriminalCheckFilePath());
        deleteFileIfExists(kyc.getLiabilityFilePath());
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

    private String saveFile(MultipartFile file, String type, String identifier, List<String> allowedExtensions)
            throws IOException, ValidationException {

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if (!allowedExtensions.contains(ext)) {
            throw new ValidationException("Invalid file type for: " + originalFilename);
        }

        String folderPath = DOCUMENT_ROOT + File.separator + QrFolder + File.separator + identifier + File.separator;
        Path directory = Paths.get(folderPath);
        Files.createDirectories(directory);

        String fileName = type + "_" + System.currentTimeMillis() + "." + ext;
        String filePath = folderPath + fileName;

        File destFile = new File(filePath);
        file.transferTo(destFile);

        return filePath;
    }
}