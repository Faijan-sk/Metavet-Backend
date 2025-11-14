package com.example.demo.Dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

public class WalkerKycRequestDto {

    // Personal & Business Information
    private String fullLegalName;
    
    private String businessName;
    
    private String email;
    
    private String phone;
    
    private String address;
    
    private String serviceArea;
    
    private String yearsExperience;
    
    
    // ******************************************** Professional Credentials ****************************************
    
    private Boolean hasPetCareCertifications;
    
    private String hasPetCareCertificationsDetails;
    
    private MultipartFile petCareCertificationDoc;
    
    private String certificationFilePath;
    
    private String certificationFileURL;
    
    
    
    private Boolean bondedOrInsured;
    
    private MultipartFile bondedOrInsuredDoc;
    
    private String bondedFilePath;
    
    private String bondedFileURL;
    
    
    
    private Boolean hasFirstAid;
    
    private MultipartFile petFirstAidCertificateDoc;
    
    private String firstAidFilePath;
    
    private String firstAidFileURL;
    
    
    
    private Boolean criminalCheck;
    
    private MultipartFile crimialRecordDoc;
    
    private String criminalCheckFilePath;
    
    private String criminalCheckFileURL;
    
    
    // **************************************** Liability and Insurance ***********************************
    
    private Boolean liabilityInsurance;
    
    private String liabilityProvider;
    
    private String liabilityPolicyNumber;
    
    private LocalDate insuranceExpiry;
    
    private MultipartFile liabilityInsuaranceDoc;
    
    private String liabilityFilePath;
    
    private String liabilityFileURL;
    
    
    
    private Boolean hasBusinessLicenseDoc;
    
    private MultipartFile businessLicenseDoc;
    
    private String businessLicenseFilePath;
    
    private String businessLicenseFileURL;
    
    
    // ***************************************** Operations **************************
    
    private String walkRadius;
    
    private Integer maxPetsPerWalk;
    
    private String preferredCommunication;
    
    
    // ********************************************* Declarations ******************************************
    
    private Boolean declarationAccurate;
    
    private Boolean declarationVerifyOk;
    
    private Boolean declarationComply;
    
    private String signature;
    
    private LocalDate signatureDate;
    
    private String status;

    // ============================================ Getters and Setters ============================================

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

    public Boolean getHasPetCareCertifications() {
        return hasPetCareCertifications;
    }

    public void setHasPetCareCertifications(Boolean hasPetCareCertifications) {
        this.hasPetCareCertifications = hasPetCareCertifications;
    }

    public String getHasPetCareCertificationsDetails() {
        return hasPetCareCertificationsDetails;
    }

    public void setHasPetCareCertificationsDetails(String hasPetCareCertificationsDetails) {
        this.hasPetCareCertificationsDetails = hasPetCareCertificationsDetails;
    }

    public MultipartFile getPetCareCertificationDoc() {
        return petCareCertificationDoc;
    }

    public void setPetCareCertificationDoc(MultipartFile petCareCertificationDoc) {
        this.petCareCertificationDoc = petCareCertificationDoc;
    }

    public String getCertificationFilePath() {
        return certificationFilePath;
    }

    public void setCertificationFilePath(String certificationFilePath) {
        this.certificationFilePath = certificationFilePath;
    }

    public String getCertificationFileURL() {
        return certificationFileURL;
    }

    public void setCertificationFileURL(String certificationFileURL) {
        this.certificationFileURL = certificationFileURL;
    }

    public Boolean getBondedOrInsured() {
        return bondedOrInsured;
    }

    public void setBondedOrInsured(Boolean bondedOrInsured) {
        this.bondedOrInsured = bondedOrInsured;
    }

    public MultipartFile getBondedOrInsuredDoc() {
        return bondedOrInsuredDoc;
    }

    public void setBondedOrInsuredDoc(MultipartFile bondedOrInsuredDoc) {
        this.bondedOrInsuredDoc = bondedOrInsuredDoc;
    }

    public String getBondedFilePath() {
        return bondedFilePath;
    }

    public void setBondedFilePath(String bondedFilePath) {
        this.bondedFilePath = bondedFilePath;
    }

    public String getBondedFileURL() {
        return bondedFileURL;
    }

    public void setBondedFileURL(String bondedFileURL) {
        this.bondedFileURL = bondedFileURL;
    }

    public Boolean getHasFirstAid() {
        return hasFirstAid;
    }

    public void setHasFirstAid(Boolean hasFirstAid) {
        this.hasFirstAid = hasFirstAid;
    }

    public MultipartFile getPetFirstAidCertificateDoc() {
        return petFirstAidCertificateDoc;
    }

    public void setPetFirstAidCertificateDoc(MultipartFile petFirstAidCertificateDoc) {
        this.petFirstAidCertificateDoc = petFirstAidCertificateDoc;
    }

    public String getFirstAidFilePath() {
        return firstAidFilePath;
    }

    public void setFirstAidFilePath(String firstAidFilePath) {
        this.firstAidFilePath = firstAidFilePath;
    }

    public String getFirstAidFileURL() {
        return firstAidFileURL;
    }

    public void setFirstAidFileURL(String firstAidFileURL) {
        this.firstAidFileURL = firstAidFileURL;
    }

    public Boolean getCriminalCheck() {
        return criminalCheck;
    }

    public void setCriminalCheck(Boolean criminalCheck) {
        this.criminalCheck = criminalCheck;
    }

    public MultipartFile getCrimialRecordDoc() {
        return crimialRecordDoc;
    }

    public void setCrimialRecordDoc(MultipartFile crimialRecordDoc) {
        this.crimialRecordDoc = crimialRecordDoc;
    }

    public String getCriminalCheckFilePath() {
        return criminalCheckFilePath;
    }

    public void setCriminalCheckFilePath(String criminalCheckFilePath) {
        this.criminalCheckFilePath = criminalCheckFilePath;
    }

    public String getCriminalCheckFileURL() {
        return criminalCheckFileURL;
    }

    public void setCriminalCheckFileURL(String criminalCheckFileURL) {
        this.criminalCheckFileURL = criminalCheckFileURL;
    }

    public Boolean getLiabilityInsurance() {
        return liabilityInsurance;
    }

    public void setLiabilityInsurance(Boolean liabilityInsurance) {
        this.liabilityInsurance = liabilityInsurance;
    }

    public String getLiabilityProvider() {
        return liabilityProvider;
    }

    public void setLiabilityProvider(String liabilityProvider) {
        this.liabilityProvider = liabilityProvider;
    }

    public String getLiabilityPolicyNumber() {
        return liabilityPolicyNumber;
    }

    public void setLiabilityPolicyNumber(String liabilityPolicyNumber) {
        this.liabilityPolicyNumber = liabilityPolicyNumber;
    }

    public LocalDate getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public void setInsuranceExpiry(LocalDate insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public MultipartFile getLiabilityInsuaranceDoc() {
        return liabilityInsuaranceDoc;
    }

    public void setLiabilityInsuaranceDoc(MultipartFile liabilityInsuaranceDoc) {
        this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
    }

    public String getLiabilityFilePath() {
        return liabilityFilePath;
    }

    public void setLiabilityFilePath(String liabilityFilePath) {
        this.liabilityFilePath = liabilityFilePath;
    }

    public String getLiabilityFileURL() {
        return liabilityFileURL;
    }

    public void setLiabilityFileURL(String liabilityFileURL) {
        this.liabilityFileURL = liabilityFileURL;
    }

    public Boolean getHasBusinessLicenseDoc() {
        return hasBusinessLicenseDoc;
    }

    public void setHasBusinessLicenseDoc(Boolean hasBusinessLicenseDoc) {
        this.hasBusinessLicenseDoc = hasBusinessLicenseDoc;
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

    public String getWalkRadius() {
        return walkRadius;
    }

    public void setWalkRadius(String walkRadius) {
        this.walkRadius = walkRadius;
    }

    public Integer getMaxPetsPerWalk() {
        return maxPetsPerWalk;
    }

    public void setMaxPetsPerWalk(Integer maxPetsPerWalk) {
        this.maxPetsPerWalk = maxPetsPerWalk;
    }

    public String getPreferredCommunication() {
        return preferredCommunication;
    }

    public void setPreferredCommunication(String preferredCommunication) {
        this.preferredCommunication = preferredCommunication;
    }

    public Boolean getDeclarationAccurate() {
        return declarationAccurate;
    }

    public void setDeclarationAccurate(Boolean declarationAccurate) {
        this.declarationAccurate = declarationAccurate;
    }

    public Boolean getDeclarationVerifyOk() {
        return declarationVerifyOk;
    }

    public void setDeclarationVerifyOk(Boolean declarationVerifyOk) {
        this.declarationVerifyOk = declarationVerifyOk;
    }

    public Boolean getDeclarationComply() {
        return declarationComply;
    }

    public void setDeclarationComply(Boolean declarationComply) {
        this.declarationComply = declarationComply;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}