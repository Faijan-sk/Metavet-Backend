package com.example.demo.Controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dto.GroomerKycRequestDto;
import com.example.demo.Service.GroomerKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/groomerkyc")
public class GroomerKycController {

	private static final Logger logger = LoggerFactory.getLogger(GroomerKycController.class);

	@Autowired
	private GroomerKycService groomerKycService;

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createGroomerKyc(@Valid @ModelAttribute GroomerKycRequestDto dto,
			BindingResult bindingResult,
			@RequestParam(value = "documentNames", required = false) List<String> documentNames,
			@RequestParam(value = "documentFiles", required = false) List<MultipartFile> documentFiles) {

		try {
			// validation errors from @Valid
			if (bindingResult != null && bindingResult.hasErrors()) {
				logger.warn("Validation failed for GroomerKycRequestDto: {}", bindingResult.getAllErrors());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
			}

			// call service
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

}
