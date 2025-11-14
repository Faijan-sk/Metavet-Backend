package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

     // Service में बदलाव
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

    // =================================================================

    private String saveFile(MultipartFile file, String type, String identifier, List<String> allowedExtensions)
            throws IOException, ValidationException {

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if (!allowedExtensions.contains(ext)) {
            throw new ValidationException("Invalid file type for: " + originalFilename);
        }

        // Create absolute path in project directory
        String folderPath = DOCUMENT_ROOT + File.separator + QrFolder + File.separator + identifier + File.separator;
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