package com.example.demo.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entities.BehaviouristKyc.ServiceOffered;
import com.example.demo.Entities.BehaviouristKyc.Specialization;
import com.example.demo.Entities.BehaviouristKyc.ApprovalStatus;

public class BehaviouristKycRequestDto {

    // BaseEntity fields
    private Long id;
    private UUID uid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---------------- Personal & Business Information ----------------
    private String fullLegalName;
    private String businessName;
    private String email;
    private String phone;
    private String address;
    private String serviceArea;
    private String yearsExperience;

    // ---------------- Professional Credentials ----------------
    private Boolean hasBehaviouralCertifications;
    private MultipartFile behaviouralCertificateDoc;
    private String behaviouralCertificateDetails;
    private String behaviouralCertificateFilePath;
    private String behaviouralCertificateFileURL;
    private String educationBackground;

    // ---------------- Insurance / Criminal / Liability ----------------
    private Boolean hasInsurance;
    private String insuranceProvider;
    private String insurancePolicyNumber;
    private LocalDate insuranceExpiry;
    private MultipartFile insuranceDoc;
    private String insuranceDocPath;
    private String insuranceDocURL;

    private Boolean hasCriminalCheck;
    private MultipartFile criminalRecordDoc;
    private String criminalDocPath;
    private String criminalDocURL;

    private Boolean liabilityInsurance;
    private MultipartFile liabilityInsuranceDoc;
    private String liabilityDocPath;
    private String liabilityDocURL;

    // ---------------- Business License ----------------
    private Boolean hasBusinessLicense;
    private MultipartFile businessLicenseDoc;
    private String businessLicenseFilePath;
    private String businessLicenseFileURL;

    // ---------------- Practice Details ----------------
    private List<ServiceOffered> servicesOffered;
    private String servicesOtherText;
    private List<Specialization> specializations;
    private String specializationOtherText;
    private String serviceRadius;

    // ---------------- Declarations & Signature ----------------
    private Boolean infoTrue;
    private Boolean verifyOk;
    private Boolean abideStandards;
    private String signature;
    private LocalDate signatureDate;

    // ---------------- Status ----------------
    private ApprovalStatus status;

    // ---------------- Getters and Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullLegalName() {
        return fullLegalName;
    }

    public void setFullLegalName(String fullLegalName) {
        this.fullLegalName = fullLegalName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(String yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public Boolean getHasBehaviouralCertifications() {
        return hasBehaviouralCertifications;
    }

    public void setHasBehaviouralCertifications(Boolean hasBehaviouralCertifications) {
        this.hasBehaviouralCertifications = hasBehaviouralCertifications;
    }

    public MultipartFile getBehaviouralCertificateDoc() {
        return behaviouralCertificateDoc;
    }

    public void setBehaviouralCertificateDoc(MultipartFile behaviouralCertificateDoc) {
        this.behaviouralCertificateDoc = behaviouralCertificateDoc;
    }

    public String getBehaviouralCertificateDetails() {
        return behaviouralCertificateDetails;
    }

    public void setBehaviouralCertificateDetails(String behaviouralCertificateDetails) {
        this.behaviouralCertificateDetails = behaviouralCertificateDetails;
    }

    public String getBehaviouralCertificateFilePath() {
        return behaviouralCertificateFilePath;
    }

    public void setBehaviouralCertificateFilePath(String behaviouralCertificateFilePath) {
        this.behaviouralCertificateFilePath = behaviouralCertificateFilePath;
    }

    public String getBehaviouralCertificateFileURL() {
        return behaviouralCertificateFileURL;
    }

    public void setBehaviouralCertificateFileURL(String behaviouralCertificateFileURL) {
        this.behaviouralCertificateFileURL = behaviouralCertificateFileURL;
    }

    public String getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    public Boolean getHasInsurance() {
        return hasInsurance;
    }

    public void setHasInsurance(Boolean hasInsurance) {
        this.hasInsurance = hasInsurance;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public LocalDate getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(LocalDate insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public MultipartFile getInsuranceDoc() {
        return insuranceDoc;
    }

    public void setInsuranceDoc(MultipartFile insuranceDoc) {
        this.insuranceDoc = insuranceDoc;
    }

    public String getInsuranceDocPath() {
        return insuranceDocPath;
    }

    public void setInsuranceDocPath(String insuranceDocPath) {
        this.insuranceDocPath = insuranceDocPath;
    }

    public String getInsuranceDocURL() {
        return insuranceDocURL;
    }

    public void setInsuranceDocURL(String insuranceDocURL) {
        this.insuranceDocURL = insuranceDocURL;
    }

    public Boolean getHasCriminalCheck() {
        return hasCriminalCheck;
    }

    public void setHasCriminalCheck(Boolean hasCriminalCheck) {
        this.hasCriminalCheck = hasCriminalCheck;
    }

    public MultipartFile getCriminalRecordDoc() {
        return criminalRecordDoc;
    }

    public void setCriminalRecordDoc(MultipartFile criminalRecordDoc) {
        this.criminalRecordDoc = criminalRecordDoc;
    }

    public String getCriminalDocPath() {
        return criminalDocPath;
    }

    public void setCriminalDocPath(String criminalDocPath) {
        this.criminalDocPath = criminalDocPath;
    }

    public String getCriminalDocURL() {
        return criminalDocURL;
    }

    public void setCriminalDocURL(String criminalDocURL) {
        this.criminalDocURL = criminalDocURL;
    }

    public Boolean getLiabilityInsurance() {
        return liabilityInsurance;
    }

    public void setLiabilityInsurance(Boolean liabilityInsurance) {
        this.liabilityInsurance = liabilityInsurance;
    }

    public MultipartFile getLiabilityInsuranceDoc() {
        return liabilityInsuranceDoc;
    }

    public void setLiabilityInsuranceDoc(MultipartFile liabilityInsuranceDoc) {
        this.liabilityInsuranceDoc = liabilityInsuranceDoc;
    }

    public String getLiabilityDocPath() {
        return liabilityDocPath;
    }

    public void setLiabilityDocPath(String liabilityDocPath) {
        this.liabilityDocPath = liabilityDocPath;
    }

    public String getLiabilityDocURL() {
        return liabilityDocURL;
    }

    public void setLiabilityDocURL(String liabilityDocURL) {
        this.liabilityDocURL = liabilityDocURL;
    }

    public Boolean getHasBusinessLicense() {
        return hasBusinessLicense;
    }

    public void setHasBusinessLicense(Boolean hasBusinessLicense) {
        this.hasBusinessLicense = hasBusinessLicense;
    }

    public MultipartFile getBusinessLicenseDoc() {
        return businessLicenseDoc;
    }

    public void setBusinessLicenseDoc(MultipartFile businessLicenseDoc) {
        this.businessLicenseDoc = businessLicenseDoc;
    }

    public String getBusinessLicenseFilePath() {
        return businessLicenseFilePath;
    }

    public void setBusinessLicenseFilePath(String businessLicenseFilePath) {
        this.businessLicenseFilePath = businessLicenseFilePath;
    }

    public String getBusinessLicenseFileURL() {
        return businessLicenseFileURL;
    }

    public void setBusinessLicenseFileURL(String businessLicenseFileURL) {
        this.businessLicenseFileURL = businessLicenseFileURL;
    }

    public List<ServiceOffered> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(List<ServiceOffered> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public String getServicesOtherText() {
        return servicesOtherText;
    }

    public void setServicesOtherText(String servicesOtherText) {
        this.servicesOtherText = servicesOtherText;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public String getSpecializationOtherText() {
        return specializationOtherText;
    }

    public void setSpecializationOtherText(String specializationOtherText) {
        this.specializationOtherText = specializationOtherText;
    }

    public String getServiceRadius() {
        return serviceRadius;
    }

    public void setServiceRadius(String serviceRadius) {
        this.serviceRadius = serviceRadius;
    }

    public Boolean getInfoTrue() {
        return infoTrue;
    }

    public void setInfoTrue(Boolean infoTrue) {
        this.infoTrue = infoTrue;
    }

    public Boolean getVerifyOk() {
        return verifyOk;
    }

    public void setVerifyOk(Boolean verifyOk) {
        this.verifyOk = verifyOk;
    }

    public Boolean getAbideStandards() {
        return abideStandards;
    }

    public void setAbideStandards(Boolean abideStandards) {
        this.abideStandards = abideStandards;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDate getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(LocalDate signatureDate) {
        this.signatureDate = signatureDate;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
}