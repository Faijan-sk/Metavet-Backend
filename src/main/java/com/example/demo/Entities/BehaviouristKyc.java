//package com.example.demo.Entities;
//
//
//
//import jakarta.persistence.*;
//
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "behaviour_specialists")
//public class BehaviouristKyc extends BaseEntity{
//
//  
//
//    // Personal & Business Information
//    @Column(nullable = false, length = 200)
//    private String fullLegalName;
//
//    @Column(length = 200)
//    private String businessName;
//
//    @Column(nullable = false, unique = true, length = 150)
//    private String email;
//
//    @Column(length = 20)
//    private String phone;
//
//    @Column(length = 500)
//    private String address;
//
//    @Column(length = 200)
//    private String serviceArea;
//
//    @Column(length = 3)
//    private String yearsExperience;
//
//    
//    
//    
//    // Professional Credentials
//    @Column(name = "has_certifications")
//    private Boolean hasCertifications = false ;
//    
//    @Column(name = "Behavioural_certificate", columnDefinition = "BYTEA" )
//    private byte[] behaviouralCertificateDoc;
//    
//
//    @Column(length = 500)
//    private String  behaviouralCertificateDetails;
//
//    @Column(length = 255)
//    private String  behaviouralCertificateFilePath;
//    
//    
//    
//    
//    
//    
//
//    @Column(length = 500)
//    private String educationBackground;
//    
//    
//    
//
//	private Boolean hasInsurance = false;
//
//	private String insuranceProvider;
//
//	private String insurancePolicyNumber;
//
//	private LocalDate insuranceExpiry;
//
//	@Column(name = "insuarance_Doc", columnDefinition = "BYTEA")
//	private byte[] insuranceDoc;
//
//	private String insuaranceDoccPath;
//	
//	
//
//	private Boolean criminalCheck = false;
//
//	@Column(name = "criminal_record_doc", columnDefinition = "BYTEA")
//	private byte[] crimialRecordDoc;
//
//	private String criminalDocPath;
//	
//	
//	//*******************Liabality 
//
//    // Liability & Compliance
//    @Column(name = "liability_insurance")
//	private Boolean liabilityInsurance = false;
//
//	@Column(name = "liability_induarance_doc", columnDefinition = "BYTEA")
//	private byte[] liabilityInsuaranceDoc;
//
//	private String liabilityDocPath;
//
//	
//	
//	
//	
//	
//    @Column(name = "business_license")
//    private Boolean hasBusinessLicense = false;
//
//	@Column(name = "business_license_doc", columnDefinition = "BYTEA")
//	private byte[] businessLicenseDoc;
//
//	private String businessLicenseFilePath;
//
//	
//	//********************Practice Details *****************
//    // Practice Details
//    @ElementCollection
//    @CollectionTable(name = "specialist_services", joinColumns = @JoinColumn(name = "specialist_id"))
//    @Column(name = "service")
//    private List<String> servicesOffered;
//
//    @ElementCollection
//    @CollectionTable(name = "specialist_specializations", joinColumns = @JoinColumn(name = "specialist_id"))
//    @Column(name = "specialization")
//    private List<String> specializations;
//
//    @Column(length = 300)
//    private String serviceRadius;
//    
//    //*********************************
//
//    // Declarations
//    @Column(name = "info_true")
//    private Boolean infoTrue = false ;
//
//    @Column(name = "verify_ok")
//    private Boolean verifyOk = false ;
//
//    @Column(name = "abide_standards")
//    private Boolean abideStandards = false ;
//
//    // Signature
//    @Column(length = 200)
//    private String signature;
//
//    private LocalDate signatureDate;
//
//    // Status & Tracking
//    @Enumerated(EnumType.STRING)
//    @Column(length = 20)
//    private ApprovalStatus status = ApprovalStatus.PENDING;
//
//
//
//    // Enum for approval status
//    public enum ApprovalStatus {
//        PENDING,
//        APPROVED,
//        REJECTED,
//        UNDER_REVIEW
//    }
//
//
//
//	public String getFullLegalName() {
//		return fullLegalName;
//	}
//
//
//
//	public void setFullLegalName(String fullLegalName) {
//		this.fullLegalName = fullLegalName;
//	}
//
//
//
//	public String getBusinessName() {
//		return businessName;
//	}
//
//
//
//	public void setBusinessName(String businessName) {
//		this.businessName = businessName;
//	}
//
//
//
//	public String getEmail() {
//		return email;
//	}
//
//
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//
//
//	public String getPhone() {
//		return phone;
//	}
//
//
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//
//
//
//	public String getAddress() {
//		return address;
//	}
//
//
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//
//
//	public String getServiceArea() {
//		return serviceArea;
//	}
//
//
//
//	public void setServiceArea(String serviceArea) {
//		this.serviceArea = serviceArea;
//	}
//
//
//
//	public String getYearsExperience() {
//		return yearsExperience;
//	}
//
//
//
//	public void setYearsExperience(String yearsExperience) {
//		this.yearsExperience = yearsExperience;
//	}
//
//
//
//	public Boolean getHasCertifications() {
//		return hasCertifications;
//	}
//
//
//
//	public void setHasCertifications(Boolean hasCertifications) {
//		this.hasCertifications = hasCertifications;
//	}
//
//
//
//	public byte[] getBehaviouralCertificateDoc() {
//		return behaviouralCertificateDoc;
//	}
//
//
//
//	public void setBehaviouralCertificateDoc(byte[] behaviouralCertificateDoc) {
//		this.behaviouralCertificateDoc = behaviouralCertificateDoc;
//	}
//
//
//
//	public String getBehaviouralCertificateDetails() {
//		return behaviouralCertificateDetails;
//	}
//
//
//
//	public void setBehaviouralCertificateDetails(String behaviouralCertificateDetails) {
//		this.behaviouralCertificateDetails = behaviouralCertificateDetails;
//	}
//
//
//
//	public String getBehaviouralCertificateFilePath() {
//		return behaviouralCertificateFilePath;
//	}
//
//
//
//	public void setBehaviouralCertificateFilePath(String behaviouralCertificateFilePath) {
//		this.behaviouralCertificateFilePath = behaviouralCertificateFilePath;
//	}
//
//
//
//	public String getEducationBackground() {
//		return educationBackground;
//	}
//
//
//
//	public void setEducationBackground(String educationBackground) {
//		this.educationBackground = educationBackground;
//	}
//
//
//
//	public Boolean getHasInsurance() {
//		return hasInsurance;
//	}
//
//
//
//	public void setHasInsurance(Boolean hasInsurance) {
//		this.hasInsurance = hasInsurance;
//	}
//
//
//
//	public String getInsuranceProvider() {
//		return insuranceProvider;
//	}
//
//
//
//	public void setInsuranceProvider(String insuranceProvider) {
//		this.insuranceProvider = insuranceProvider;
//	}
//
//
//
//	public String getInsurancePolicyNumber() {
//		return insurancePolicyNumber;
//	}
//
//
//
//	public void setInsurancePolicyNumber(String insurancePolicyNumber) {
//		this.insurancePolicyNumber = insurancePolicyNumber;
//	}
//
//
//
//	public LocalDate getInsuranceExpiry() {
//		return insuranceExpiry;
//	}
//
//
//
//	public void setInsuranceExpiry(LocalDate insuranceExpiry) {
//		this.insuranceExpiry = insuranceExpiry;
//	}
//
//
//
//	public byte[] getInsuranceDoc() {
//		return insuranceDoc;
//	}
//
//
//
//	public void setInsuranceDoc(byte[] insuranceDoc) {
//		this.insuranceDoc = insuranceDoc;
//	}
//
//
//
//	public String getInsuaranceDoccPath() {
//		return insuaranceDoccPath;
//	}
//
//
//
//	public void setInsuaranceDoccPath(String insuaranceDoccPath) {
//		this.insuaranceDoccPath = insuaranceDoccPath;
//	}
//
//
//
//	public Boolean getCriminalCheck() {
//		return criminalCheck;
//	}
//
//
//
//	public void setCriminalCheck(Boolean criminalCheck) {
//		this.criminalCheck = criminalCheck;
//	}
//
//
//
//	public byte[] getCrimialRecordDoc() {
//		return crimialRecordDoc;
//	}
//
//
//
//	public void setCrimialRecordDoc(byte[] crimialRecordDoc) {
//		this.crimialRecordDoc = crimialRecordDoc;
//	}
//
//
//
//	public String getCriminalDocPath() {
//		return criminalDocPath;
//	}
//
//
//
//	public void setCriminalDocPath(String criminalDocPath) {
//		this.criminalDocPath = criminalDocPath;
//	}
//
//
//
//	public Boolean getLiabilityInsurance() {
//		return liabilityInsurance;
//	}
//
//
//
//	public void setLiabilityInsurance(Boolean liabilityInsurance) {
//		this.liabilityInsurance = liabilityInsurance;
//	}
//
//
//
//	public byte[] getLiabilityInsuaranceDoc() {
//		return liabilityInsuaranceDoc;
//	}
//
//
//
//	public void setLiabilityInsuaranceDoc(byte[] liabilityInsuaranceDoc) {
//		this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
//	}
//
//
//
//	public String getLiabilityDocPath() {
//		return liabilityDocPath;
//	}
//
//
//
//	public void setLiabilityDocPath(String liabilityDocPath) {
//		this.liabilityDocPath = liabilityDocPath;
//	}
//
//
//
//	public Boolean getHasBusinessLicense() {
//		return hasBusinessLicense;
//	}
//
//
//
//	public void setHasBusinessLicense(Boolean hasBusinessLicense) {
//		this.hasBusinessLicense = hasBusinessLicense;
//	}
//
//
//
//	public byte[] getBusinessLicenseDoc() {
//		return businessLicenseDoc;
//	}
//
//
//
//	public void setBusinessLicenseDoc(byte[] businessLicenseDoc) {
//		this.businessLicenseDoc = businessLicenseDoc;
//	}
//
//
//
//	public String getBusinessLicenseFilePath() {
//		return businessLicenseFilePath;
//	}
//
//
//
//	public void setBusinessLicenseFilePath(String businessLicenseFilePath) {
//		this.businessLicenseFilePath = businessLicenseFilePath;
//	}
//
//
//
//	public List<String> getServicesOffered() {
//		return servicesOffered;
//	}
//
//
//
//	public void setServicesOffered(List<String> servicesOffered) {
//		this.servicesOffered = servicesOffered;
//	}
//
//
//
//	public List<String> getSpecializations() {
//		return specializations;
//	}
//
//
//
//	public void setSpecializations(List<String> specializations) {
//		this.specializations = specializations;
//	}
//
//
//
//	public String getServiceRadius() {
//		return serviceRadius;
//	}
//
//
//
//	public void setServiceRadius(String serviceRadius) {
//		this.serviceRadius = serviceRadius;
//	}
//
//
//
//	public Boolean getInfoTrue() {
//		return infoTrue;
//	}
//
//
//
//	public void setInfoTrue(Boolean infoTrue) {
//		this.infoTrue = infoTrue;
//	}
//
//
//
//	public Boolean getVerifyOk() {
//		return verifyOk;
//	}
//
//
//
//	public void setVerifyOk(Boolean verifyOk) {
//		this.verifyOk = verifyOk;
//	}
//
//
//
//	public Boolean getAbideStandards() {
//		return abideStandards;
//	}
//
//
//
//	public void setAbideStandards(Boolean abideStandards) {
//		this.abideStandards = abideStandards;
//	}
//
//
//
//	public String getSignature() {
//		return signature;
//	}
//
//
//
//	public void setSignature(String signature) {
//		this.signature = signature;
//	}
//
//
//
//	public LocalDate getSignatureDate() {
//		return signatureDate;
//	}
//
//
//
//	public void setSignatureDate(LocalDate signatureDate) {
//		this.signatureDate = signatureDate;
//	}
//
//
//
//	public ApprovalStatus getStatus() {
//		return status;
//	}
//
//
//
//	public void setStatus(ApprovalStatus status) {
//		this.status = status;
//	}
//
//   
//
//    
//}