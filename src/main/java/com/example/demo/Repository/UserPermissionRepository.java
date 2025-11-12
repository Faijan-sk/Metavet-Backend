package com.example.demo.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.UserPermission;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    Optional<UserPermission> findByUid(UUID uid);

    // changed to match property 'roleName' in UserPermission entity
    Optional<UserPermission> findByRoleName(String roleName);
}
