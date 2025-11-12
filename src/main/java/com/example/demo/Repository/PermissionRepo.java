package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Permissions;

@Repository
public interface PermissionRepo extends JpaRepository<Permissions, Long> {

   Optional<Permissions> findByUid(UUID uid);

   List<Permissions> findByIsDeletedFalse();

   List<Permissions> findByUidIn(List<UUID> uid);
}
