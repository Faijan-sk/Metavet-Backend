//package com.example.demo.Controller;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.Dto.WalkerKycRequestDto;
//import com.example.demo.Entities.WalkerKyc;
//import com.example.demo.Service.WalkerKycService;
//
//@RestController
//@RequestMapping("/api/walkerkyc")
//public class WalkerKycController {
//
//    private static final Logger logger = LoggerFactory.getLogger(WalkerKycController.class);
//
//    @Autowired
//    private WalkerKycService walkerKycService;
//
//    /**
//     * Create Walker KYC
//     * Expects multipart/form-data (files + fields) — use @ModelAttribute to bind.
//     */
//    @PostMapping(value = "/create",
//                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//                 produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createWalkerKyc(@ModelAttribute WalkerKycRequestDto dto) {
//        try {
//            logger.info("Received WalkerKyc create request for email: {}", dto.getEmail());
//            WalkerKyc walkerKyc = walkerKycService.createWalkerKyc(dto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(walkerKyc);
//        } catch (RuntimeException e) {
//            logger.warn("Validation/runtime error while creating WalkerKyc: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
//        } catch (IOException e) {
//            logger.error("IO error while saving files for WalkerKyc", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                                      "Error saving files: " + e.getMessage()));
//        } catch (Exception e) {
//            logger.error("Unexpected error in createWalkerKyc", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                                      "Unexpected server error"));
//        }
//    }
//
//    // Simple inner DTO for error responses (keeps controller self-contained)
//    private static class ErrorResponse {
//        private int status;
//        private String message;
//
//        public ErrorResponse(int status, String message) {
//            this.status = status;
//            this.message = message;
//        }
//
//        public int getStatus() { return status; }
//        public String getMessage() { return message; }
//    }
//}
