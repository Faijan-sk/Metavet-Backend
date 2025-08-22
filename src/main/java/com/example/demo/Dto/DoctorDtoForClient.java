package com.example.demo.Dto;

public class DoctorDtoForClient {
	
/*
 * doctor Info from UsersEntity
 */
	private Long docotrUid;
	private String email;
	private String phoneNumber;
	private String  firstName;
	private String lastName ;
	
/*
* doctor Info from DoctorEntity
*/
	private Long doctorId;
	private Integer experienceYears;
	private String address;
	private String city;
	private String state;
	private String bio;
	private Double consultationFee;
	private String licenseNumber;
	private String qualification;
	private String specialization;
	
	
	


// No-args constructor
public DoctorDtoForClient() {
}

// All-args constructor
public DoctorDtoForClient(Long docotrUid, String email, String phoneNumber, String firstName, String lastName,
                          Long doctorId, Integer experienceYears, String address, String city, String state,
                          String bio, Double consultationFee, String licenseNumber, String qualification,
                          String specialization, Integer yearsOfExperience) {
    this.docotrUid = docotrUid;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.doctorId = doctorId;
    this.experienceYears = experienceYears;
    this.address = address;
    this.city = city;
    this.state = state;
    this.bio = bio;
    this.consultationFee = consultationFee;
    this.licenseNumber = licenseNumber;
    this.qualification = qualification;
    this.specialization = specialization;

}

public Long getDocotrUid() {
	return docotrUid;
}

public void setDocotrUid(Long docotrUid) {
	this.docotrUid = docotrUid;
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





}
