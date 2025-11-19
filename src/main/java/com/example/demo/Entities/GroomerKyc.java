package com.example.demo.Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
//@Table(name = "metavet_to_groomer_kyc")
public class GroomerKyc extends BaseEntity {

	// Personal & Business Information
	@Column(nullable = false)
	private String fullLegalName;

	private String businessName;

	private Boolean hasBusinessLicense = false;

	@Column(name = "business_license_doc", columnDefinition = "BYTEA")
	private byte[] businessLicenseDoc;

	private String businessLicenseFilePath;

	@Column(nullable = false)
	@Email
	private String email;

	private String phone;

	@Column(length = 500)
	private String address;

	@Enumerated(EnumType.STRING)
	private ServiceLocationType serviceLocationType;

	private Integer yearsExperience;

	// ********************************************Personal Credential ****************************************

	// Professional Credentials
	private Boolean hasGroomingCert = false;

	private String groomingCertDetails;

	@Column(name = "grooming_certi_docc", columnDefinition = "BYTEA")
	private byte[] GroomingCertificateDoc;

	private String groomingCertificateDocPath;

	private Boolean hasFirstAidCert = false;

	@Column(name = "first_Aid_Certificate_doc", columnDefinition = "BYTEA")
	private byte[] firstAidCertificateDoc;

	private String firstAidCertificatePath;

	private Boolean hasInsurance = false;

	private String insuranceProvider;

	private String insurancePolicyNumber;

	private LocalDate insuranceExpiry;

	@Column(name = "insuarance_Doc", columnDefinition = "BYTEA")
	private byte[] insuranceDoc;

	private String insuaranceDoccPath;

	private Boolean criminalCheck = false;

	@Column(name = "criminal_record_doc", columnDefinition = "BYTEA")
	private byte[] crimialRecordDoc;

	private String criminalDocPath;
	
	

//***************************************** Liability & Complience***********************************

	// Liability & Compliance
	private Boolean liabilityInsurance = false;

	private String liabilityProvider;

	private String liabilityPolicyNumber;

	private LocalDate liabilityExpiry;

	@Column(name = "liability_induarance_doc", columnDefinition = "BYTEA")
	private byte[] liabilityInsuaranceDoc;

	private String liabilityDocPath;

	private Boolean hasIncidentPolicy = false;

	@Column(length = 1000)
	private String incidentPolicyDetails;

	// *****************************************Operation & service
	// **************************

	// Operations & Services
	@Enumerated(EnumType.STRING)
	@Column(name = "services_offered")
	private List<ServiceOffered> servicesOffered;

	private String servicesOtherText;

	@Column(length = 1000)
	private String servicesPrices;

	private Integer averageAppointmentDuration;

	private String serviceRadius;

	// *********************************************
	// Declaration******************************************

	// Declarations
	@Column(nullable = false)
	private Boolean declarationAccuracy = false;

	@Column(nullable = false)
	private Boolean declarationConsentVerify = false;

	@Column(nullable = false)
	private Boolean declarationComply = false;

	private String signature;

	private LocalDate signatureDate;

	// Application Status
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status = ApplicationStatus.PENDING;

	// Enums
	public enum ServiceLocationType {
		MOBILE, SALON, HOME_BASED
	}

	public enum ApplicationStatus {
		PENDING, APPROVED, REJECTED, UNDER_REVIEW
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

	public byte[] getGroomingCertificateDoc() {
		return GroomingCertificateDoc;
	}

	public void setGroomingCertificateDoc(byte[] groomingCertificateDoc) {
		GroomingCertificateDoc = groomingCertificateDoc;
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

	public byte[] getFirstAidCertificateDoc() {
		return firstAidCertificateDoc;
	}

	public void setFirstAidCertificateDoc(byte[] firstAidCertificateDoc) {
		this.firstAidCertificateDoc = firstAidCertificateDoc;
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

	public byte[] getInsuranceDoc() {
		return insuranceDoc;
	}

	public void setInsuranceDoc(byte[] insuranceDoc) {
		this.insuranceDoc = insuranceDoc;
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

	public byte[] getCrimialRecordDoc() {
		return crimialRecordDoc;
	}

	public void setCrimialRecordDoc(byte[] crimialRecordDoc) {
		this.crimialRecordDoc = crimialRecordDoc;
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

	public byte[] getLiabilityInsuaranceDoc() {
		return liabilityInsuaranceDoc;
	}

	public void setLiabilityInsuaranceDoc(byte[] liabilityInsuaranceDoc) {
		this.liabilityInsuaranceDoc = liabilityInsuaranceDoc;
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

	

	public String getLiabilityDocPath() {
		return liabilityDocPath;
	}

	public void setLiabilityDocPath(String liabilityDocPath) {
		this.liabilityDocPath = liabilityDocPath;
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
	
	public enum ServiceOffered {
	    FULL_GROOM("FULL_GROOM"),
	    BATH_BRUSH("BATH_BRUSH"),
	    NAIL_TRIM("NAIL_TRIM"),
	    EAR_CLEANING("EAR_CLEANING"),
	    DESHEDDING("DESHEDDING"),
	    SPECIALTY_CUT("SPECIALTY_CUT"),
	    OTHER("OTHER");

	    private final String label;

	    ServiceOffered(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
	}

}