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
//import com.example.demo.Entities.BehaviouristKyc;
//
//@Repository
//public interface BehaviouristKycRepo extends JpaRepository<BehaviouristKyc, Long> {
//	
//	Optional<BehaviouristKyc> findByUid(UUID uid);
//	
//	Optional<BehaviouristKyc> findByEmail(String email);
//	
//	Optional<BehaviouristKyc> findByPhone(String phone);
//	
//	boolean existsByEmail(String email);
//	
//	boolean existsByPhone(String phone);
//}