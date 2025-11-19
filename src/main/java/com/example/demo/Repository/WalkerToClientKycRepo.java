package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.PetsEntity;
import com.example.demo.Entities.WalkerToClientKycEntity;

@Repository
public interface WalkerToClientKycRepo extends JpaRepository<WalkerToClientKycEntity, Long> {

    // ---------- BaseEntity Related ----------
    Optional<WalkerToClientKycEntity> findByUid(UUID uid);

    List<WalkerToClientKycEntity> findAllByOrderByCreatedAtDesc();

    List<WalkerToClientKycEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ---------- Entity Specific ----------

    // Find by pet
    Optional<WalkerToClientKycEntity> findByPet(PetsEntity pet);

    List<WalkerToClientKycEntity> findByPetId(Long petId);

    // Find by petUid string
    Optional<WalkerToClientKycEntity> findByPetUid(String petUid);

    // Check if KYC exists for a pet
    boolean existsByPet(PetsEntity pet);

    boolean existsByPetId(Long petId);

    boolean existsByPetUid(String petUid);

    // Find by pet names (partial match)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.petNames) LIKE LOWER(CONCAT('%', :petName, '%'))")
    List<WalkerToClientKycEntity> searchByPetName(@Param("petName") String petName);

    // Recently submitted KYCs
    @Query("SELECT w FROM WalkerToClientKycEntity w ORDER BY w.createdAt DESC")
    List<WalkerToClientKycEntity> findRecentSubmissions();

    // Find by consent status
    List<WalkerToClientKycEntity> findByConsent(Boolean consent);

    // Find KYCs without consent (for follow-up)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE w.consent = false OR w.consent IS NULL")
    List<WalkerToClientKycEntity> findPendingConsent();

    // Find by signature name
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.signature) LIKE LOWER(CONCAT('%', :signature, '%'))")
    List<WalkerToClientKycEntity> searchBySignature(@Param("signature") String signature);

    // ---------- NEW: Status Related Queries ----------
    
    // Find by status
    List<WalkerToClientKycEntity> findByStatus(WalkerToClientKycEntity.KycStatus status);
    
    // Check if KYC exists by UID
    boolean existsByUid(UUID uid);

    // ---------- Search queries for comma-separated fields ----------
    
    // Search by leash behavior (comma-separated field)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.leashBehavior) LIKE LOWER(CONCAT('%', :behavior, '%'))")
    List<WalkerToClientKycEntity> searchByLeashBehavior(@Param("behavior") String behavior);

    // Search by handling notes (comma-separated field)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.handlingNotes) LIKE LOWER(CONCAT('%', :note, '%'))")
    List<WalkerToClientKycEntity> searchByHandlingNotes(@Param("note") String note);

    // Search by post walk preferences (comma-separated field)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.postWalkPreferences) LIKE LOWER(CONCAT('%', :preference, '%'))")
    List<WalkerToClientKycEntity> searchByPostWalkPreferences(@Param("preference") String preference);

    // Search by additional services (comma-separated field)
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.additionalServices) LIKE LOWER(CONCAT('%', :service, '%'))")
    List<WalkerToClientKycEntity> searchByAdditionalServices(@Param("service") String service);

    // Find by energy level
    List<WalkerToClientKycEntity> findByEnergyLevel(WalkerToClientKycEntity.EnergyLevel energyLevel);

    // Find by walking experience
    List<WalkerToClientKycEntity> findByWalkingExperience(WalkerToClientKycEntity.WalkingExperience walkingExperience);

    // Find by preferred walk type
    List<WalkerToClientKycEntity> findByPreferredWalkType(WalkerToClientKycEntity.PreferredWalkType preferredWalkType);

    // Find by social compatibility
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.socialCompatibility) = LOWER(:compatibility)")
    List<WalkerToClientKycEntity> findBySocialCompatibility(@Param("compatibility") String compatibility);

    // Find KYCs with medical conditions
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE w.medicalConditions = true")
    List<WalkerToClientKycEntity> findKycsWithMedicalConditions();

    // Find KYCs with medications
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE w.medications = true")
    List<WalkerToClientKycEntity> findKycsWithMedications();

    // Find by preferred time of day
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.preferredTimeOfDay) = LOWER(:timeOfDay)")
    List<WalkerToClientKycEntity> findByPreferredTimeOfDay(@Param("timeOfDay") String timeOfDay);

    // Find by frequency
    @Query("SELECT w FROM WalkerToClientKycEntity w WHERE LOWER(w.frequency) = LOWER(:frequency)")
    List<WalkerToClientKycEntity> findByFrequency(@Param("frequency") String frequency);
}