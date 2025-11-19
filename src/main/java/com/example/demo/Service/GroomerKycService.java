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

import com.example.demo.Dto.GroomerKycRequestDto;
import com.example.demo.Entities.GroomerKyc;
import com.example.demo.Entities.GroomerKyc.ApplicationStatus;
import com.example.demo.Entities.GroomerKyc.ServiceOffered;
import com.example.demo.Repository.GroomerKycRepo;

import jakarta.validation.ValidationException;

@Service
public class GroomerKycService {

	@Autowired
	private GroomerKycRepo groomerKycRepository;

	private static final String DOCUMENT_ROOT = System.getProperty("user.dir");
	private static final String QrFolder = "groomer_kyc";
	private static final String Qr_FILE_DIR = DOCUMENT_ROOT + File.separator + QrFolder + File.separator;

	// ===================== CREATE METHOD =====================
	public void createGroomerKyc(GroomerKycRequestDto dto, List<String> documentNames,
			List<MultipartFile> documentFiles) throws IOException, ValidationException, java.io.IOException {

		GroomerKyc kyc = groomerKycRepository.findByEmail(dto.getEmail()).orElse(new GroomerKyc());

		kyc.setFullLegalName(dto.getFullLegalName());
		kyc.setBusinessName(dto.getBusinessName());

		List<String> allowedExtensions = Arrays.asList("pdf", "jpeg", "jpg", "png", "doc", "docx");

		if (dto.getHasBusinessLicense() != null && dto.getHasBusinessLicense().equals(Boolean.TRUE)) {
			kyc.setHasBusinessLicense(dto.getHasBusinessLicense());
			if (dto.getBusinessLicenseDoc() != null && !dto.getBusinessLicenseDoc().isEmpty()) {
				byte[] fileBytes = dto.getBusinessLicenseDoc().getBytes();
				String path = saveFile(dto.getBusinessLicenseDoc(), "business_license", dto.getEmail(),
						allowedExtensions);
				kyc.setBusinessLicenseDoc(fileBytes);
				kyc.setBusinessLicenseFilePath(path);
			} else {
				throw new ValidationException("Business License is required");
			}
		}

		kyc.setEmail(dto.getEmail());
		kyc.setPhone(dto.getPhone());
		kyc.setAddress(dto.getAddress());
		kyc.setServiceLocationType(dto.getServiceLocationType());
		kyc.setYearsExperience(dto.getYearsExperience());

		if (dto.getHasGroomingCert() != null && dto.getHasGroomingCert().equals(Boolean.TRUE)) {
			kyc.setHasGroomingCert(dto.getHasGroomingCert());
			if (dto.getGroomingCertificateDoc() != null && !dto.getGroomingCertificateDoc().isEmpty()) {
				byte[] fileBytes = dto.getGroomingCertificateDoc().getBytes();
				String path = saveFile(dto.getGroomingCertificateDoc(), "grooming_certificate", dto.getEmail(),
						allowedExtensions);
				kyc.setGroomingCertDetails(dto.getGroomingCertDetails());
				kyc.setGroomingCertificateDoc(fileBytes);
				kyc.setGroomingCertificateDocPath(path);
			} else {
				throw new ValidationException("Grooming Certificate Document is required.");
			}
		}

		if (dto.getHasFirstAidCert() != null && dto.getHasFirstAidCert().equals(Boolean.TRUE)) {
			kyc.setHasFirstAidCert(dto.getHasFirstAidCert());
			if (dto.getFirstAidCertificateDoc() != null && !dto.getFirstAidCertificateDoc().isEmpty()) {
				byte[] fileBytes = dto.getFirstAidCertificateDoc().getBytes();
				String path = saveFile(dto.getFirstAidCertificateDoc(), "first_aid_certificate", dto.getEmail(),
						allowedExtensions);
				kyc.setFirstAidCertificateDoc(fileBytes);
				kyc.setFirstAidCertificatePath(path);
			} else {
				throw new ValidationException("First Aid Certificate Document is required.");
			}
		}

		if (dto.getHasInsurance() != null && dto.getHasInsurance().equals(Boolean.TRUE)) {
			kyc.setHasInsurance(dto.getHasInsurance());
			if (dto.getInsuranceDoc() != null && !dto.getInsuranceDoc().isEmpty()) {
				byte[] fileBytes = dto.getInsuranceDoc().getBytes();
				String path = saveFile(dto.getInsuranceDoc(), "insurance", dto.getEmail(), allowedExtensions);
				kyc.setInsuranceDoc(fileBytes);
				kyc.setInsuranceExpiry(dto.getInsuranceExpiry());
				kyc.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
				kyc.setInsuranceProvider(dto.getInsuranceProvider());
				kyc.setInsuaranceDoccPath(path);
			} else {
				throw new ValidationException("Insurance Document is required.");
			}
		}

		if (dto.getCriminalCheck() != null && dto.getCriminalCheck().equals(Boolean.TRUE)) {
			kyc.setCriminalCheck(dto.getCriminalCheck());
			if (dto.getCrimialRecordDoc() != null && !dto.getCrimialRecordDoc().isEmpty()) {
				byte[] fileBytes = dto.getCrimialRecordDoc().getBytes();
				String path = saveFile(dto.getCrimialRecordDoc(), "criminal_record", dto.getEmail(), allowedExtensions);
				kyc.setCrimialRecordDoc(fileBytes);
				kyc.setCriminalDocPath(path);
			} else {
				throw new ValidationException("Criminal Record Document is required.");
			}
		}

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

			if (dto.getLiabilityExpiry() != null) {
				kyc.setLiabilityExpiry(dto.getLiabilityExpiry());
			} else {
				throw new ValidationException("Liability Expiry is required.");
			}

			if (dto.getLiabilityInsuaranceDoc() != null && !dto.getLiabilityInsuaranceDoc().isEmpty()) {
				byte[] fileBytes = dto.getLiabilityInsuaranceDoc().getBytes();
				String path = saveFile(dto.getLiabilityInsuaranceDoc(), "liability_insurance", dto.getEmail(),
						allowedExtensions);
				kyc.setLiabilityInsuaranceDoc(fileBytes);
				kyc.setLiabilityDocPath(path);
			} else {
				throw new ValidationException("Liability Insurance Document is required.");
			}

		}

		if (dto.getHasIncidentPolicy() != null && dto.getHasIncidentPolicy().equals(Boolean.TRUE)) {
			kyc.setHasIncidentPolicy(dto.getHasIncidentPolicy());
			if (dto.getIncidentPolicyDetails() != null) {
				kyc.setIncidentPolicyDetails(dto.getIncidentPolicyDetails());
			} else {
				throw new ValidationException("Incident Policy Details is required.");
			}
		}

		if (dto.getServicesOffered() != null) {
			if (!(dto.getServicesOffered().contains(ServiceOffered.OTHER))) {
				kyc.setServicesOffered(dto.getServicesOffered());
			} else {
				kyc.setServicesOffered(dto.getServicesOffered());
				if (dto.getServicesOtherText() != null) {
					kyc.setServicesOtherText(dto.getServicesOtherText());
				} else {
					throw new ValidationException("Services Other Text is required.");
				}
			}
		}

		if (dto.getServicesPrices() != null) {
			kyc.setServicesPrices(dto.getServicesPrices());
		}

		if (dto.getAverageAppointmentDuration() != null) {
			kyc.setAverageAppointmentDuration(dto.getAverageAppointmentDuration());
		}

		if (dto.getServiceRadius() != null) {
			kyc.setServiceRadius(dto.getServiceRadius());
		}

		if (dto.getDeclarationAccuracy() != null && dto.getDeclarationAccuracy().equals(Boolean.TRUE)) {
			kyc.setDeclarationAccuracy(dto.getDeclarationAccuracy());
		} else {
			throw new ValidationException("Declaration Accuracy is required.");
		}

		if (dto.getDeclarationConsentVerify() != null && dto.getDeclarationConsentVerify().equals(Boolean.TRUE)) {
			kyc.setDeclarationConsentVerify(dto.getDeclarationConsentVerify());
		} else {
			throw new ValidationException("Declaration Consent Verify is required.");
		}

		if (dto.getDeclarationComply() != null && dto.getDeclarationComply().equals(Boolean.TRUE)) {
			kyc.setDeclarationComply(dto.getDeclarationComply());
		} else {
			throw new ValidationException("Declaration is required.");
		}

		if (dto.getSignature() != null) {
			kyc.setSignature(dto.getSignature());
		}

		if (dto.getSignatureDate() != null) {
			kyc.setSignatureDate(dto.getSignatureDate());
		}

		groomerKycRepository.save(kyc);
	}

	// ===================== GET ALL METHOD =====================
	public List<GroomerKycRequestDto> getAll() {
		List<GroomerKyc> allDocumnets = groomerKycRepository.findAll();
		System.out.println("***************" + allDocumnets.size());
		return allDocumnets.stream().map(this::copyToDto).collect(Collectors.toList());
	}

	// ===================== GET BY UID METHOD =====================
	public GroomerKycRequestDto getGroomerKycByUid(UUID uid) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findByUid(uid)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with uid: " + uid));
		return copyToDto(kyc);
	}

	// ===================== GET BY ID METHOD =====================
	public GroomerKycRequestDto getGroomerKycById(Long id) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with id: " + id));
		return copyToDto(kyc);
	}

	// ===================== DELETE BY UID METHOD =====================
	public void deleteGroomerKycByUid(UUID uid) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findByUid(uid)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with uid: " + uid));

		// Delete associated files from filesystem
		deleteAssociatedFiles(kyc);

		groomerKycRepository.delete(kyc);
	}

	// ===================== DELETE BY ID METHOD =====================
	public void deleteGroomerKycById(Long id) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with id: " + id));

		// Delete associated files from filesystem
		deleteAssociatedFiles(kyc);

		groomerKycRepository.delete(kyc);
	}

	// ===================== GET STATUS BY UID METHOD =====================
	public ApplicationStatus getStatusByUid(UUID uid) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findByUid(uid)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with uid: " + uid));
		return kyc.getStatus();
	}

	// ===================== GET STATUS BY ID METHOD =====================
	public ApplicationStatus getStatusById(Long id) throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with id: " + id));
		return kyc.getStatus();
	}

	// ===================== UPDATE STATUS BY UID METHOD =====================
	public GroomerKycRequestDto updateApplicationStatusByUid(UUID uid, ApplicationStatus status)
			throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findByUid(uid)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with uid: " + uid));

		kyc.setStatus(status);
		GroomerKyc updatedKyc = groomerKycRepository.save(kyc);

		return copyToDto(updatedKyc);
	}

	// ===================== UPDATE STATUS BY ID METHOD =====================
	public GroomerKycRequestDto updateApplicationStatusById(Long id, ApplicationStatus status)
			throws ValidationException {
		GroomerKyc kyc = groomerKycRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Groomer KYC not found with id: " + id));

		kyc.setStatus(status);
		GroomerKyc updatedKyc = groomerKycRepository.save(kyc);

		return copyToDto(updatedKyc);
	}

	// ===================== HELPER METHODS =====================

	private GroomerKycRequestDto copyToDto(GroomerKyc entity) {
		GroomerKycRequestDto dto = new GroomerKycRequestDto();
		BeanUtils.copyProperties(entity, dto);

		// Set BaseEntity fields
		dto.setId(entity.getId());
		dto.setUid(entity.getUid());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());

		// base endpoint (relative). If you want absolute URL include host via ServletUriComponentsBuilder.
		String base = "/groomerkyc/uploaded_files/" + entity.getUid();

		// For each stored file, set path (filename) and a type-specific URL
		if (entity.getBusinessLicenseFilePath() != null && !entity.getBusinessLicenseFilePath().isBlank()) {
			dto.setBusinessLicenseFilePath(entity.getBusinessLicenseFilePath());
			dto.setBusinessLicenseFileURL(base + "/business_license");
		}
		if (entity.getGroomingCertificateDocPath() != null && !entity.getGroomingCertificateDocPath().isBlank()) {
			dto.setGroomingCertificateDocPath(entity.getGroomingCertificateDocPath());
			dto.setGroomingCertificateDocURL(base + "/grooming_certificate");
		}
		if (entity.getFirstAidCertificatePath() != null && !entity.getFirstAidCertificatePath().isBlank()) {
			dto.setFirstAidCertificatePath(entity.getFirstAidCertificatePath());
			dto.setFirstAidCertificateURL(base + "/first_aid_certificate");
		}
		if (entity.getInsuaranceDoccPath() != null && !entity.getInsuaranceDoccPath().isBlank()) {
			dto.setInsuaranceDoccPath(entity.getInsuaranceDoccPath());
			dto.setInsuaranceDoccURL(base + "/insurance");
		}
		if (entity.getCriminalDocPath() != null && !entity.getCriminalDocPath().isBlank()) {
			dto.setCriminalDocPath(entity.getCriminalDocPath());
			dto.setCriminalDocURL(base + "/criminal_record");
		}
		if (entity.getLiabilityDocPath() != null && !entity.getLiabilityDocPath().isBlank()) {
			dto.setLiabilityDocPath(entity.getLiabilityDocPath());
			dto.setLiabilityDocURL(base + "/liability_insurance");
		}

		return dto;
	}

	private void deleteAssociatedFiles(GroomerKyc kyc) {
		// Delete all associated files from filesystem
		deleteFileIfExists(kyc.getBusinessLicenseFilePath());
		deleteFileIfExists(kyc.getGroomingCertificateDocPath());
		deleteFileIfExists(kyc.getFirstAidCertificatePath());
		deleteFileIfExists(kyc.getInsuaranceDoccPath());
		deleteFileIfExists(kyc.getCriminalDocPath());
		deleteFileIfExists(kyc.getLiabilityDocPath());
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