package com.example.demo.Repository;

import com.example.demo.Entities.AdminsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<AdminsEntity, Long> {
	
    Optional<AdminsEntity> findByUsername(String username);
    
    Optional<AdminsEntity> findByEmail(String email);
    
   
    
    @Query("SELECT a FROM AdminsEntity a WHERE a.username = :usernameOrEmail OR a.email = :usernameOrEmail")
    Optional<AdminsEntity> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    // Role ke basis pe admins find karna (ab Integer role)
    @Query("SELECT a FROM AdminsEntity a WHERE a.role = :role")
    List<AdminsEntity> findByRole(@Param("role") Integer role);
     
   
    // Find all active admins (if you have an active field)
    // List<AdminsEntity> findByActiveTrue();
    
    // Custom query to find admins with specific roles
    @Query("SELECT a FROM AdminsEntity a WHERE a.role IN :roles")
    List<AdminsEntity> findByRoles(@Param("roles") List<Integer> roles);
}


