package com.example.demo.Dto;

import com.example.demo.Enum.EmploymentType;
import com.example.demo.Enum.Gender;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateDoctorRequest {

    // User ID reference
    @NotNull(message = "User ID is required")
    private Long userId;

    // Professional Information
    @NotBlank(message = "License number is required")
    @Pattern(regexp = "^[A-Z0-9]{6,20}$", message = "License number must be 6-20 alphanumeric characters")
    private String licenseNumber;

    @NotNull(message = "License issue date is required")
    private LocalDate licenseIssueDate;

    private LocalDate licenseExpiryDate;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 200, message = "Qualification must be between 2 and 200 characters")
    private String qualification;

    @NotBlank(message = "Specialization is required")
    @Size(min = 3, max = 100, message = "Specialization must be between 3 and 100 characters")
    private String specialization;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experienceYears;

    // Hospital/Clinic Information
    @Size(min = 3, max = 150, message = "Hospital/Clinic name must be between 3 and 150 characters")
    private String hospitalClinicName;

    @Size(min = 10, max = 300, message = "Hospital address must be between 10 and 300 characters")
    private String hospitalClinicAddress;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    // Address Information
    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 200, message = "Address must be between 10 and 200 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "City must contain only letters and spaces, 2-50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "State must contain only letters and spaces, 2-50 characters")
    private String state;

    private String country;

    // Personal Information
    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[0-9]{10}$", message = "Emergency contact must be 10 digits")
    private String emergencyContactNumber;

    // Employment Information
    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @Size(max = 100, message = "Previous workplace cannot exceed 100 characters")
    private String previousWorkplace;

    // Professional Details
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "0.0", message = "Consultation fee cannot be negative")
    @DecimalMax(value = "50000.0", message = "Consultation fee cannot exceed 50000")
    private Double consultationFee;

    // Constructors
    public CreateDoctorRequest() {}

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getPreviousWorkplace() {
        return previousWorkplace;
    }

    public void setPreviousWorkplace(String previousWorkplace) {
        this.previousWorkplace = previousWorkplace;
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

    @Override
    public String toString() {
        return "CreateDoctorRequest{" +
                "userId=" + userId +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", qualification='" + qualification + '\'' +
                ", specialization='" + specialization + '\'' +
                ", experienceYears=" + experienceYears +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", consultationFee=" + consultationFee +
                '}';
    }
}