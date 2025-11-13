//package com.example.demo.Service;
//
//
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.demo.Dto.WalkerKycRequestDto;
//import com.example.demo.Entities.WalkerKyc;
//import com.example.demo.Repository.WalkerKycRepo;
//
//@Service
//public class WalkerKycService {
//
//	@Autowired
//	private WalkerKycRepo walkerKycRepo;
//
//	@Value("${file.upload-dir:uploads/walker-kyc}")
//	private String uploadDir;
//
//	public WalkerKyc createWalkerKyc(WalkerKycRequestDto dto) throws IOException {
//
//		// Check if email already exists
//		if (walkerKycRepo.existsByEmail(dto.getEmail())) {
//			throw new RuntimeException("Email already exists");
//		}
//
//		// Check if phone already exists
//		if (dto.getPhone() != null && walkerKycRepo.existsByPhone(dto.getPhone())) {
//			throw new RuntimeException("Phone number already exists");
//		}
//
//		WalkerKyc walkerKyc = new WalkerKyc();
//
//		// Personal & Business Information
//		walkerKyc.setFullLegalName(dto.getFullLegalName());
//		walkerKyc.setBusinessName(dto.getBusinessName());
//		walkerKyc.setEmail(dto.getEmail());
//		walkerKyc.setPhone(dto.getPhone());
//		walkerKyc.setAddress(dto.getAddress());
//		walkerKyc.setServiceArea(dto.getServiceArea());
//		walkerKyc.setYearsExperience(dto.getYearsExperience());
//
//		// Professional Credentials
//		walkerKyc.setHasCertifications(dto.getHasCertifications());
//		walkerKyc.setCertificationDetails(dto.getCertificationDetails());
//
//		// Handle Pet Care Certificate Doc
//		if (dto.getPetCareCertificationDoc() != null && !dto.getPetCareCertificationDoc().isEmpty()) {
//			String filePath = saveFile(dto.getPetCareCertificationDoc(), "pet-care-cert");
//			walkerKyc.setCertificationFilePath(filePath);
//			walkerKyc.setPetCareCertificationDoc(dto.getPetCareCertificationDoc().getBytes());
//		}
//
//		// Bonded or Insured
//		walkerKyc.setBondedOrInsured(dto.getBondedOrInsured());
//		if (dto.getBondedOrInsuredDoc() != null && !dto.getBondedOrInsuredDoc().isEmpty()) {
//			String filePath = saveFile(dto.getBondedOrInsuredDoc(), "bonded-doc");
//			walkerKyc.setBondedFilePath(filePath);
//			walkerKyc.setBondedOrInsuredDoc(dto.getBondedOrInsuredDoc().getBytes());
//		}
//
//		// First Aid
//		walkerKyc.setHasFirstAid(dto.getHasFirstAid());
//		if (dto.getPetFirstAidCertificateDoc() != null && !dto.getPetFirstAidCertificateDoc().isEmpty()) {
//			String filePath = saveFile(dto.getPetFirstAidCertificateDoc(), "first-aid-cert");
//			walkerKyc.setFirstAidFilePath(filePath);
//			walkerKyc.setPetFirstAidCertificateDoc(dto.getPetFirstAidCertificateDoc().getBytes());
//		}
//
//		// Criminal Check
//		walkerKyc.setCriminalCheck(dto.getCriminalCheck());
//		if (dto.getCrimialRecordDoc() != null && !dto.getCrimialRecordDoc().isEmpty()) {
//			String filePath = saveFile(dto.getCrimialRecordDoc(), "criminal-check");
//			walkerKyc.setCriminalCheckFilePath(filePath);
//			walkerKyc.setCrimialRecordDoc(dto.getCrimialRecordDoc().getBytes());
//		}
//
//		// Liability & Insurance
//		walkerKyc.setLiabilityInsurance(dto.getLiabilityInsurance());
//		walkerKyc.setLiabilityProvider(dto.getLiabilityProvider());
//		walkerKyc.setLiabilityPolicyNumber(dto.getLiabilityPolicyNumber());
//		walkerKyc.setInsuranceExpiry(dto.getInsuranceExpiry());
//
//		if (dto.getLiabilityInsuaranceDoc() != null && !dto.getLiabilityInsuaranceDoc().isEmpty()) {
//			String filePath = saveFile(dto.getLiabilityInsuaranceDoc(), "liability-insurance");
//			walkerKyc.setLiabilityFilePath(filePath);
//			walkerKyc.setLiabilityInsuaranceDoc(dto.getLiabilityInsuaranceDoc().getBytes());
//		}
//
//		// Business License
//		if (dto.getBusinessLicenseDoc() != null && !dto.getBusinessLicenseDoc().isEmpty()) {
//			String filePath = saveFile(dto.getBusinessLicenseDoc(), "business-license");
//			walkerKyc.setBusinessLicenseFilePath(filePath);
//			walkerKyc.setBusinessLicenseDoc(dto.getBusinessLicenseDoc().getBytes());
//		}
//
//		// Operations
//		walkerKyc.setWalkRadius(dto.getWalkRadius());
//		walkerKyc.setMaxPetsPerWalk(dto.getMaxPetsPerWalk());
//		walkerKyc.setPreferredCommunication(dto.getPreferredCommunication());
//
//		// Declarations
//		walkerKyc.setDeclarationAccurate(dto.getDeclarationAccurate());
//		walkerKyc.setDeclarationVerifyOk(dto.getDeclarationVerifyOk());
//		walkerKyc.setDeclarationComply(dto.getDeclarationComply());
//		walkerKyc.setSignature(dto.getSignature());
//		walkerKyc.setSignatureDate(dto.getSignatureDate());
//
//		// Status (will be set to PENDING by @PrePersist)
//		walkerKyc.setStatus("PENDING");
//
//		return walkerKycRepo.save(walkerKyc);
//	}
//
//	private String saveFile(MultipartFile file, String fileType) throws IOException {
//		// Create upload directory if it doesn't exist
//		Path uploadPath = Paths.get(uploadDir);
//		if (!Files.exists(uploadPath)) {
//			Files.createDirectories(uploadPath);
//		}
//
//		// Generate unique filename
//		String originalFilename = file.getOriginalFilename();
//		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//		String filename = fileType + "_" + UUID.randomUUID().toString() + extension;
//
//		// Save file
//		Path filePath = uploadPath.resolve(filename);
//		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//		return filePath.toString();
//	}
//}
