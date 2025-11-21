package com.example.demo.Controller;



import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.PermissionRequestDto;
import com.example.demo.Dto.PermissionResponseDto;
import com.example.demo.Service.PermissionsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {

 private final PermissionsService permissionsService;

 public PermissionsController(PermissionsService permissionsService) {
     this.permissionsService = permissionsService;
 }

 // Create
 @PostMapping
 public ResponseEntity<PermissionResponseDto> createPermission(
         @Valid @RequestBody PermissionRequestDto request) {

     PermissionResponseDto created = permissionsService.createPermission(request);
     return ResponseEntity.status(201).body(created);
 }

 // Get all (not deleted)
 @GetMapping
 public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
     return ResponseEntity.ok(permissionsService.getAllPermissions());
 }

 // Get by uid
 @GetMapping("/{uid}")
 public ResponseEntity<PermissionResponseDto> getPermissionByUid(@PathVariable UUID uid) {
     return ResponseEntity.ok(permissionsService.getByUid(uid));
 }

 // Update
 @PutMapping("/{uid}")
 public ResponseEntity<PermissionResponseDto> updatePermission(
         @PathVariable UUID uid,
         @Valid @RequestBody PermissionRequestDto request) {

     return ResponseEntity.ok(permissionsService.updatePermission(uid, request));
 }

 // Soft delete
 @DeleteMapping("/{uid}")
 public ResponseEntity<String> deletePermission(@PathVariable UUID uid) {
     permissionsService.softDelete(uid);
     return ResponseEntity.ok("Permission deleted successfully (soft delete)");
 }
}
