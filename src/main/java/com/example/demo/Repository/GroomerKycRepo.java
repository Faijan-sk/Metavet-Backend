package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.GroomerKyc;
import com.example.demo.Entities.GroomerKyc.ApplicationStatus;
import com.example.demo.Entities.GroomerKyc.ServiceLocationType;

@Repository
public interface GroomerKycRepo extends JpaRepository<GroomerKyc, Long> {
	
	
	// ---------- BaseEntity Related ----------
    Optional<GroomerKyc> findByUid(UUID uid);

    List<GroomerKyc> findAllByOrderByCreatedAtDesc();

    List<GroomerKyc> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ---------- Entity Specific ----------
    Optional<GroomerKyc> findByEmail(String email);

    boolean existsByEmail(String email);

    List<GroomerKyc> findByStatus(ApplicationStatus status);

    List<GroomerKyc> findByServiceLocationType(ServiceLocationType serviceLocationType);

    // Search by partial name or business name
    @Query("SELECT g FROM GroomerKyc g WHERE LOWER(g.fullLegalName) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(g.businessName) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<GroomerKyc> searchByNameOrBusiness(@Param("q") String query);

    // Recently added applications (latest first)
    @Query("SELECT g FROM GroomerKyc g ORDER BY g.createdAt DESC")
    List<GroomerKyc> findRecentApplications();

    // Applications by status (ordered by creation date)
    @Query("SELECT g FROM GroomerKyc g WHERE g.status = :status ORDER BY g.createdAt DESC")
    List<GroomerKyc> findByStatusOrderByCreatedAtDesc(@Param("status") ApplicationStatus status);


}
