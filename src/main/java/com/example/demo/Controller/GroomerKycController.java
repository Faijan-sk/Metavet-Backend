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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dto.GroomerKycRequestDto;
import com.example.demo.Entities.GroomerKyc;
import com.example.demo.Entities.GroomerKyc.ApplicationStatus;
import com.example.demo.Repository.GroomerKycRepo;
import com.example.demo.Service.GroomerKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/groomerkyc")
public class GroomerKycController {

	private static final Logger logger = LoggerFactory.getLogger(GroomerKycController.class);

	@Autowired
	private GroomerKycService groomerKycService;

	@Autowired
	private GroomerKycRepo groomerKycRepo;

	private static final String DOCUMENT_ROOT = System.getProperty("user.dir");
	private static final String QrFolder = "groomer_kyc";
	private static final String Qr_FILE_DIR = DOCUMENT_ROOT + File.separator + QrFolder + File.separator;

	// ===================== CREATE KYC =====================
	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createGroomerKyc(@Valid @ModelAttribute GroomerKycRequestDto dto,
			BindingResult bindingResult,
			@RequestParam(value = "documentNames", required = false) List<String> documentNames,
			@RequestParam(value = "documentFiles", required = false) List<MultipartFile> documentFiles) {
		try {
			if (bindingResult != null && bindingResult.hasErrors()) {
				logger.warn("Validation failed for GroomerKycRequestDto: {}", bindingResult.getAllErrors());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
			}

			groomerKycService.createGroomerKyc(dto, documentNames, documentFiles);
			return ResponseEntity.status(HttpStatus.CREATED).body("Groomer KYC created successfully.");
		} catch (ValidationException ve) {
			logger.warn("ValidationException while creating KYC: {}", ve.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ve.getMessage());
		} catch (IOException ioe) {
			logger.error("IOException while creating KYC", ioe);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process uploaded files.");
		} catch (Exception ex) {
			logger.error("Unexpected error while creating KYC", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
		}
	}

	// ===================== GET ALL KYC =====================
	@GetMapping("/all")
	public ResponseEntity<List<GroomerKycRequestDto>> getDtos() {
		return ResponseEntity.ok(groomerKycService.getAll());
	}

	// ===================== GET SINGLE KYC BY UID =====================
	@GetMapping("/uid/{uid}")
	public ResponseEntity<?> getGroomerKycByUid(@PathVariable UUID uid) {
		try {
			logger.info("Fetching KYC record with uid: {}", uid);
			GroomerKycRequestDto kyc = groomerKycService.getGroomerKycByUid(uid);
			return ResponseEntity.ok(kyc);
		} catch (ValidationException ve) {
			logger.warn("KYC not found with uid: {}", uid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error fetching KYC with uid: {}", uid, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch KYC record: " + ex.getMessage());
		}
	}

	// ===================== GET SINGLE KYC BY ID =====================
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getGroomerKycById(@PathVariable Long id) {
		try {
			logger.info("Fetching KYC record with id: {}", id);
			GroomerKycRequestDto kyc = groomerKycService.getGroomerKycById(id);
			return ResponseEntity.ok(kyc);
		} catch (ValidationException ve) {
			logger.warn("KYC not found with id: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error fetching KYC with id: {}", id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch KYC record: " + ex.getMessage());
		}
	}

	// ===================== GET UPLOADED FILE =====================
	@GetMapping("/uploaded_files/{uid}/{fileType}")
	public ResponseEntity<Resource> getDocument(@PathVariable UUID uid, @PathVariable String fileType)
			throws MalformedURLException, ValidationException {

		GroomerKyc document = groomerKycRepo.findByUid(uid)
				.orElseThrow(() -> new ValidationException("Document not found"));

		// choose which stored file to return based on fileType
		byte[] fileBytes = null;
		String fileName = null; // actual filename (on disk) or fallback name for content-disposition

		switch (fileType.toLowerCase()) {
		case "business_license":
			fileBytes = document.getBusinessLicenseDoc();
			fileName = document.getBusinessLicenseFilePath();
			break;
		case "grooming_certificate":
			fileBytes = document.getGroomingCertificateDoc();
			fileName = document.getGroomingCertificateDocPath();
			break;
		case "first_aid_certificate":
			fileBytes = document.getFirstAidCertificateDoc();
			fileName = document.getFirstAidCertificatePath();
			break;
		case "insurance":
			fileBytes = document.getInsuranceDoc();
			fileName = document.getInsuaranceDoccPath();
			break;
		case "criminal_record":
			fileBytes = document.getCrimialRecordDoc();
			fileName = document.getCriminalDocPath();
			break;
		case "liability_insurance":
			fileBytes = document.getLiabilityInsuaranceDoc();
			fileName = document.getLiabilityDocPath();
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

	// ===================== DELETE KYC BY UID =====================
	@DeleteMapping("/uid/{uid}")
	public ResponseEntity<?> deleteGroomerKycByUid(@PathVariable UUID uid) {
		try {
			logger.info("Deleting KYC record with uid: {}", uid);
			groomerKycService.deleteGroomerKycByUid(uid);
			return ResponseEntity.ok("Groomer KYC deleted successfully.");
		} catch (ValidationException ve) {
			logger.warn("KYC not found for deletion with uid: {}", uid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error deleting KYC with uid: {}", uid, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete KYC record: " + ex.getMessage());
		}
	}

	// ===================== DELETE KYC BY ID =====================
	@DeleteMapping("/id/{id}")
	public ResponseEntity<?> deleteGroomerKycById(@PathVariable Long id) {
		try {
			logger.info("Deleting KYC record with id: {}", id);
			groomerKycService.deleteGroomerKycById(id);
			return ResponseEntity.ok("Groomer KYC deleted successfully.");
		} catch (ValidationException ve) {
			logger.warn("KYC not found for deletion with id: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error deleting KYC with id: {}", id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete KYC record: " + ex.getMessage());
		}
	}

	// ===================== GET STATUS BY UID =====================
	@GetMapping("/uid/{uid}/status")
	public ResponseEntity<?> getStatusByUid(@PathVariable UUID uid) {
		try {
			logger.info("Fetching status for KYC with uid: {}", uid);
			ApplicationStatus status = groomerKycService.getStatusByUid(uid);
			
			Map<String, Object> response = new HashMap<>();
			response.put("uid", uid);
			response.put("status", status);
			
			return ResponseEntity.ok(response);
		} catch (ValidationException ve) {
			logger.warn("KYC not found with uid: {}", uid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error fetching status for KYC with uid: {}", uid, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch status: " + ex.getMessage());
		}
	}

	// ===================== GET STATUS BY ID =====================
	@GetMapping("/id/{id}/status")
	public ResponseEntity<?> getStatusById(@PathVariable Long id) {
		try {
			logger.info("Fetching status for KYC with id: {}", id);
			ApplicationStatus status = groomerKycService.getStatusById(id);
			
			Map<String, Object> response = new HashMap<>();
			response.put("id", id);
			response.put("status", status);
			
			return ResponseEntity.ok(response);
		} catch (ValidationException ve) {
			logger.warn("KYC not found with id: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error fetching status for KYC with id: {}", id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch status: " + ex.getMessage());
		}
	}

	// ===================== UPDATE APPLICATION STATUS BY UID =====================
	@PatchMapping("/uid/{uid}/status")
	public ResponseEntity<?> updateApplicationStatusByUid(
			@PathVariable UUID uid,
			@RequestParam ApplicationStatus status) {
		try {
			logger.info("Updating status for KYC with uid: {} to {}", uid, status);
			GroomerKycRequestDto updatedKyc = groomerKycService.updateApplicationStatusByUid(uid, status);
			return ResponseEntity.ok(updatedKyc);
		} catch (ValidationException ve) {
			logger.warn("KYC not found for status update with uid: {}", uid);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error updating status for KYC with uid: {}", uid, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update application status: " + ex.getMessage());
		}
	}

	// ===================== UPDATE APPLICATION STATUS BY ID =====================
	@PatchMapping("/id/{id}/status")
	public ResponseEntity<?> updateApplicationStatusById(
			@PathVariable Long id,
			@RequestParam ApplicationStatus status) {
		try {
			logger.info("Updating status for KYC with id: {} to {}", id, status);
			GroomerKycRequestDto updatedKyc = groomerKycService.updateApplicationStatusById(id, status);
			return ResponseEntity.ok(updatedKyc);
		} catch (ValidationException ve) {
			logger.warn("KYC not found for status update with id: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ve.getMessage());
		} catch (Exception ex) {
			logger.error("Error updating status for KYC with id: {}", id, ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update application status: " + ex.getMessage());
		}
	}
}