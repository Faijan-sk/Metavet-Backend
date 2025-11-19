package com.example.demo.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "metavet_to_walker_kyc")
public class WalkerKyc extends BaseEntity {
    
    @Column(name = "full_legal_name", nullable = false)
    private String fullLegalName;
    
    @Column(name = "business_name")
    private String businessName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "service_area")
    private String serviceArea;
    
    @Column(name = "years_experience")
    private String yearsExperience;
    
    // Professional Credentials
    @Column(name = "has_pet_care_certifications")
    private Boolean hasPetCareCertifications = false;
    
    @Column(name = "pet_care_certification_details")
    private String hasPetCareCertificationsDetails;
    
    @Column(name = "pet_care_certificate", columnDefinition = "BYTEA")
    private byte[] petCareCertificationDoc;
    
    @Column(name = "certification_file_path")
    private String certificationFilePath;
    
    @Column(name = "bonded_or_insured")
    private Boolean bondedOrInsured = false;
    
    @Column(name = "bonded_or_insured_doc", columnDefinition = "BYTEA")
    private byte[] bondedOrInsuredDoc;
    
    @Column(name = "bonded_file_path")
    private String bondedFilePath;
    
    @Column(name = "has_first_aid")
    private Boolean hasFirstAid = false;
    
    @Column(name = "pet_first_aid_certificate_doc", columnDefinition = "BYTEA")
    private byte[] petFirstAidCertificateDoc;
    
    @Column(name = "first_aid_file_path")
    private String firstAidFilePath;
    
    @Column(name = "criminal_check")
    private Boolean criminalCheck;
    
    @Column(name = "criminal_record_doc", columnDefinition = "BYTEA")
    private byte[] crimialRecordDoc;
    
    @Column(name = "criminal_check_file_path")
    private String criminalCheckFilePath;
    
    // Liability & Insurance
    @Column(name = "liability_insurance")
    private Boolean liabilityInsurance = false;
    
    @Column(name = "liability_provider")
    private String liabilityProvider;
    
    @Column(name = "liability_policy_number")
    private String liabilityPolicyNumber;
    
    @Column(name = "insurance_expiry")
    private LocalDate insuranceExpiry;
    
    @Column(name = "liability_induarance_doc", columnDefinition = "BYTEA")
    private byte[] liabilityInsuaranceDoc;
    
    @Column(name = "liability_file_path")
    private String liabilityFilePath;
    
    @Column(name = "hasBusiness_Licence")
    private Boolean hasBusinessLicenseDoc = false;
    
    @Column(name = "business_license_doc", columnDefinition = "BYTEA")
    private byte[] businessLicenseDoc;
    
    @Column(name = "business_license_file_path")
    private String businessLicenseFilePath;
    
    // Operations
    @Column(name = "walk_radius")
    private String walkRadius;
    
    @Column(name = "max_pets_per_walk")
    private Integer maxPetsPerWalk;
    
    @Column(name = "preferred_communication")
    private String preferredCommunication;
    
    // Declarations
    @Column(name = "declaration_accurate")
    private Boolean declarationAccurate;
    
    @Column(name = "declaration_verify_ok")
    private Boolean declarationVerifyOk;
    
    @Column(name = "declaration_comply")
    private Boolean declarationComply;
    
    @Column(name = "signature")
    private String signature;
    
    @Column(name = "signature_date")
    private LocalDate signatureDate;
    
    @Column(name = "status")
    private String status; // PENDING, APPROVED, REJECTED
    
    @PrePersist
    protected void onCreate() {
        // CRITICAL: Call parent's @PrePersist to set createdAt and updatedAt
        super.onCreate();
        
        // Then set default status
        if (status == null) {
            status = "PENDING";
        }
    }

    // All getters and setters remain the same...
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

    public byte[] getPetCareCertificationDoc() {
        return petCareCertificationDoc;
    }

    public void setPetCareCertificationDoc(byte[] petCareCertificationDoc) {
        this.petCareCertificationDoc = petCareCertificationDoc;
    }

    public String getCertificationFilePath() {
        return certificationFilePath;
    }

    public void setCertificationFilePath(String certificationFilePath) {
        this.certificationFilePath = certificationFilePath;
    }

    public Boolean getBondedOrInsured() {
        return bondedOrInsured;
    }

    public void setBondedOrInsured(Boolean bondedOrInsured) {
        this.bondedOrInsured = bondedOrInsured;
    }

    public byte[] getBondedOrInsuredDoc() {
        return bondedOrInsuredDoc;
    }

    public void setBondedOrInsuredDoc(byte[] bondedOrInsuredDoc) {
        this.bondedOrInsuredDoc = bondedOrInsuredDoc;
    }

    public String getBondedFilePath() {
        return bondedFilePath;
    }

    public void setBondedFilePath(String bondedFilePath) {
        this.bondedFilePath = bondedFilePath;
    }

    public Boolean getHasFirstAid() {
        return hasFirstAid;
    }

    public void setHasFirstAid(Boolean hasFirstAid) {
        this.hasFirstAid = hasFirstAid;
    }

    public byte[] getPetFirstAidCertificateDoc() {
        return petFirstAidCertificateDoc;
    }

    public void setPetFirstAidCertificateDoc(byte[] petFirstAidCertificateDoc) {
        this.petFirstAidCertificateDoc = petFirstAidCertificateDoc;
    }

    public String getFirstAidFilePath() {
        return firstAidFilePath;
    }

    public void setFirstAidFilePath(String firstAidFilePath) {
        this.firstAidFilePath = firstAidFilePath;
    }

    public Boolean getCriminalCheck() {
        return criminalCheck;
    }

    public void setCriminalCheck(Boolean criminalCheck) {
        this.criminalCheck = criminalCheck;
    }

    public byte[] getCrimialRecordDoc() {
        return crimialRecordDoc;
    }

    public void setCrimialRecordDoc(byte[] crimialRecordDoc) {
        this.crimialRecordDoc = crimialRecordDoc;
    }

    public String getCriminalCheckFilePath() {
        return criminalCheckFilePath;
    }

    public void setCriminalCheckFilePath(String criminalCheckFilePath) {
        this.criminalCheckFilePath = criminalCheckFilePath;
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

    public byte[] getLiabilityInsuaranceDoc() {
        return liabilityInsuaranceDoc;
    }

    public void setLiabilityInsuaranceDoc(byte[] liabilityInsuaranceDoc) {
        this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
    }

    public String getLiabilityFilePath() {
        return liabilityFilePath;
    }

    public void setLiabilityFilePath(String liabilityFilePath) {
        this.liabilityFilePath = liabilityFilePath;
    }

    public Boolean getHasBusinessLicenseDoc() {
        return hasBusinessLicenseDoc;
    }

    public void setHasBusinessLicenseDoc(Boolean hasBusinessLicenseDoc) {
        this.hasBusinessLicenseDoc = hasBusinessLicenseDoc;
    }

    public byte[] getBusinessLicenseDoc() {
        return businessLicenseDoc;
    }

    public void setBusinessLicenseDoc(byte[] businessLicenseDoc) {
        this.businessLicenseDoc = businessLicenseDoc;
    }

    public String getBusinessLicenseFilePath() {
        return businessLicenseFilePath;
    }

    public void setBusinessLicenseFilePath(String businessLicenseFilePath) {
        this.businessLicenseFilePath = businessLicenseFilePath;
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