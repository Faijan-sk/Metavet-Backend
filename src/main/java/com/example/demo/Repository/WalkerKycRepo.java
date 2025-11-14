package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.WalkerKyc;

@Repository
public interface WalkerKycRepo extends JpaRepository<WalkerKyc, Long> {

    // ---------- BaseEntity Related ----------
    Optional<WalkerKyc> findByUid(UUID uid);

    List<WalkerKyc> findAllByOrderByCreatedAtDesc();

    List<WalkerKyc> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // ---------- Entity Specific ----------
    Optional<WalkerKyc> findByEmail(String email);

    boolean existsByEmail(String email);

    List<WalkerKyc> findByStatus(String status);

    // Search by partial name or business name
    @Query("SELECT w FROM WalkerKyc w WHERE LOWER(w.fullLegalName) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(w.businessName) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<WalkerKyc> searchByNameOrBusiness(@Param("q") String query);

    // Recently added applications (latest first)
    @Query("SELECT w FROM WalkerKyc w ORDER BY w.createdAt DESC")
    List<WalkerKyc> findRecentApplications();

    // Applications by status (ordered by creation date)
    @Query("SELECT w FROM WalkerKyc w WHERE w.status = :status ORDER BY w.createdAt DESC")
    List<WalkerKyc> findByStatusOrderByCreatedAtDesc(@Param("status") String status);
}