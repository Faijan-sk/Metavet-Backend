package com.example.demo.Entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.math.BigDecimal;

@Entity
@Table(name = "pets_entity")
public class PetsEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private Long pid;
       
    // Foreign key relationship with UsersEntity (Pet Owner)
    @ManyToOne
    private UsersEntity owner;
   
    // Foreign key relationship with DoctorsEntity (Treating Doctor) - Optional
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctorId", nullable = true)
    private DoctorsEntity treatingDoctor;
    
    
    @NotBlank(message = "Pet name is required")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z\\s]{2,50}$", message = "Pet name must contain only letters and spaces")
    @Column(name = "pet_name", nullable = false, length = 50)
    private String petName;
    
    
    @NotNull(message = "Pet age is required")
    @Min(value = 0, message = "Pet age cannot be negative")
    @Max(value = 30, message = "Pet age cannot exceed 30 years")
    @Column(name = "pet_age", nullable = false)
    private Integer petAge;
    
    
    @NotNull(message = "Pet height is required")
    @DecimalMin(value = "0.1", message = "Pet height must be at least 0.1 cm")
    @DecimalMax(value = "300.0", message = "Pet height cannot exceed 300 cm")
    @Column(name = "pet_height", nullable = false, precision = 5, scale = 2)
    private BigDecimal petHeight; // in cm
    
    
    @NotNull(message = "Pet weight is required")
    @DecimalMin(value = "0.1", message = "Pet weight must be at least 0.1 kg")
    @DecimalMax(value = "500.0", message = "Pet weight cannot exceed 500 kg")
    @Column(name = "pet_weight", nullable = false, precision = 6, scale = 2)
    private BigDecimal petWeight; // in kg
    
    @NotBlank(message = "Pet species is required")
    @Size(min = 3, max = 30, message = "Pet species must be between 3 and 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]{3,30}$", message = "Pet species must contain only letters and spaces")
    @Column(name = "pet_species", nullable = false, length = 30)
    private String petSpecies; // Dog, Cat, Bird, etc.
    
    
    @NotBlank(message = "Pet gender is required")
    @Pattern(regexp = "^(Male|Female)$", message = "Pet gender must be either 'Male' or 'Female'")
    @Column(name = "pet_gender", nullable = false, length = 6)
    private String petGender;
    
    
    @Size(max = 50, message = "Pet breed cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Pet breed must contain only letters and spaces")
    @Column(name = "pet_breed", length = 50)
    private String petBreed;
    
    
    @NotNull(message = "Vaccination status is required")
    @Column(name = "is_vaccinated", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVaccinated = false;
    
    
    @NotNull(message = "Neutered status is required")
    @Column(name = "is_neutered", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isNeutered = false;
    
    
    @Size(max = 500, message = "Medical notes cannot exceed 500 characters")
    @Column(name = "medical_notes", length = 500)
    private String medicalNotes;
    
    @Column(name = "created_at") 
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
//    d
    // Automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public String getOwnerFullName() {
        return owner != null ? owner.getFirstName() + " " + owner.getLastName() : "Unknown Owner";
    }
    
 
    
    public String getPetInfo() {
        return petName + " (" + petSpecies + " - " + petBreed + ")";
    }
    
    public String getHealthStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Vaccinated: ").append(isVaccinated ? "Yes" : "No");
        status.append(", Neutered: ").append(isNeutered ? "Yes" : "No");
        return status.toString();
    }
    
    // Getters and Setters
    public Long getPid() {
        return pid;
    }
    
    public void setPid(Long pid) {
        this.pid = pid;
    }
    
    public UsersEntity getOwner() {
        return owner;
    }
    
    public void setOwner(UsersEntity owner) {
        this.owner = owner;
    }
    
    public DoctorsEntity getTreatingDoctor() {
        return treatingDoctor;
    }
    
    public void setTreatingDoctor(DoctorsEntity treatingDoctor) {
        this.treatingDoctor = treatingDoctor;
    }
    
    public String getPetName() {
        return petName;
    }
    
    public void setPetName(String petName) {
        this.petName = petName;
    }
    
    public Integer getPetAge() {
        return petAge;
    }
    
    public void setPetAge(Integer petAge) {
        this.petAge = petAge;
    }
    
    public BigDecimal getPetHeight() {
        return petHeight;
    }
    
    public void setPetHeight(BigDecimal petHeight) {
        this.petHeight = petHeight;
    }
    
    public BigDecimal getPetWeight() {
        return petWeight;
    }
    
    public void setPetWeight(BigDecimal petWeight) {
        this.petWeight = petWeight;
    }
    
    public String getPetSpecies() {
        return petSpecies;
    }
    
    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }
    
    public String getPetGender() {
        return petGender;
    }
    
    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
    
    public String getPetBreed() {
        return petBreed;
    }
    
    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }
    
    public Boolean getIsVaccinated() {
        return isVaccinated;
    }
    
    public void setIsVaccinated(Boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
    }
    
    public Boolean getIsNeutered() {
        return isNeutered;
    }
    
    public void setIsNeutered(Boolean isNeutered) {
        this.isNeutered = isNeutered;
    }
    
    public String getMedicalNotes() {
        return medicalNotes;
    }
    
    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
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
}
