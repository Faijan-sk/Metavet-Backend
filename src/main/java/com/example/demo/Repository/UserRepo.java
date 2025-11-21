package com.example.demo.Repository;


import com.example.demo.Entities.UsersEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UsersEntity, Long> {

    // ============ EXISTING / STABLE METHODS ============

    // Check if email already exists
    boolean existsByEmail(String email);

    // Check if phone number already exists
    boolean existsByPhoneNumber(String phoneNumber);

    // Find user by email (return Optional)
    Optional<UsersEntity> findByEmail(String email);

    // Find user by phone number
    Optional<UsersEntity> findByPhoneNumber(String phoneNumber);

    // Find user by email or phone number (useful for login)
    Optional<UsersEntity> findByEmailOrPhoneNumber(String email, String phoneNumber);

    // Find user by UID (assumes BaseEntity.uid is UUID)
    Optional<UsersEntity> findByUid(UUID uid);

    // ============ NEW / UTILITY METHODS FOR APPOINTMENT & STATS ============

    List<UsersEntity> findByUserType(Integer userType);

    List<UsersEntity> findByUserTypeOrderByFirstNameAsc(Integer userType);

    long countByUserType(Integer userType);

    List<UsersEntity> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName);

    List<UsersEntity> findByOtpIsNotNull();

    List<UsersEntity> findByOtpIsNull();

    List<UsersEntity> findByCountryCode(String countryCode);

    List<UsersEntity> findByCountryCodeAndUserType(String countryCode, Integer userType);

    boolean existsByUid(UUID uid);

    List<UsersEntity> findByFirstName(String firstName);

    List<UsersEntity> findByLastName(String lastName);

    List<UsersEntity> findByFirstNameAndLastName(String firstName, String lastName);

    List<UsersEntity> findByPhoneNumberContaining(String phoneNumber);

    List<UsersEntity> findByEmailContaining(String emailDomain);

    @Query("SELECT u.userType, COUNT(u) FROM UsersEntity u GROUP BY u.userType")
    List<Object[]> getUserTypeStatistics();

    @Query(value = "SELECT * FROM users_entity ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<UsersEntity> findTopRecentUsers(@Param("limit") int limit);

    List<UsersEntity> findByUserTypeIn(List<Integer> userTypes);

    List<UsersEntity> findByUserTypeNot(Integer userType);

    @Query(value = "SELECT * FROM users_entity WHERE user_type = :userType LIMIT :limit", nativeQuery = true)
    List<UsersEntity> findByUserTypeWithLimit(@Param("userType") Integer userType, @Param("limit") int limit);

    // ============ ROLE / PERMISSION RELATED METHODS ============

}
