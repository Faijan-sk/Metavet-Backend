package com.example.demo.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.PermissionDto;
import com.example.demo.Entities.Permissions;
import com.example.demo.Repository.PermissionRepo;

import jakarta.validation.ValidationException;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepo permissionRepository;

    // CREATE
    public Permissions create(PermissionDto dto) throws ValidationException {

        validateBeforeCreate(dto);

        Permissions entity = new Permissions();
        entity.setModuleName(dto.getModuleName());
        entity.setAction(dto.getAction());

        return permissionRepository.save(entity);
    }

    // UPDATE
    public Permissions update(UUID uid, PermissionDto dto) throws ValidationException {

        validateBeforeCreate(dto);

        Permissions existing = permissionRepository.findByUid(uid)
            .orElseThrow(() -> new RuntimeException("Permission not found with uid: " + uid));

        existing.setModuleName(dto.getModuleName());
        existing.setAction(dto.getAction());

        return permissionRepository.save(existing);
    }

    // GET ALL
    public List<PermissionDto> getAll() throws ValidationException {

        return permissionRepository.findAll().stream()
            .filter(p -> !p.isDeleted())
            .sorted(Comparator.comparing(Permissions::getId).reversed())
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // GET BY UID
    public PermissionDto getByUid(UUID uid) {

        Permissions entity = permissionRepository.findByUid(uid)
            .orElseThrow(() -> new RuntimeException("Permission not found with uid: " + uid));

        return convertToDto(entity);
    }

    // DELETE (soft)
    public void delete(UUID uid) {

        Permissions entity = permissionRepository.findByUid(uid)
            .orElseThrow(() -> new RuntimeException("Permission not found with uid: " + uid));

        entity.setDeleted(true);
        permissionRepository.save(entity);
    }

    // PRIVATE
    private PermissionDto convertToDto(Permissions entity) {

        PermissionDto dto = new PermissionDto();
        dto.setUid(entity.getUid());
        dto.setModuleName(entity.getModuleName());
        dto.setAction(entity.getAction());
        dto.setIsDeleted(entity.isDeleted());

        return dto;
    }

    private void validateBeforeCreate(PermissionDto dto) throws ValidationException {

        if (dto.getModuleName() == null || dto.getModuleName().trim().isEmpty()) {
            throw new ValidationException("Module name is required.");
        }

        if (dto.getAction() == null || dto.getAction().trim().isEmpty()) {
            throw new ValidationException("Action is required.");
        }
    }
}
