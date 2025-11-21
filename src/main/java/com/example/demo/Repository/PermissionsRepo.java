package com.example.demo.Repository;
import com.example.demo.Entities.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionsRepo extends JpaRepository<Permissions, Long> {

    Optional<Permissions> findByUid(UUID uid);

    List<Permissions> findByIsDeletedFalse();
}