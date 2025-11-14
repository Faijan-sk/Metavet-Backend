package com.example.demo.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "behaviour_specialists")
public class BehaviouristKyc extends BaseEntity {

    // ---------------- Personal & Business Information ----------------
    @Column(nullable = false, length = 200)
    private String fullLegalName;

    @Column(length = 200)
    private String businessName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String address;

    @Column(length = 200)
    private String serviceArea;

    @Column(length = 3)
    private String yearsExperience;

    // ---------------- Professional Credentials ----------------
    @Column(name = "has_certifications")
    private Boolean hasBehaviouralCertifications = false;

    @Column(name = "behavioural_certificate", columnDefinition = "BYTEA")
    private byte[] behaviouralCertificateDoc;

    @Column(length = 500)
    private String behaviouralCertificateDetails;

    @Column(length = 255)
    private String behaviouralCertificateFilePath;

    @Column(length = 500)
    private String educationBackground;

    // ---------------- Insurance / Criminal / Liability ----------------
    @Column(name = "has_insurance")
    private Boolean hasInsurance = false;

    @Column(length = 200)
    private String insuranceProvider;

    @Column(length = 100)
    private String insurancePolicyNumber;

    private LocalDate insuranceExpiry;

    @Column(name = "insurance_doc", columnDefinition = "BYTEA")
    private byte[] insuranceDoc;

    @Column(length = 255)
    private String insuranceDocPath;

    @Column(name = "has_criminal_check")
    private Boolean hasCriminalCheck = false;

    @Column(name = "criminal_record_doc", columnDefinition = "BYTEA")
    private byte[] criminalRecordDoc;

    @Column(length = 255)
    private String criminalDocPath;

    // Liability
    @Column(name = "liability_insurance")
    private Boolean liabilityInsurance = false;

    @Column(name = "liability_insurance_doc", columnDefinition = "BYTEA")
    private byte[] liabilityInsuranceDoc;

    @Column(length = 255)
    private String liabilityDocPath;

    // ---------------- Business License ----------------
    @Column(name = "business_license")
    private Boolean hasBusinessLicense = false;

    @Column(name = "business_license_doc", columnDefinition = "BYTEA")
    private byte[] businessLicenseDoc;

    @Column(length = 255)
    private String businessLicenseFilePath;

    // ---------------- Practice Details (Enums lists) ----------------
    /**
     * servicesOffered mapped as a separate collection table:
     * behaviourist_services_offered (behaviourist_id, service)
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "behaviourist_services_offered",
            joinColumns = @JoinColumn(name = "behaviourist_id"))
    @Column(name = "service", length = 50)
    @Enumerated(EnumType.STRING)
    private List<ServiceOffered> servicesOffered;

    @Column(length = 500)
    private String servicesOtherText;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "behaviourist_specializations",
            joinColumns = @JoinColumn(name = "behaviourist_id"))
    @Column(name = "specialization", length = 50)
    @Enumerated(EnumType.STRING)
    private List<Specialization> specializations;

    @Column(length = 500)
    private String specializationOtherText;

    @Column(length = 300)
    private String serviceRadius;

    // ---------------- Declarations & Signature ----------------
    @Column(name = "info_true")
    private Boolean infoTrue = false;

    @Column(name = "verify_ok")
    private Boolean verifyOk = false;

    @Column(name = "abide_standards")
    private Boolean abideStandards = false;

    @Column(length = 200)
    private String signature;

    private LocalDate signatureDate;

    // ---------------- Approval Status ----------------
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    // ------------ Enums (you can move these to separate files) ------------
    public enum ApprovalStatus {
        PENDING,
        APPROVED,
        REJECTED,
        UNDER_REVIEW
    }

    public enum ServiceOffered {
        BEHAVIOURAL_CONSULTATION,
        TRAINING,
        FOLLOW_UP,
        VIRTUAL_SESSIONS,
        OTHER
    }

    public enum Specialization {
        AGGRESSION,
        SEPARATION_ANXIETY,
        OBEDIENCE,
        PUPPY_TRAINING,
        OTHER
    }

    // ---------------- Getters & Setters ----------------
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

	public byte[] getBehaviouralCertificateDoc() {
		return behaviouralCertificateDoc;
	}

	public void setBehaviouralCertificateDoc(byte[] behaviouralCertificateDoc) {
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

	public byte[] getInsuranceDoc() {
		return insuranceDoc;
	}

	public void setInsuranceDoc(byte[] insuranceDoc) {
		this.insuranceDoc = insuranceDoc;
	}

	public String getInsuranceDocPath() {
		return insuranceDocPath;
	}

	public void setInsuranceDocPath(String insuranceDocPath) {
		this.insuranceDocPath = insuranceDocPath;
	}

	public Boolean getHasCriminalCheck() {
		return hasCriminalCheck;
	}

	public void setHasCriminalCheck(Boolean hasCriminalCheck) {
		this.hasCriminalCheck = hasCriminalCheck;
	}

	public byte[] getCriminalRecordDoc() {
		return criminalRecordDoc;
	}

	public void setCriminalRecordDoc(byte[] criminalRecordDoc) {
		this.criminalRecordDoc = criminalRecordDoc;
	}

	public String getCriminalDocPath() {
		return criminalDocPath;
	}

	public void setCriminalDocPath(String criminalDocPath) {
		this.criminalDocPath = criminalDocPath;
	}

	public Boolean getLiabilityInsurance() {
		return liabilityInsurance;
	}

	public void setLiabilityInsurance(Boolean liabilityInsurance) {
		this.liabilityInsurance = liabilityInsurance;
	}

	public byte[] getLiabilityInsuranceDoc() {
		return liabilityInsuranceDoc;
	}

	public void setLiabilityInsuranceDoc(byte[] liabilityInsuranceDoc) {
		this.liabilityInsuranceDoc = liabilityInsuranceDoc;
	}

	public String getLiabilityDocPath() {
		return liabilityDocPath;
	}

	public void setLiabilityDocPath(String liabilityDocPath) {
		this.liabilityDocPath = liabilityDocPath;
	}

	public Boolean getHasBusinessLicense() {
		return hasBusinessLicense;
	}

	public void setHasBusinessLicense(Boolean hasBusinessLicense) {
		this.hasBusinessLicense = hasBusinessLicense;
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

    // add remaining getters/setters for any extra fields if required
}
