package com.example.demo.Service;


import com.example.demo.Dto.PermissionRequestDto;
import com.example.demo.Dto.PermissionResponseDto;
import com.example.demo.Entities.Permissions;
import com.example.demo.Repository.PermissionsRepo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionsService {

 private final PermissionsRepo permissionsRepository;

 public PermissionsService(PermissionsRepo permissionsRepository) {
     this.permissionsRepository = permissionsRepository;
 }

 // Create
 public PermissionResponseDto createPermission(PermissionRequestDto request) {
     Permissions permission = new Permissions();
     permission.setModuleName(request.getModuleName());
     permission.setAction(request.getAction());
     permission.setDeleted(false);

     Permissions saved = permissionsRepository.save(permission);
     return mapToResponse(saved);
 }

 // Get all (not deleted)
 public List<PermissionResponseDto> getAllPermissions() {
     return permissionsRepository.findByIsDeletedFalse()
             .stream()
             .map(this::mapToResponse)
             .collect(Collectors.toList());
 }

 // Get by uid
 public PermissionResponseDto getByUid(UUID uid) {
     Permissions permission = permissionsRepository.findByUid(uid)
             .filter(p -> !p.isDeleted())
             .orElseThrow(() -> new RuntimeException("Permission not found for uid: " + uid));

     return mapToResponse(permission);
 }

 // Update by uid
 public PermissionResponseDto updatePermission(UUID uid, PermissionRequestDto request) {
     Permissions permission = permissionsRepository.findByUid(uid)
             .filter(p -> !p.isDeleted())
             .orElseThrow(() -> new RuntimeException("Permission not found for uid: " + uid));

     permission.setModuleName(request.getModuleName());
     permission.setAction(request.getAction());

     Permissions updated = permissionsRepository.save(permission);
     return mapToResponse(updated);
 }

 // Soft delete
 public void softDelete(UUID uid) {
     Permissions permission = permissionsRepository.findByUid(uid)
             .filter(p -> !p.isDeleted())
             .orElseThrow(() -> new RuntimeException("Permission not found for uid: " + uid));

     permission.setDeleted(true);
     permissionsRepository.save(permission);
 }

 // Mapping helper
 private PermissionResponseDto mapToResponse(Permissions permission) {
     PermissionResponseDto res = new PermissionResponseDto();
     res.setUid(permission.getUid());
     res.setModuleName(permission.getModuleName());
     res.setAction(permission.getAction());
     res.setDeleted(permission.isDeleted());
     res.setCreatedAt(permission.getCreatedAt());
     res.setUpdatedAt(permission.getUpdatedAt());
     return res;
 }
}
