package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.UsersEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetRepo extends JpaRepository<PetsEntity, Long> {
    
    // Find by UUID (from BaseEntity)
    Optional<PetsEntity> findByUid(UUID uid);
    
    // Find all pets by owner
    List<PetsEntity> findByOwner(UsersEntity owner);
    
    // Find all pets by owner ID
    List<PetsEntity> findByOwner_Id(Long ownerId);
    
    // Find all pets by owner UUID
    List<PetsEntity> findByOwner_Uid(UUID ownerUid);
    
    // Find all pets by treating doctor
    List<PetsEntity> findByTreatingDoctor(DoctorsEntity doctor);
    
    // Find all pets by doctor ID
    List<PetsEntity> findByTreatingDoctor_Id(Long doctorId);
    
    // Find pets by species
    List<PetsEntity> findByPetSpeciesIgnoreCase(String species);
    
    // Find pets by breed
    List<PetsEntity> findByPetBreedIgnoreCase(String breed);
    
    // Find pets by gender
    List<PetsEntity> findByPetGender(String gender);
    
    // Find vaccinated pets
    List<PetsEntity> findByIsVaccinated(Boolean isVaccinated);
    
    // Find neutered pets
    List<PetsEntity> findByIsNeutered(Boolean isNeutered);
    
    // Find pets by species and breed
    List<PetsEntity> findByPetSpeciesIgnoreCaseAndPetBreedIgnoreCase(String species, String breed);
    
    // Find pets without assigned doctor
    List<PetsEntity> findByTreatingDoctorIsNull();
    
    // Find pets with assigned doctor
    List<PetsEntity> findByTreatingDoctorIsNotNull();
    
    // Custom query to find pets by owner email
    @Query("SELECT p FROM PetsEntity p WHERE p.owner.email = :email")
    List<PetsEntity> findByOwnerEmail(@Param("email") String email);
    
    // Custom query to find pets by doctor specialization
    @Query("SELECT p FROM PetsEntity p WHERE p.treatingDoctor.specialization = :specialization")
    List<PetsEntity> findByDoctorSpecialization(@Param("specialization") String specialization);
    
    // Custom query to count pets by doctor ID
    @Query("SELECT COUNT(p) FROM PetsEntity p WHERE p.treatingDoctor.id = :doctorId")
    Long countPetsByDoctorId(@Param("doctorId") Long doctorId);
    
    // Custom query to find pets with health issues (not vaccinated or not neutered)
    @Query("SELECT p FROM PetsEntity p WHERE p.isVaccinated = false OR p.isNeutered = false")
    List<PetsEntity> findPetsWithHealthConcerns();
    
    // Custom query to find pets by multiple criteria
    @Query("SELECT p FROM PetsEntity p WHERE " +
           "(:species IS NULL OR LOWER(p.petSpecies) = LOWER(:species)) AND " +
           "(:breed IS NULL OR LOWER(p.petBreed) = LOWER(:breed)) AND " +
           "(:gender IS NULL OR p.petGender = :gender) AND " +
           "(:minAge IS NULL OR p.petAge >= :minAge) AND " +
           "(:maxAge IS NULL OR p.petAge <= :maxAge)")
    List<PetsEntity> findPetsByCriteria(@Param("species") String species,
                                       @Param("breed") String breed,
                                       @Param("gender") String gender,
                                       @Param("minAge") Integer minAge,
                                       @Param("maxAge") Integer maxAge);
    
    // Check if pet name exists for a specific owner (using owner ID)
    boolean existsByPetNameAndOwner_Id(String petName, Long ownerId);
    
    // CHECKED / FIXED: Check if pet name exists for a specific owner (using owner UUID)
    // NOTE: Spring Data derived query method must include BOTH properties used in the method name
    boolean existsByPetNameAndOwner_Uid(String petName, UUID ownerUid);
    
    // Find pet by ID and owner ID (for security - ensure owner can only access their pets)
    Optional<PetsEntity> findByIdAndOwner_Id(Long id, Long ownerId);
    
    // Find pet by UUID and owner UUID
    Optional<PetsEntity> findByUidAndOwner_Uid(UUID petUid, UUID ownerUid);
    
    // Find pet by ID and doctor ID (for doctors to access their assigned pets)
    Optional<PetsEntity> findByIdAndTreatingDoctor_Id(Long id, Long doctorId);
    
    //delete pet by uid 
    void deleteByUid(UUID uid);

    
    // Check if pet exists by UUID
    boolean existsByUid(UUID uid);
    
    
}
