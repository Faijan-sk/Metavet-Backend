package com.example.demo.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.demo.Entities.GroomerKyc.ApplicationStatus;
import com.example.demo.Entities.GroomerKyc.ServiceLocationType;
import com.example.demo.Entities.GroomerKyc.ServiceOffered;

public class GroomerKycResponseDto {
	
	// Base Entity Fields
	private Long id;
	private UUID uid;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	// Personal & Business Information
	private String fullLegalName;
	private String businessName;
	private Boolean hasBusinessLicense;
	private String businessLicenseDocBase64;  // Base64 encoded document
	private String businessLicenseFilePath;
	
	private String email;
	private String phone;
	private String address;
	private ServiceLocationType serviceLocationType;
	private Integer yearsExperience;
	
	// Professional Credentials
	private Boolean hasGroomingCert;
	private String groomingCertDetails;
	private String groomingCertificateDocBase64;  // Base64 encoded document
	private String groomingCertificateDocPath;
	
	private Boolean hasFirstAidCert;
	private String firstAidCertificateDocBase64;  // Base64 encoded document
	private String firstAidCertificatePath;
	
	private Boolean hasInsurance;
	private String insuranceProvider;
	private String insurancePolicyNumber;
	private LocalDate insuranceExpiry;
	private String insuranceDocBase64;  // Base64 encoded document
	private String insuaranceDoccPath;
	
	private Boolean criminalCheck;
	private String criminalRecordDocBase64;  // Base64 encoded document
	private String criminalDocPath;
	
	// Liability & Compliance
	private Boolean liabilityInsurance;
	private String liabilityProvider;
	private String liabilityPolicyNumber;
	private LocalDate liabilityExpiry;
	private String liabilityInsuaranceDocBase64;  // Base64 encoded document
	private String liabilityDocPath;
	
	private Boolean hasIncidentPolicy;
	private String incidentPolicyDetails;
	
	// Operations & Services
	private List<ServiceOffered> servicesOffered;
	private String servicesOtherText;
	private String servicesPrices;
	private Integer averageAppointmentDuration;
	private String serviceRadius;
	
	// Declarations
	private Boolean declarationAccuracy;
	private Boolean declarationConsentVerify;
	private Boolean declarationComply;
	private String signature;
	private LocalDate signatureDate;
	
	// Application Status
	private ApplicationStatus status;

	// Getters and Setters
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

	public Boolean getHasBusinessLicense() {
		return hasBusinessLicense;
	}

	public void setHasBusinessLicense(Boolean hasBusinessLicense) {
		this.hasBusinessLicense = hasBusinessLicense;
	}

	public String getBusinessLicenseDocBase64() {
		return businessLicenseDocBase64;
	}

	public void setBusinessLicenseDocBase64(String businessLicenseDocBase64) {
		this.businessLicenseDocBase64 = businessLicenseDocBase64;
	}

	public String getBusinessLicenseFilePath() {
		return businessLicenseFilePath;
	}

	public void setBusinessLicenseFilePath(String businessLicenseFilePath) {
		this.businessLicenseFilePath = businessLicenseFilePath;
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

	public ServiceLocationType getServiceLocationType() {
		return serviceLocationType;
	}

	public void setServiceLocationType(ServiceLocationType serviceLocationType) {
		this.serviceLocationType = serviceLocationType;
	}

	public Integer getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(Integer yearsExperience) {
		this.yearsExperience = yearsExperience;
	}

	public Boolean getHasGroomingCert() {
		return hasGroomingCert;
	}

	public void setHasGroomingCert(Boolean hasGroomingCert) {
		this.hasGroomingCert = hasGroomingCert;
	}

	public String getGroomingCertDetails() {
		return groomingCertDetails;
	}

	public void setGroomingCertDetails(String groomingCertDetails) {
		this.groomingCertDetails = groomingCertDetails;
	}

	public String getGroomingCertificateDocBase64() {
		return groomingCertificateDocBase64;
	}

	public void setGroomingCertificateDocBase64(String groomingCertificateDocBase64) {
		this.groomingCertificateDocBase64 = groomingCertificateDocBase64;
	}

	public String getGroomingCertificateDocPath() {
		return groomingCertificateDocPath;
	}

	public void setGroomingCertificateDocPath(String groomingCertificateDocPath) {
		this.groomingCertificateDocPath = groomingCertificateDocPath;
	}

	public Boolean getHasFirstAidCert() {
		return hasFirstAidCert;
	}

	public void setHasFirstAidCert(Boolean hasFirstAidCert) {
		this.hasFirstAidCert = hasFirstAidCert;
	}

	public String getFirstAidCertificateDocBase64() {
		return firstAidCertificateDocBase64;
	}

	public void setFirstAidCertificateDocBase64(String firstAidCertificateDocBase64) {
		this.firstAidCertificateDocBase64 = firstAidCertificateDocBase64;
	}

	public String getFirstAidCertificatePath() {
		return firstAidCertificatePath;
	}

	public void setFirstAidCertificatePath(String firstAidCertificatePath) {
		this.firstAidCertificatePath = firstAidCertificatePath;
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

	public String getInsuranceDocBase64() {
		return insuranceDocBase64;
	}

	public void setInsuranceDocBase64(String insuranceDocBase64) {
		this.insuranceDocBase64 = insuranceDocBase64;
	}

	public String getInsuaranceDoccPath() {
		return insuaranceDoccPath;
	}

	public void setInsuaranceDoccPath(String insuaranceDoccPath) {
		this.insuaranceDoccPath = insuaranceDoccPath;
	}

	public Boolean getCriminalCheck() {
		return criminalCheck;
	}

	public void setCriminalCheck(Boolean criminalCheck) {
		this.criminalCheck = criminalCheck;
	}

	public String getCriminalRecordDocBase64() {
		return criminalRecordDocBase64;
	}

	public void setCriminalRecordDocBase64(String criminalRecordDocBase64) {
		this.criminalRecordDocBase64 = criminalRecordDocBase64;
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

	public LocalDate getLiabilityExpiry() {
		return liabilityExpiry;
	}

	public void setLiabilityExpiry(LocalDate liabilityExpiry) {
		this.liabilityExpiry = liabilityExpiry;
	}

	public String getLiabilityInsuaranceDocBase64() {
		return liabilityInsuaranceDocBase64;
	}

	public void setLiabilityInsuaranceDocBase64(String liabilityInsuaranceDocBase64) {
		this.liabilityInsuaranceDocBase64 = liabilityInsuaranceDocBase64;
	}

	public String getLiabilityDocPath() {
		return liabilityDocPath;
	}

	public void setLiabilityDocPath(String liabilityDocPath) {
		this.liabilityDocPath = liabilityDocPath;
	}

	public Boolean getHasIncidentPolicy() {
		return hasIncidentPolicy;
	}

	public void setHasIncidentPolicy(Boolean hasIncidentPolicy) {
		this.hasIncidentPolicy = hasIncidentPolicy;
	}

	public String getIncidentPolicyDetails() {
		return incidentPolicyDetails;
	}

	public void setIncidentPolicyDetails(String incidentPolicyDetails) {
		this.incidentPolicyDetails = incidentPolicyDetails;
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

	public String getServicesPrices() {
		return servicesPrices;
	}

	public void setServicesPrices(String servicesPrices) {
		this.servicesPrices = servicesPrices;
	}

	public Integer getAverageAppointmentDuration() {
		return averageAppointmentDuration;
	}

	public void setAverageAppointmentDuration(Integer averageAppointmentDuration) {
		this.averageAppointmentDuration = averageAppointmentDuration;
	}

	public String getServiceRadius() {
		return serviceRadius;
	}

	public void setServiceRadius(String serviceRadius) {
		this.serviceRadius = serviceRadius;
	}

	public Boolean getDeclarationAccuracy() {
		return declarationAccuracy;
	}

	public void setDeclarationAccuracy(Boolean declarationAccuracy) {
		this.declarationAccuracy = declarationAccuracy;
	}

	public Boolean getDeclarationConsentVerify() {
		return declarationConsentVerify;
	}

	public void setDeclarationConsentVerify(Boolean declarationConsentVerify) {
		this.declarationConsentVerify = declarationConsentVerify;
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

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
}