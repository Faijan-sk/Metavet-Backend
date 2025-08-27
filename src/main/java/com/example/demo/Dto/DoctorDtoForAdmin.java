	package com.example.demo.Dto;
	
	import java.time.LocalDate;
	import java.time.LocalDateTime;
	
	import com.example.demo.Enum.AppointmentStatus;
	
	public class DoctorDtoForAdmin {
	    
	    /*
	     * doctor Info from UsersEntity
	     */
	    private Long doctorUid;
	    private String email;
	    private String phoneNumber;
	    private String firstName;
	    private String lastName;
	    private LocalDateTime createdAt;
	
	    /*
	     * doctor Info from DoctorEntity
	     */
	    private Long doctorId;
	    private Integer experienceYears;
	    private String hospitalClinicAddress;
	    private String city;
	    private String state;
	    private String bio;
	    private Double consultationFee;
	    private String licenseNumber;
	    private String qualification;
	    private String specialization;
	    private Boolean profileCompleted;
	    private LocalDate licenseIssueDate;
	    private LocalDate licenseExpiryDate;
	    private LocalDate joiningDate;
	    private Boolean isActive;
	    private Boolean isAvailable;
	    
	    // Additional admin-specific fields
	    private LocalDateTime updatedAt;
	    private String approvalStatus; // PENDING, APPROVED, REJECTED
	    private String adminNotes;
	    private Integer totalAppointments;
	    private Double totalEarnings;
	    
	    // ✅ Default constructor
	    public DoctorDtoForAdmin() {}
	
	    // ✅ All-args constructor
	    public DoctorDtoForAdmin(Long doctorUid, String email, String phoneNumber, String firstName, String lastName,
	                            LocalDateTime createdAt, Long doctorId, Integer experienceYears,
	                            String hospitalClinicAddress, String city, String state, String bio,
	                            Double consultationFee, String licenseNumber, String qualification,
	                            String specialization, Boolean profileCompleted,
	                            LocalDate licenseIssueDate, LocalDate licenseExpiryDate, LocalDate joiningDate,
	                            Boolean isActive, Boolean isAvailable, LocalDateTime updatedAt,
	                            String approvalStatus, String adminNotes, Integer totalAppointments, Double totalEarnings) {
	        this.doctorUid = doctorUid;
	        this.email = email;
	        this.phoneNumber = phoneNumber;
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.createdAt = createdAt;
	        this.doctorId = doctorId;
	        this.experienceYears = experienceYears;
	        this.hospitalClinicAddress = hospitalClinicAddress;
	        this.city = city;
	        this.state = state;
	        this.bio = bio;
	        this.consultationFee = consultationFee;
	        this.licenseNumber = licenseNumber;
	        this.qualification = qualification;
	        this.specialization = specialization;
	        this.profileCompleted = profileCompleted;
	        this.licenseIssueDate = licenseIssueDate;
	        this.licenseExpiryDate = licenseExpiryDate;
	        this.joiningDate = joiningDate;
	        this.isActive = isActive;
	        this.isAvailable = isAvailable;
	        this.updatedAt = updatedAt;
	        this.approvalStatus = approvalStatus;
	        this.adminNotes = adminNotes;
	        this.totalAppointments = totalAppointments;
	        this.totalEarnings = totalEarnings;
	    }
	
	    // ✅ Getters & Setters
	    public Long getDoctorUid() {
	        return doctorUid;
	    }
	
	    public void setDoctorUid(Long doctorUid) {
	        this.doctorUid = doctorUid;
	    }
	
	    public String getEmail() {
	        return email;
	    }
	
	    public void setEmail(String email) {
	        this.email = email;
	    }
	
	    public String getPhoneNumber() {
	        return phoneNumber;
	    }
	
	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }
	
	    public String getFirstName() {
	        return firstName;
	    }
	
	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }
	
	    public String getLastName() {
	        return lastName;
	    }
	
	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }
	
	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }
	
	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
	
	    public Long getDoctorId() {
	        return doctorId;
	    }
	
	    public void setDoctorId(Long doctorId) {
	        this.doctorId = doctorId;
	    }
	
	    public Integer getExperienceYears() {
	        return experienceYears;
	    }
	
	    public void setExperienceYears(Integer experienceYears) {
	        this.experienceYears = experienceYears;
	    }
	
	    public String getHospitalClinicAddress() {
	        return hospitalClinicAddress;
	    }
	
	    public void setHospitalClinicAddress(String hospitalClinicAddress) {
	        this.hospitalClinicAddress = hospitalClinicAddress;
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
	
	    public String getLicenseNumber() {
	        return licenseNumber;
	    }
	
	    public void setLicenseNumber(String licenseNumber) {
	        this.licenseNumber = licenseNumber;
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
	
	    public Boolean getProfileCompleted() {
	        return profileCompleted;
	    }
	
	    public void setProfileCompleted(Boolean profileCompleted) {
	        this.profileCompleted = profileCompleted;
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
	
	    public LocalDate getJoiningDate() {
	        return joiningDate;
	    }
	
	    public void setJoiningDate(LocalDate joiningDate) {
	        this.joiningDate = joiningDate;
	    }
	
	    public Boolean getIsActive() {
	        return isActive;
	    }
	
	    public void setIsActive(Boolean isActive) {
	        this.isActive = isActive;
	    }
	
	    public Boolean getIsAvailable() {
	        return isAvailable;
	    }
	
	    public void setIsAvailable(Boolean isAvailable) {
	        this.isAvailable = isAvailable;
	    }
	
	    // Admin-specific getters and setters
	    public LocalDateTime getupdatedAt() {
	        return updatedAt;
	    }
	
	    public void set(LocalDateTime updatedAt) {
	        this.updatedAt = updatedAt;
	    }
	
	    public String getApprovalStatus() {
	        return approvalStatus;
	    }
	
	    public void setApprovalStatus(String approvalStatus) {
	        this.approvalStatus = approvalStatus;
	    }
	
	    public String getAdminNotes() {
	        return adminNotes;
	    }
	
	    public void setAdminNotes(String adminNotes) {
	        this.adminNotes = adminNotes;
	    }
	
	    public Integer getTotalAppointments() {
	        return totalAppointments;
	    }
	
	    public void setTotalAppointments(Integer totalAppointments) {
	        this.totalAppointments = totalAppointments;
	    }
	
	    public Double getTotalEarnings() {
	        return totalEarnings;
	    }
	
	    public void setTotalEarnings(Double totalEarnings) {
	        this.totalEarnings = totalEarnings;
	    }
	
		public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
			// TODO Auto-generated method stub
			
		}
	}