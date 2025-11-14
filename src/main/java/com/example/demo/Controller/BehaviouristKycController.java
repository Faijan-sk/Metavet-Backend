package com.example.demo.Controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.BehaviouristKycRequestDto;
import com.example.demo.Service.BehaviouristKycService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(path = "/api/behaviouristkyc")
public class BehaviouristKycController {

    private static final Logger logger = LoggerFactory.getLogger(BehaviouristKycController.class);

    @Autowired
    private BehaviouristKycService behaviouristKycService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBehaviouristKyc(
            @Valid @ModelAttribute BehaviouristKycRequestDto dto,
            BindingResult bindingResult) {

        try {
            // Validation errors from @Valid
            if (bindingResult != null && bindingResult.hasErrors()) {
                logger.warn("Validation failed for BehaviouristKycRequestDto: {}", bindingResult.getAllErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
            }

            // Call service
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
}