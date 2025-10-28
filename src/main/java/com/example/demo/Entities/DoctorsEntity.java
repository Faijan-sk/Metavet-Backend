package com.example.demo.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Enum.AppointmentStatus;
import com.example.demo.Enum.DoctorProfileStatus;
import com.example.demo.Enum.EmploymentStatus;
import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "doctors_entity")
public class DoctorsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    // Foreign key relationship with UsersEntity
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsersEntity user;


    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

  


    @Size(min = 3, max = 150, message = "Hospital/Clinic name must be between 3 and 150 characters")
    @Column(name = "hospital_clinic_name", length = 150)
    private String hospitalClinicName;

    
    @Size(min = 10, max = 300, message = "Address must be between 10 and 300 characters")
    @Column(name = "hospital_clinic_address",  length = 300)
    private String hospitalClinicAddress;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    @Column(name = "pincode", length = 6)
    private String pincode;

    @NotBlank(message = "address is required")
    @Column(length = 200, nullable = false)
    private String address;

    @Column(length = 50)
    private String country;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "City must contain only letters and spaces, 2-50 characters")
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "State must contain only letters and spaces, 2-50 characters")
    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    @Column(name = "bio", length = 500)
    private String bio;

    @NotNull(message = "Consultation fee is required")
    @Min(value = 0, message = "Consultation fee cannot be negative")
    @Max(value = 50000, message = "Consultation fee cannot exceed 50000")
    @Column(name = "consultation_fee", nullable = false)
    private Double consultationFee;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", nullable = false)
    private DoctorProfileStatus doctorProfileStatus ;
    

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAvailable = true;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    // Professional Information
    @NotBlank(message = "License number is required")
    @Pattern(regexp = "^[A-Z0-9]{6,20}$", message = "License number must be 6-20 alphanumeric characters")
    @Column(name = "license_number", unique = true, nullable = false, length = 20)
    private String licenseNumber;

    @Column(nullable = false)
    private LocalDate licenseIssueDate;

    @Column
    private LocalDate licenseExpiryDate;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 200, message = "Qualification must be between 2 and 200 characters")
    @Column(name = "qualification", nullable = false, length = 200)
    private String qualification;

    @NotBlank(message = "Specialization is required")
    @Size(min = 3, max = 100, message = "Specialization must be between 3 and 100 characters")
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

  

    @Column(length = 100)
    private String previousWorkplace;

    // Employment Information
    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column
    private LocalDate resignationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    // System Information
    @Column(nullable = false)
    private Boolean isActive;

    @Column(length = 50)
    private String createdBy;

    @Column(length = 50)
    private String updatedBy;
 
    @Column(length = 15)
    private String emergencyContactNumber;

    // Getters and Setters
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

 //   public List<DoctorAvailabilityEntity> getAvailabilities() {
//        return availabilities;
//    }
//
//    public void setAvailabilities(List<DoctorAvailabilityEntity> availabilities) {
//        this.availabilities = availabilities;
//    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getHospitalClinicName() {
        return hospitalClinicName;
    }

    public void setHospitalClinicName(String hospitalClinicName) {
        this.hospitalClinicName = hospitalClinicName;
    }

    public String getHospitalClinicAddress() {
        return hospitalClinicAddress;
    }

    public void setHospitalClinicAddress(String hospitalClinicAddress) {
        this.hospitalClinicAddress = hospitalClinicAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(LocalDate licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

 

    public String getPreviousWorkplace() {
        return previousWorkplace;
    }

    public void setPreviousWorkplace(String previousWorkplace) {
        this.previousWorkplace = previousWorkplace;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(LocalDate resignationDate) {
        this.resignationDate = resignationDate;
    }

   

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public DoctorsEntity() {
    }

  
  	
    public DoctorsEntity(Long doctorId, UsersEntity user, List<DoctorAvailabilityEntity> availabilities,
            Integer experienceYears, String hospitalClinicName, String hospitalClinicAddress, String pincode,
            String address, String country, String city, String state, String bio, Double consultationFee,
            Boolean isAvailable, Boolean profileCompleted, LocalDateTime createdAt, LocalDateTime updatedAt,
            Gender gender, LocalDate dateOfBirth, String licenseNumber, LocalDate licenseIssueDate,
            LocalDate licenseExpiryDate, String qualification, String specialization, Integer yearsOfExperience,
            String previousWorkplace, LocalDate joiningDate, LocalDate resignationDate,
             EmploymentType employmentType, Boolean isActive, String createdBy,
            String updatedBy, String emergencyContactNumber) {
        this.doctorId = doctorId;
        this.user = user;
//        this.availabilities = availabilities; 
        this.experienceYears = experienceYears;
        this.hospitalClinicName = hospitalClinicName;
        this.hospitalClinicAddress = hospitalClinicAddress;
        this.pincode = pincode;
        this.address = address;
        this.country = country;
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.consultationFee = consultationFee;
        this.isAvailable = isAvailable;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.licenseNumber = licenseNumber;
        this.licenseIssueDate = licenseIssueDate;
        this.licenseExpiryDate = licenseExpiryDate;
        this.qualification = qualification;
        this.specialization = specialization;
       
        this.previousWorkplace = previousWorkplace;
        this.joiningDate = joiningDate;
        this.resignationDate = resignationDate;
       
        this.employmentType = employmentType;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.emergencyContactNumber = emergencyContactNumber;
    }
    
    public DoctorProfileStatus getDoctorProfileStatus() {
		return doctorProfileStatus;
	}

	public void setDoctorProfileStatus(DoctorProfileStatus doctorProfileStatus) {
		this.doctorProfileStatus = doctorProfileStatus;
	}

	@PrePersist
    @PreUpdate
    public void validateAppointmentStatus() {
        if (this.doctorProfileStatus == null) {
            this.doctorProfileStatus = DoctorProfileStatus.PENDING;
        }
        // Validate if the status is a valid enum value
        try {
            AppointmentStatus.valueOf(this.doctorProfileStatus.name());
        } catch (Exception e) {
            this.doctorProfileStatus = DoctorProfileStatus.PENDING;
        }
    }
}
