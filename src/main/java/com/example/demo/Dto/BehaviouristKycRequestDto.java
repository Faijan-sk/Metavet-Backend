//package com.example.demo.Dto;
//
//
//
//import java.time.LocalDate;
//import java.util.List;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.demo.Entities.BehaviouristKyc.ApprovalStatus;
//
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//
//public class BehaviouristKycRequestDto {
//
//	// Personal & Business Information
//	private String fullLegalName;
//
//	private String businessName;
//
//	private String email;
//
//	private String phone;
//
//	private String address;
//
//	private String serviceArea;
//
//	private String yearsExperience;
//
//	// Professional Credentials
//	private Boolean hasCertifications;
//
//	private MultipartFile behaviouralCertificateDoc;
//
//	private String behaviouralCertificateDetails;
//
//	private String behaviouralCertificateFilePath;
//
//	private String behaviouralCertificateFileURL;
//
//	private String educationBackground;
//
//	private Boolean hasInsurance;
//
//	private String insuranceProvider;
//
//	private String insurancePolicyNumber;
//
//	private LocalDate insuranceExpiry;
//
//	private MultipartFile insuranceDoc;
//
//	private String insuaranceDoccPath;
//
//	private String insuaranceDoccURL;
//
//	private Boolean criminalCheck;
//
//	private MultipartFile crimialRecordDoc;
//
//	private String criminalDocPath;
//
//	private String criminalDocURL;
//
//	// ******************* Liability
//
//	private Boolean liabilityInsurance;
//
//	private MultipartFile liabilityInsuaranceDoc;
//
//	private String liabilityDocPath;
//
//	private String liabilityDocURL;
//
//	private Boolean hasBusinessLicense;
//
//	private MultipartFile businessLicenseDoc;
//
//	private String businessLicenseFilePath;
//
//	private String businessLicenseFileURL;
//
//	// ******************** Practice Details *****************
//
//	private List<String> servicesOffered;
//
//	private List<String> specializations;
//
//	private String serviceRadius;
//
//	// *********************************
//
//	// Declarations
//	private Boolean infoTrue;
//
//	private Boolean verifyOk;
//
//	private Boolean abideStandards;
//
//	// Signature
//	private String signature;
//
//	private LocalDate signatureDate;
//
//	// Status & Tracking
//	@Enumerated(EnumType.STRING)
//	private ApprovalStatus status;
//
//	// Getters and Setters
//
//	public String getFullLegalName() {
//		return fullLegalName;
//	}
//
//	public void setFullLegalName(String fullLegalName) {
//		this.fullLegalName = fullLegalName;
//	}
//
//	public String getBusinessName() {
//		return businessName;
//	}
//
//	public void setBusinessName(String businessName) {
//		this.businessName = businessName;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPhone() {
//		return phone;
//	}
//
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getServiceArea() {
//		return serviceArea;
//	}
//
//	public void setServiceArea(String serviceArea) {
//		this.serviceArea = serviceArea;
//	}
//
//	public String getYearsExperience() {
//		return yearsExperience;
//	}
//
//	public void setYearsExperience(String yearsExperience) {
//		this.yearsExperience = yearsExperience;
//	}
//
//	public Boolean getHasCertifications() {
//		return hasCertifications;
//	}
//
//	public void setHasCertifications(Boolean hasCertifications) {
//		this.hasCertifications = hasCertifications;
//	}
//
//	public MultipartFile getBehaviouralCertificateDoc() {
//		return behaviouralCertificateDoc;
//	}
//
//	public void setBehaviouralCertificateDoc(MultipartFile behaviouralCertificateDoc) {
//		this.behaviouralCertificateDoc = behaviouralCertificateDoc;
//	}
//
//	public String getBehaviouralCertificateDetails() {
//		return behaviouralCertificateDetails;
//	}
//
//	public void setBehaviouralCertificateDetails(String behaviouralCertificateDetails) {
//		this.behaviouralCertificateDetails = behaviouralCertificateDetails;
//	}
//
//	public String getBehaviouralCertificateFilePath() {
//		return behaviouralCertificateFilePath;
//	}
//
//	public void setBehaviouralCertificateFilePath(String behaviouralCertificateFilePath) {
//		this.behaviouralCertificateFilePath = behaviouralCertificateFilePath;
//	}
//
//	public String getBehaviouralCertificateFileURL() {
//		return behaviouralCertificateFileURL;
//	}
//
//	public void setBehaviouralCertificateFileURL(String behaviouralCertificateFileURL) {
//		this.behaviouralCertificateFileURL = behaviouralCertificateFileURL;
//	}
//
//	public String getEducationBackground() {
//		return educationBackground;
//	}
//
//	public void setEducationBackground(String educationBackground) {
//		this.educationBackground = educationBackground;
//	}
//
//	public Boolean getHasInsurance() {
//		return hasInsurance;
//	}
//
//	public void setHasInsurance(Boolean hasInsurance) {
//		this.hasInsurance = hasInsurance;
//	}
//
//	public String getInsuranceProvider() {
//		return insuranceProvider;
//	}
//
//	public void setInsuranceProvider(String insuranceProvider) {
//		this.insuranceProvider = insuranceProvider;
//	}
//
//	public String getInsurancePolicyNumber() {
//		return insurancePolicyNumber;
//	}
//
//	public void setInsurancePolicyNumber(String insurancePolicyNumber) {
//		this.insurancePolicyNumber = insurancePolicyNumber;
//	}
//
//	public LocalDate getInsuranceExpiry() {
//		return insuranceExpiry;
//	}
//
//	public void setInsuranceExpiry(LocalDate insuranceExpiry) {
//		this.insuranceExpiry = insuranceExpiry;
//	}
//
//	public MultipartFile getInsuranceDoc() {
//		return insuranceDoc;
//	}
//
//	public void setInsuranceDoc(MultipartFile insuranceDoc) {
//		this.insuranceDoc = insuranceDoc;
//	}
//
//	public String getInsuaranceDoccPath() {
//		return insuaranceDoccPath;
//	}
//
//	public void setInsuaranceDoccPath(String insuaranceDoccPath) {
//		this.insuaranceDoccPath = insuaranceDoccPath;
//	}
//
//	public String getInsuaranceDoccURL() {
//		return insuaranceDoccURL;
//	}
//
//	public void setInsuaranceDoccURL(String insuaranceDoccURL) {
//		this.insuaranceDoccURL = insuaranceDoccURL;
//	}
//
//	public Boolean getCriminalCheck() {
//		return criminalCheck;
//	}
//
//	public void setCriminalCheck(Boolean criminalCheck) {
//		this.criminalCheck = criminalCheck;
//	}
//
//	public MultipartFile getCrimialRecordDoc() {
//		return crimialRecordDoc;
//	}
//
//	public void setCrimialRecordDoc(MultipartFile crimialRecordDoc) {
//		this.crimialRecordDoc = crimialRecordDoc;
//	}
//
//	public String getCriminalDocPath() {
//		return criminalDocPath;
//	}
//
//	public void setCriminalDocPath(String criminalDocPath) {
//		this.criminalDocPath = criminalDocPath;
//	}
//
//	public String getCriminalDocURL() {
//		return criminalDocURL;
//	}
//
//	public void setCriminalDocURL(String criminalDocURL) {
//		this.criminalDocURL = criminalDocURL;
//	}
//
//	public Boolean getLiabilityInsurance() {
//		return liabilityInsurance;
//	}
//
//	public void setLiabilityInsurance(Boolean liabilityInsurance) {
//		this.liabilityInsurance = liabilityInsurance;
//	}
//
//	public MultipartFile getLiabilityInsuaranceDoc() {
//		return liabilityInsuaranceDoc;
//	}
//
//	public void setLiabilityInsuaranceDoc(MultipartFile liabilityInsuaranceDoc) {
//		this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
//	}
//
//	public String getLiabilityDocPath() {
//		return liabilityDocPath;
//	}
//
//	public void setLiabilityDocPath(String liabilityDocPath) {
//		this.liabilityDocPath = liabilityDocPath;
//	}
//
//	public String getLiabilityDocURL() {
//		return liabilityDocURL;
//	}
//
//	public void setLiabilityDocURL(String liabilityDocURL) {
//		this.liabilityDocURL = liabilityDocURL;
//	}
//
//	public Boolean getHasBusinessLicense() {
//		return hasBusinessLicense;
//	}
//
//	public void setHasBusinessLicense(Boolean hasBusinessLicense) {
//		this.hasBusinessLicense = hasBusinessLicense;
//	}
//
//	public MultipartFile getBusinessLicenseDoc() {
//		return businessLicenseDoc;
//	}
//
//	public void setBusinessLicenseDoc(MultipartFile businessLicenseDoc) {
//		this.businessLicenseDoc = businessLicenseDoc;
//	}
//
//	public String getBusinessLicenseFilePath() {
//		return businessLicenseFilePath;
//	}
//
//	public void setBusinessLicenseFilePath(String businessLicenseFilePath) {
//		this.businessLicenseFilePath = businessLicenseFilePath;
//	}
//
//	public String getBusinessLicenseFileURL() {
//		return businessLicenseFileURL;
//	}
//
//	public void setBusinessLicenseFileURL(String businessLicenseFileURL) {
//		this.businessLicenseFileURL = businessLicenseFileURL;
//	}
//
//	public List<String> getServicesOffered() {
//		return servicesOffered;
//	}
//
//	public void setServicesOffered(List<String> servicesOffered) {
//		this.servicesOffered = servicesOffered;
//	}
//
//	public List<String> getSpecializations() {
//		return specializations;
//	}
//
//	public void setSpecializations(List<String> specializations) {
//		this.specializations = specializations;
//	}
//
//	public String getServiceRadius() {
//		return serviceRadius;
//	}
//
//	public void setServiceRadius(String serviceRadius) {
//		this.serviceRadius = serviceRadius;
//	}
//
//	public Boolean getInfoTrue() {
//		return infoTrue;
//	}
//
//	public void setInfoTrue(Boolean infoTrue) {
//		this.infoTrue = infoTrue;
//	}
//
//	public Boolean getVerifyOk() {
//		return verifyOk;
//	}
//
//	public void setVerifyOk(Boolean verifyOk) {
//		this.verifyOk = verifyOk;
//	}
//
//	public Boolean getAbideStandards() {
//		return abideStandards;
//	}
//
//	public void setAbideStandards(Boolean abideStandards) {
//		this.abideStandards = abideStandards;
//	}
//
//	public String getSignature() {
//		return signature;
//	}
//
//	public void setSignature(String signature) {
//		this.signature = signature;
//	}
//
//	public LocalDate getSignatureDate() {
//		return signatureDate;
//	}
//
//	public void setSignatureDate(LocalDate signatureDate) {
//		this.signatureDate = signatureDate;
//	}
//
//	public ApprovalStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(ApprovalStatus status) {
//		this.status = status;
//	}
//}
