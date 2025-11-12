package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Config.SpringSecurityAuditorAware;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.DoctorRepo;
import com.example.demo.Repository.PetRepo;
import com.example.demo.Repository.UserRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PetsService {
    
    @Autowired
    private PetRepo petRepository;
    
    @Autowired
    private UserRepo usersRepository;
    
    @Autowired
    private DoctorRepo doctorsRepository;
    
    @Autowired
    private SpringSecurityAuditorAware auditorAware;
    
    // Create a new pet
    public PetsEntity createPet(PetsEntity pet) {
        try {
            UsersEntity owner = auditorAware.getCurrentAuditor().orElse(null);
            System.out.println("owner => " + owner.getFirstName());
            pet.setOwner(owner);
            
            // Validate doctor if provided
            if (pet.getTreatingDoctor() != null && pet.getTreatingDoctor().getId() != null) {
                DoctorsEntity doctor = doctorsRepository.findById(pet.getTreatingDoctor().getId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
                pet.setTreatingDoctor(doctor);
            }
            
            // Check if pet name already exists for this owner (using owner ID)
            if (petRepository.existsByPetNameAndOwner_Id(pet.getPetName(), owner.getId())) {
                throw new RuntimeException("Pet with this name already exists for this owner");
            }
            
            return petRepository.save(pet);
            
        } catch (Exception e) {
            throw new RuntimeException("Error creating pet: " + e.getMessage());
        }
    }
    
    // Get all pets
    public List<PetsEntity> getAllPets() {
        return petRepository.findAll();
    }
    
    // Get pet by ID
    public Optional<PetsEntity> getPetById(Long petId) {
        return petRepository.findById(petId);
    }
    
    // Get pet by UUID
    public Optional<PetsEntity> getPetByUid(UUID petUid) {
        return petRepository.findByUid(petUid);
    }
    
    // Get pets by owner ID
    public List<PetsEntity> getPetsByOwnerId() {
        try {
            // Get current logged-in user from security context
            UsersEntity loginUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("User not authenticated or not logged in"));
            
            System.out.println("🔍 Fetching pets for owner: " + loginUser.getFirstName() 
                + " " + loginUser.getLastName() + " (ID: " + loginUser.getId() + ")");
            
            // Validate user is a client (userType = 1)
            if (loginUser.getUserType() != 1) {
                throw new RuntimeException("Only clients can view their pets. Your user type: " 
                    + loginUser.getUserTypeAsString());
            }
            
            List<PetsEntity> pets = petRepository.findByOwner_Id(loginUser.getId());
            System.out.println("✅ Found " + pets.size() + " pets for user ID: " + loginUser.getId());
            
            return pets;
            
        } catch (Exception e) {
            System.out.println("❌ Error in getPetsByOwnerId: " + e.getMessage());
            throw new RuntimeException("Error fetching pets: " + e.getMessage());
        }
    }
    
    // Get pets by doctor ID
    public List<PetsEntity> getPetsByDoctorId(Long doctorId) {
        return petRepository.findByTreatingDoctor_Id(doctorId);
    }
    
    // Update pet information
    public PetsEntity updatePet(Long petId, PetsEntity updatedPet) {
        try {
            PetsEntity existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
            
            // Update fields
            existingPet.setPetName(updatedPet.getPetName());
            existingPet.setPetAge(updatedPet.getPetAge());
            existingPet.setPetHeight(updatedPet.getPetHeight());
            existingPet.setPetWeight(updatedPet.getPetWeight());
            existingPet.setPetSpecies(updatedPet.getPetSpecies());
            existingPet.setPetGender(updatedPet.getPetGender());
            existingPet.setPetBreed(updatedPet.getPetBreed());
            existingPet.setIsVaccinated(updatedPet.getIsVaccinated());
            existingPet.setIsNeutered(updatedPet.getIsNeutered());
            existingPet.setMedicalNotes(updatedPet.getMedicalNotes());
            
            return petRepository.save(existingPet);
            
        } catch (Exception e) {
            throw new RuntimeException("Error updating pet: " + e.getMessage());
        }
    }
    
    // Assign doctor to pet
    public PetsEntity assignDoctorToPet(Long petId, Long doctorId) {
        try {
            PetsEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
            
            DoctorsEntity doctor = doctorsRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
            
            pet.setTreatingDoctor(doctor);
            return petRepository.save(pet);
            
        } catch (Exception e) {
            throw new RuntimeException("Error assigning doctor to pet: " + e.getMessage());
        }
    }
    
    // Remove doctor from pet
    public PetsEntity removeDoctorFromPet(Long petId) {
        try {
            PetsEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
            
            pet.setTreatingDoctor(null);
            return petRepository.save(pet);
            
        } catch (Exception e) {
            throw new RuntimeException("Error removing doctor from pet: " + e.getMessage());
        }
    }
    
    // Delete pet
    public boolean deletePet(Long petId) {
        try {
            if (!petRepository.existsById(petId)) {
                throw new RuntimeException("Pet not found with ID: " + petId);
            }
            
            petRepository.deleteById(petId);
            return true;
            
        } catch (Exception e) {
            throw new RuntimeException("Error deleting pet: " + e.getMessage());
        }
    }
    
    // Search pets by criteria
    public List<PetsEntity> searchPets(String species, String breed, String gender, Integer minAge, Integer maxAge) {
        return petRepository.findPetsByCriteria(species, breed, gender, minAge, maxAge);
    }
    
    // Get pets by species
    public List<PetsEntity> getPetsBySpecies(String species) {
        return petRepository.findByPetSpeciesIgnoreCase(species);
    }
    
    // Get pets by breed
    public List<PetsEntity> getPetsByBreed(String breed) {
        return petRepository.findByPetBreedIgnoreCase(breed);
    }
    
    // Get vaccinated pets
    public List<PetsEntity> getVaccinatedPets() {
        return petRepository.findByIsVaccinated(true);
    }
    
    // Get non-vaccinated pets
    public List<PetsEntity> getNonVaccinatedPets() {
        return petRepository.findByIsVaccinated(false);
    }
    
    // Get pets without assigned doctor
    public List<PetsEntity> getPetsWithoutDoctor() {
        return petRepository.findByTreatingDoctorIsNull();
    }
    
    // Count pets by doctor
    public Long countPetsByDoctor(Long doctorId) {
        return petRepository.countPetsByDoctorId(doctorId);
    }
    
    // Get pet for specific doctor (security check)
    public Optional<PetsEntity> getPetByIdAndDoctor(Long petId, Long doctorId) {
        return petRepository.findByIdAndTreatingDoctor_Id(petId, doctorId);
    }
    
 // Add these methods to your PetsService class

 // Delete pet by UID
 public boolean deletePetByUid(UUID petUid) {
     try {
         // Check if pet exists
         PetsEntity pet = petRepository.findByUid(petUid)
             .orElseThrow(() -> new RuntimeException("Pet not found with UID: " + petUid));
         
         // Get current logged-in user
         UsersEntity currentUser = auditorAware.getCurrentAuditor()
             .orElseThrow(() -> new RuntimeException("User not authenticated"));
         
         // Verify that the pet belongs to the current user (security check)
         if (!pet.getOwner().getId().equals(currentUser.getId())) {
             throw new RuntimeException("You don't have permission to delete this pet");
         }
         
         petRepository.deleteByUid(petUid);
         System.out.println("✅ Pet deleted successfully with UID: " + petUid);
         return true;
         
     } catch (Exception e) {
         System.out.println("❌ Error deleting pet: " + e.getMessage());
         throw new RuntimeException("Error deleting pet: " + e.getMessage());
     }
 }

 // Update pet by UID
 public PetsEntity updatePetByUid(UUID petUid, PetsEntity updatedPet) {
     try {
         // Find existing pet by UID
         PetsEntity existingPet = petRepository.findByUid(petUid)
             .orElseThrow(() -> new RuntimeException("Pet not found with UID: " + petUid));
         
         // Get current logged-in user
         UsersEntity currentUser = auditorAware.getCurrentAuditor()
             .orElseThrow(() -> new RuntimeException("User not authenticated"));
         
         // Verify that the pet belongs to the current user (security check)
         if (!existingPet.getOwner().getId().equals(currentUser.getId())) {
             throw new RuntimeException("You don't have permission to update this pet");
         }
         
         // Update only the allowed fields
         existingPet.setPetName(updatedPet.getPetName());
         existingPet.setPetAge(updatedPet.getPetAge());
         existingPet.setPetHeight(updatedPet.getPetHeight());
         existingPet.setPetWeight(updatedPet.getPetWeight());
         existingPet.setPetSpecies(updatedPet.getPetSpecies());
         existingPet.setPetGender(updatedPet.getPetGender());
         existingPet.setPetBreed(updatedPet.getPetBreed());
         existingPet.setIsVaccinated(updatedPet.getIsVaccinated());
         existingPet.setIsNeutered(updatedPet.getIsNeutered());
         existingPet.setMedicalNotes(updatedPet.getMedicalNotes());
         
         // Validate and update doctor if provided
         if (updatedPet.getTreatingDoctor() != null && updatedPet.getTreatingDoctor().getId() != null) {
             DoctorsEntity doctor = doctorsRepository.findById(updatedPet.getTreatingDoctor().getId())
                 .orElseThrow(() -> new RuntimeException("Doctor not found"));
             existingPet.setTreatingDoctor(doctor);
         }
         
         PetsEntity savedPet = petRepository.save(existingPet);
         System.out.println("✅ Pet updated successfully with UID: " + petUid);
         
         return savedPet;
         
     } catch (Exception e) {
         System.out.println("❌ Error updating pet: " + e.getMessage());
         throw new RuntimeException("Error updating pet: " + e.getMessage());
     }
 }
 
 
}