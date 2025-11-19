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
import com.example.demo.Entities.PetBehavioristKycEntity;

@Repository
public interface PetBehavioristKycRepo extends JpaRepository<PetBehavioristKycEntity, Long> {

    // ---------- BaseEntity Related ----------
    Optional<PetBehavioristKycEntity> findByUid(UUID uid);

    List<PetBehavioristKycEntity> findAllByOrderByCreatedAtDesc();

    List<PetBehavioristKycEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ---------- Entity Specific ----------

    // Find by pet
    Optional<PetBehavioristKycEntity> findByPet(PetsEntity pet);

    List<PetBehavioristKycEntity> findByPetId(Long petId);

    // Find by petUid string
    Optional<PetBehavioristKycEntity> findByPetUid(String petUid);

    // Check if KYC exists for a pet
    boolean existsByPet(PetsEntity pet);

    boolean existsByPetId(Long petId);

    boolean existsByPetUid(String petUid);

    // ---------- KYC STATUS Queries ----------

    // Find by enum KycStatus
    List<PetBehavioristKycEntity> findByKycStatus(PetBehavioristKycEntity.KycStatus kycStatus);

    // Find by status ordered by creation time desc
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.kycStatus = :status ORDER BY b.createdAt DESC")
    List<PetBehavioristKycEntity> findByKycStatusOrdered(@Param("status") PetBehavioristKycEntity.KycStatus status);

    // ---------- Behavioral Challenge Queries ----------
    
    // Search by behavioral challenges (comma-separated field)
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behavioralChallenges) LIKE LOWER(CONCAT('%', :challenge, '%'))")
    List<PetBehavioristKycEntity> searchByBehavioralChallenge(@Param("challenge") String challenge);

    // Find KYCs with aggression concerns
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behavioralChallenges) LIKE LOWER('%aggression%')")
    List<PetBehavioristKycEntity> findKycsWithAggression();

    // Find KYCs with separation anxiety
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behavioralChallenges) LIKE LOWER('%separation anxiety%')")
    List<PetBehavioristKycEntity> findKycsWithSeparationAnxiety();

    // Find KYCs with bite history
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.aggressionBiteDescription IS NOT NULL AND b.aggressionBiteDescription != ''")
    List<PetBehavioristKycEntity> findKycsWithBiteHistory();

    // ---------- Behavior Progress & Frequency ----------
    
    // Find by behavior frequency
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behaviorFrequency) = LOWER(:frequency)")
    List<PetBehavioristKycEntity> findByBehaviorFrequency(@Param("frequency") String frequency);

    // Find by behavior progress
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behaviorProgress) = LOWER(:progress)")
    List<PetBehavioristKycEntity> findByBehaviorProgress(@Param("progress") String progress);

    // Find worsening behaviors
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.behaviorProgress) = 'worsened'")
    List<PetBehavioristKycEntity> findWorseningBehaviors();

    // ---------- Training History ----------
    
    // Find KYCs that worked with trainer
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.workedWithTrainer = true")
    List<PetBehavioristKycEntity> findKycsWithTrainerHistory();

    // Find KYCs without trainer history
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.workedWithTrainer = false OR b.workedWithTrainer IS NULL")
    List<PetBehavioristKycEntity> findKycsWithoutTrainerHistory();

    // Search by training tools (comma-separated field)
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.currentTrainingTools) LIKE LOWER(CONCAT('%', :tool, '%'))")
    List<PetBehavioristKycEntity> searchByTrainingTool(@Param("tool") String tool);

    // Find by pet motivation
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.petMotivation) = LOWER(:motivation)")
    List<PetBehavioristKycEntity> findByPetMotivation(@Param("motivation") String motivation);

    // ---------- Environment Queries ----------
    
    // Find KYCs with other pets
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.otherPets = true")
    List<PetBehavioristKycEntity> findKycsWithOtherPets();

    // Find KYCs with children
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.childrenInHome = true")
    List<PetBehavioristKycEntity> findKycsWithChildren();

    // Find by home environment
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.homeEnvironment) = LOWER(:environment)")
    List<PetBehavioristKycEntity> findByHomeEnvironment(@Param("environment") String environment);

    // ---------- Session Preferences ----------
    
    // Find by preferred session type
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.preferredSessionType) = LOWER(:sessionType)")
    List<PetBehavioristKycEntity> findByPreferredSessionType(@Param("sessionType") String sessionType);

    // Find virtual session preferences
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.preferredSessionType) = 'virtual' OR LOWER(b.preferredSessionType) = 'either is fine'")
    List<PetBehavioristKycEntity> findVirtualSessionPreferences();

    // Find in-person session preferences
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.preferredSessionType) = 'in-person' OR LOWER(b.preferredSessionType) = 'either is fine'")
    List<PetBehavioristKycEntity> findInPersonSessionPreferences();

    // ---------- Consent & Status ----------
    
    // Find by consent status
    List<PetBehavioristKycEntity> findByConsentAccuracy(Boolean consent);

    // Find KYCs without consent (for follow-up)
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.consentAccuracy = false OR b.consentAccuracy IS NULL")
    List<PetBehavioristKycEntity> findPendingConsent();

    // ---------- Recent Submissions ----------
    
    @Query("SELECT b FROM PetBehavioristKycEntity b ORDER BY b.createdAt DESC")
    List<PetBehavioristKycEntity> findRecentSubmissions();

    // ---------- Aggressive Behaviors ----------
    
    // Search by aggressive behaviors (comma-separated field)
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.aggressiveBehaviors) LIKE LOWER(CONCAT('%', :behavior, '%'))")
    List<PetBehavioristKycEntity> searchByAggressiveBehavior(@Param("behavior") String behavior);

    // Find KYCs with serious incidents
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE b.seriousIncidents IS NOT NULL AND b.seriousIncidents != ''")
    List<PetBehavioristKycEntity> findKycsWithSeriousIncidents();

    // ---------- Openness to Adjustments ----------
    
    @Query("SELECT b FROM PetBehavioristKycEntity b WHERE LOWER(b.openToAdjustments) = LOWER(:openness)")
    List<PetBehavioristKycEntity> findByOpennessToAdjustments(@Param("openness") String openness);
}
