package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.PermissionDto;
import com.example.demo.Entities.Permissions;
import com.example.demo.Exceptions.StandardResponse;
import com.example.demo.Exceptions.SuccessResponse;
import com.example.demo.Service.PermissionService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService service;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<StandardResponse> create(@Valid @RequestBody PermissionDto dto) 
            throws ValidationException {
        
        Permissions created = service.create(dto);
        
        StandardResponse sr = new SuccessResponse(201, "Permission created successfully.");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(sr);
    }

    // ✅ UPDATE
    @PutMapping("/{uid}")
    public ResponseEntity<StandardResponse> update(
            @PathVariable UUID uid, 
            @Valid @RequestBody PermissionDto dto) throws ValidationException {
        
        service.update(uid, dto);
        
        StandardResponse sr = new SuccessResponse(200, "Permission updated successfully.");
        
        return ResponseEntity.ok(sr);
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<StandardResponse> getAllPermissions() throws ValidationException {
        
        List<PermissionDto> permissions = service.getAll();
        
        Map<String, Object> res = new HashMap<>();
        res.put("count", permissions.size());
        res.put("result", permissions);
        
        StandardResponse sr = new SuccessResponse(200, res);
        
        return ResponseEntity.ok(sr);
    }
    
    // ✅ GET BY UID (Uncomment if needed)
    @GetMapping("/{uid}")
    public ResponseEntity<StandardResponse> getPermissionByUid(@PathVariable UUID uid) {
        
        PermissionDto permissionDto = service.getByUid(uid);
        
        StandardResponse sr = new SuccessResponse(200, permissionDto);
        
        return ResponseEntity.ok(sr);
    }
    
    // ✅ DELETE (Soft Delete)
    @DeleteMapping("/{uid}")
    public ResponseEntity<StandardResponse> delete(@PathVariable UUID uid) {
        
        service.delete(uid);
        
        StandardResponse sr = new SuccessResponse(200, "Permission deleted successfully.");
        
        return ResponseEntity.ok(sr);
    }
}