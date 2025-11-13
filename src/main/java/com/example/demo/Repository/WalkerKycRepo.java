//package com.example.demo.Repository;
//
//
//
//import java.util.Optional;
//import java.util.UUID;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.example.demo.Entities.WalkerKyc;
//
//@Repository
//public interface WalkerKycRepo extends JpaRepository<WalkerKyc, Long> {
//	
//	Optional<WalkerKyc> findByUid(UUID uid);
//	
//	Optional<WalkerKyc> findByEmail(String email);
//	
//	Optional<WalkerKyc> findByPhone(String phone);
//	
//	boolean existsByEmail(String email);
//	
//	boolean existsByPhone(String phone);
//}
