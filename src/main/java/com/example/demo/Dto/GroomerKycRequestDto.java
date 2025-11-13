package com.example.demo.Dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entities.GroomerKyc.ApplicationStatus;
import com.example.demo.Entities.GroomerKyc.ServiceLocationType;
import com.example.demo.Entities.GroomerKyc.ServiceOffered;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GroomerKycRequestDto {
	
	
	private String fullLegalName;
	
	private String businessName;
	
	private Boolean hasBusinessLicense;
	
	private MultipartFile businessLicenseDoc;
	
	private String businessLicenseFilePath;
	
	private String businessLicenseFileURL;
	
	
	private String email;
	
	private String phone;
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private ServiceLocationType serviceLocationType;
	
	private Integer yearsExperience;
	
	// ********************************************Personal Credential   // ****************************************

	private Boolean hasGroomingCert;
	
	private String groomingCertDetails;
	
	private MultipartFile GroomingCertificateDoc;
	
	private String groomingCertificateDocPath;
	
	private String groomingCertificateDocURL;
	
	
	
	private Boolean hasFirstAidCert ;
	
	private MultipartFile firstAidCertificateDoc ;
	
	private String firstAidCertificatePath;
	
	private String firstAidCertificateURL;
	
	
	
	
	private Boolean hasInsurance;
	
	
	private String insuranceProvider;

	private String insurancePolicyNumber;

	private LocalDate insuranceExpiry;
	
	
	private MultipartFile insuranceDoc;
	
	private String insuaranceDoccPath;
	
	private String insuaranceDoccURL;
	
	
	
	
	private Boolean criminalCheck ;
	
	private MultipartFile crimialRecordDoc;

	private String criminalDocPath;
	
	private String criminalDocURL;
	
	
	//***************************************** Liability & Complience***********************************

	private Boolean liabilityInsurance ;

	private String liabilityProvider;

	private String liabilityPolicyNumber;

	private LocalDate liabilityExpiry;


	private MultipartFile liabilityInsuaranceDoc;
	
	private String liabilityDocPath;
	
	private String liabilityDocURL;
	
	
	
	
	
	private Boolean hasIncidentPolicy;

	@Column(length = 1000)
	private String incidentPolicyDetails;
	
	// *****************************************Operation & service  **************************

	@Enumerated(EnumType.STRING)
	@Column(name = "services_offered")
	private List<ServiceOffered> servicesOffered;
	
	private String servicesOtherText;
	
	private String servicesPrices;
	
	private Integer averageAppointmentDuration;

	private String serviceRadius;

	
	// ********************************************* Declaration******************************************

	
	private Boolean declarationAccuracy ;

	private Boolean declarationConsentVerify ;

	private Boolean declarationComply ;

	private String signature;

	private LocalDate signatureDate;
	
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;

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

	public MultipartFile getGroomingCertificateDoc() {
		return GroomingCertificateDoc;
	}

	public void setGroomingCertificateDoc(MultipartFile groomingCertificateDoc) {
		GroomingCertificateDoc = groomingCertificateDoc;
	}

	public String getGroomingCertificateDocPath() {
		return groomingCertificateDocPath;
	}

	public void setGroomingCertificateDocPath(String groomingCertificateDocPath) {
		this.groomingCertificateDocPath = groomingCertificateDocPath;
	}

	public String getGroomingCertificateDocURL() {
		return groomingCertificateDocURL;
	}

	public void setGroomingCertificateDocURL(String groomingCertificateDocURL) {
		this.groomingCertificateDocURL = groomingCertificateDocURL;
	}

	public Boolean getHasFirstAidCert() {
		return hasFirstAidCert;
	}

	public void setHasFirstAidCert(Boolean hasFirstAidCert) {
		this.hasFirstAidCert = hasFirstAidCert;
	}

	public MultipartFile getFirstAidCertificateDoc() {
		return firstAidCertificateDoc;
	}

	public void setFirstAidCertificateDoc(MultipartFile firstAidCertificateDoc) {
		this.firstAidCertificateDoc = firstAidCertificateDoc;
	}

	public String getFirstAidCertificatePath() {
		return firstAidCertificatePath;
	}

	public void setFirstAidCertificatePath(String firstAidCertificatePath) {
		this.firstAidCertificatePath = firstAidCertificatePath;
	}

	public String getFirstAidCertificateURL() {
		return firstAidCertificateURL;
	}

	public void setFirstAidCertificateURL(String firstAidCertificateURL) {
		this.firstAidCertificateURL = firstAidCertificateURL;
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

	public String getInsuaranceDoccPath() {
		return insuaranceDoccPath;
	}

	public void setInsuaranceDoccPath(String insuaranceDoccPath) {
		this.insuaranceDoccPath = insuaranceDoccPath;
	}

	public String getInsuaranceDoccURL() {
		return insuaranceDoccURL;
	}

	public void setInsuaranceDoccURL(String insuaranceDoccURL) {
		this.insuaranceDoccURL = insuaranceDoccURL;
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

	public MultipartFile getLiabilityInsuaranceDoc() {
		return liabilityInsuaranceDoc;
	}

	public void setLiabilityInsuaranceDoc(MultipartFile liabilityInsuaranceDoc) {
		this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
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

	public Integer getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(Integer yearsExperience) {
		this.yearsExperience = yearsExperience;
	}

	
	
	
	
	
	
	
}
