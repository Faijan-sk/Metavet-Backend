package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entities.PetsEntity;
import com.example.demo.Service.PetsService;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {
    
    @Autowired
    private PetsService petService;
    
    // Create a new pet
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPet(@Valid @RequestBody PetsEntity pet, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check for validation errors
            if (result.hasErrors()) {
                response.put("status", "error");
                response.put("message", "Validation failed");
                response.put("errors", result.getAllErrors());
                return ResponseEntity.badRequest().body(response);
            }
            
            PetsEntity createdPet = petService.createPet(pet);
            
            response.put("status", "success");
            response.put("message", "Pet created successfully");
            response.put("data", createdPet);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Get all pets
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllPets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getAllPets();
            
            response.put("status", "success");
            response.put("message", "Pets retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pet by ID
    @GetMapping("/{petId}")
    public ResponseEntity<Map<String, Object>> getPetById(@PathVariable Long petId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<PetsEntity> pet = petService.getPetById(petId);
            
            if (pet.isPresent()) {
                response.put("status", "success");
                response.put("message", "Pet found");
                response.put("data", pet.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Pet not found");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pets by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Map<String, Object>> getPetsByOwner(@PathVariable Long ownerId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getPetsByOwnerId(ownerId);
            
            response.put("status", "success");
            response.put("message", "Pets retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pets by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getPetsByDoctor(@PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getPetsByDoctorId(doctorId);
            
            response.put("status", "success");
            response.put("message", "Pets retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Update pet information
    @PutMapping("/update/{petId}")
    public ResponseEntity<Map<String, Object>> updatePet(@PathVariable Long petId, 
                                                        @Valid @RequestBody PetsEntity updatedPet, 
                                                        BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check for validation errors
            if (result.hasErrors()) {
                response.put("status", "error");
                response.put("message", "Validation failed");
                response.put("errors", result.getAllErrors());
                return ResponseEntity.badRequest().body(response);
            }
            
            PetsEntity pet = petService.updatePet(petId, updatedPet);
            
            response.put("status", "success");
            response.put("message", "Pet updated successfully");
            response.put("data", pet);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Assign doctor to pet
    @PutMapping("/{petId}/assign-doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> assignDoctorToPet(@PathVariable Long petId, 
                                                                @PathVariable Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            PetsEntity pet = petService.assignDoctorToPet(petId, doctorId);
            
            response.put("status", "success");
            response.put("message", "Doctor assigned successfully");
            response.put("data", pet);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Remove doctor from pet
    @PutMapping("/{petId}/remove-doctor")
    public ResponseEntity<Map<String, Object>> removeDoctorFromPet(@PathVariable Long petId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            PetsEntity pet = petService.removeDoctorFromPet(petId);
            
            response.put("status", "success");
            response.put("message", "Doctor removed successfully");
            response.put("data", pet);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Delete pet
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<Map<String, Object>> deletePet(@PathVariable Long petId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = petService.deletePet(petId);
            
            if (deleted) {
                response.put("status", "success");
                response.put("message", "Pet deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "Failed to delete pet");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Search pets by criteria
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPets(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.searchPets(species, breed, gender, minAge, maxAge);
            
            response.put("status", "success");
            response.put("message", "Search completed successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pets by species
    @GetMapping("/species/{species}")
    public ResponseEntity<Map<String, Object>> getPetsBySpecies(@PathVariable String species) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PetsEntity> pets = petService.getPetsBySpecies(species);
            
            response.put("status", "success");
            response.put("message", "Pets retrieved by species successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pets by breed
    @GetMapping("/breed/{breed}")
    public ResponseEntity<Map<String, Object>> getPetsByBreed(@PathVariable String breed) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getPetsByBreed(breed);
            
            response.put("status", "success");
            response.put("message", "Pets retrieved by breed successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
   
    
    // Get vaccinated pets
    @GetMapping("/vaccinated")
    public ResponseEntity<Map<String, Object>> getVaccinatedPets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getVaccinatedPets();
            
            response.put("status", "success");
            response.put("message", "Vaccinated pets retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get non-vaccinated pets
    @GetMapping("/non-vaccinated")
    public ResponseEntity<Map<String, Object>> getNonVaccinatedPets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getNonVaccinatedPets();
            
            response.put("status", "success");
            response.put("message", "Non-vaccinated pets retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get pets without assigned doctor
    @GetMapping("/without-doctor")
    public ResponseEntity<Map<String, Object>> getPetsWithoutDoctor() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PetsEntity> pets = petService.getPetsWithoutDoctor();
            
            response.put("status", "success");
            response.put("message", "Pets without doctor retrieved successfully");
            response.put("data", pets);
            response.put("count", pets.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
   
    // Count pets by doctor
    @GetMapping("/count/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> countPetsByDoctor(@PathVariable Long doctorId) {
    	
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long count = petService.countPetsByDoctor(doctorId);
            
            response.put("status", "success");
            response.put("message", "Pet count retrieved successfully");
            response.put("count", count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    
    
   
    // Get dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Total pets
            List<PetsEntity> allPets = petService.getAllPets();
            stats.put("totalPets", allPets.size());
            
            // Vaccinated vs Non-vaccinated
            List<PetsEntity> vaccinatedPets = petService.getVaccinatedPets();
            List<PetsEntity> nonVaccinatedPets = petService.getNonVaccinatedPets();
            stats.put("vaccinatedPets", vaccinatedPets.size());
            stats.put("nonVaccinatedPets", nonVaccinatedPets.size());
            
            // Pets without doctor
            List<PetsEntity> petsWithoutDoctor = petService.getPetsWithoutDoctor();
            stats.put("petsWithoutDoctor", petsWithoutDoctor.size());
            
            
            
            // Species distribution
            Map<String, Long> speciesCount = allPets.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                		PetsEntity::getPetSpecies, 
                    java.util.stream.Collectors.counting()
                ));
            stats.put("speciesDistribution", speciesCount);
            response.put("status", "success");
            response.put("message", "Dashboard statistics retrieved successfully");
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}