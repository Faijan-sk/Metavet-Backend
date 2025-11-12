package com.example.demo.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Config.SpringSecurityAuditorAware;
import com.example.demo.Dto.UserPermissionDto;
import com.example.demo.Dto.UserRoleRequestDto;
import com.example.demo.Entities.Permissions;
import com.example.demo.Entities.UserPermission;
import com.example.demo.Entities.UsersEntity;
import com.example.demo.Repository.PermissionRepo;
import com.example.demo.Repository.UserPermissionRepository;
import com.example.demo.Repository.UserRepo;

import jakarta.validation.ValidationException;

@Service
public class UserPermissionsService {

    private static final Logger log = LoggerFactory.getLogger(UserPermissionsService.class);

    private final PermissionRepo perRepo;
    private final UserPermissionRepository repo;
    private final UserRepo userRepository;
    private final SpringSecurityAuditorAware auditorAware;

    public UserPermissionsService(PermissionRepo perRepo,
                                  UserPermissionRepository repo,
                                  UserRepo userRepository,
                                  SpringSecurityAuditorAware auditorAware) {
        this.perRepo = perRepo;
        this.repo = repo;
        this.userRepository = userRepository;
        this.auditorAware = auditorAware;
    }

    // ================= CREATE =================
    @Transactional
    public UserPermission create(UserRoleRequestDto dto) throws ValidationException {
        UsersEntity user = auditorAware.getCurrentAuditor().orElse(null);

        validateBeforeCreate(dto);

        String roleName = dto.getRoleName().trim().toLowerCase();

        // ✅ FIX: Replaced lambda with normal if condition to avoid "effectively final" issue
        if (repo.findByRoleName(roleName).isPresent()) {
            throw new ValidationException("Role '" + roleName + "' already exists.");
        }

        // fetch permissions by UIDs
        List<Permissions> permissions = perRepo.findByUidIn(dto.getPermissionIds());
        if (permissions.size() != dto.getPermissionIds().size()) {
            throw new ValidationException("Some permissions not found for provided IDs.");
        }

        UserPermission userPermissions = new UserPermission();
        userPermissions.setRoleName(roleName);
        userPermissions.setPermissionsList(permissions);
        // optional: track creator if needed
        // userPermissions.setCreatedBy(user != null ? user.getId() : null);

        UserPermission created = repo.save(userPermissions);
        log.debug("Created UserPermission uid={} roleName={}", created.getUid(), roleName);
        return created;
    }

    // ================= UPDATE =================
    @Transactional
    public UserPermission update(UUID uid, UserRoleRequestDto dto) throws ValidationException {

        validateBeforeCreate(dto);

        UserPermission userPermissions = repo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("UserRole not found with UID: " + uid));

        String roleName = dto.getRoleName();
        if (roleName != null) {
            roleName = roleName.trim().toLowerCase();

            if (!isValidRoleName(roleName)) {
                throw new ValidationException("Role name should only contain alphabetic characters and spaces.");
            }

            // ✅ FIX: replaced lambda with if block to avoid final variable issue
            var existing = repo.findByRoleName(roleName);
            if (existing.isPresent() && !existing.get().getUid().equals(uid)) {
                throw new ValidationException("Role '" + roleName + "' already exists.");
            }

            userPermissions.setRoleName(roleName);
        }

        if (dto.getPermissionIds() != null) {
            List<Permissions> permissions = perRepo.findByUidIn(dto.getPermissionIds());
            if (permissions.size() != dto.getPermissionIds().size()) {
                throw new ValidationException("Some permissions not found for provided IDs.");
            }
            userPermissions.setPermissionsList(permissions);
        }

        UserPermission saved = repo.save(userPermissions);
        log.debug("Updated UserPermission uid={}", saved.getUid());
        return saved;
    }

    // ================= GET ALL =================
    public List<UserPermissionDto> getAll() throws ValidationException {

        List<UserPermission> allUserPermissions = repo.findAll().stream()
                // return only non-deleted by default
                .filter(up -> up.getIsDelete() == null || !up.getIsDelete())
                .sorted(Comparator.comparing(UserPermission::getId).reversed())
                .collect(Collectors.toList());

        return allUserPermissions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ================= CONVERT TO DTO =================
    private UserPermissionDto convertToDto(UserPermission entity) {
        UserPermissionDto dto = new UserPermissionDto();

        dto.setRoleName(entity.getRoleName());

        List<UUID> permUids = entity.getPermission().stream()
                .map(Permissions::getUid)
                .collect(Collectors.toList());

        dto.setPermissionIds(permUids);
        dto.setIsDelete(entity.getIsDelete());

        return dto;
    }

    // ================= RETRIEVE =================
    public UserPermissionDto retrieve(UUID uid) throws ValidationException {

        UserPermission userPermissions = repo.findByUid(uid).orElse(null);

        if (userPermissions == null) {
            return null;
        }

        return convertToDto(userPermissions);
    }

    // ================= DELETE (soft toggle) =================
    @Transactional
    public void deleteUserPermissions(UUID uid) throws ValidationException {

        UserPermission userPermissions = repo.findByUid(uid)
                .orElseThrow(() -> new ValidationException("UserRole not found with UID: " + uid));

        boolean isPermissionsInUse = false;
        try {
            isPermissionsInUse = userRepository.existsByUserRole(userPermissions);
        } catch (Exception ex) {
            log.warn("existsByUserRole(UserPermission) failed, trying existsByUserRoleUid fallback", ex);
            try {
                isPermissionsInUse = userRepository.existsByUserRoleUid(uid);
            } catch (Exception ex2) {
                log.warn("Fallback existsByUserRoleUid not available; proceeding safely", ex2);
            }
        }

        if (isPermissionsInUse) {
            throw new ValidationException("Role cannot be deleted because it is assigned to users.");
        }

        userPermissions.setIsDelete(userPermissions.getIsDelete() == null ? Boolean.TRUE : !userPermissions.getIsDelete());
        repo.save(userPermissions);

        log.debug("Toggled isDelete for UserPermission uid={} nowIsDeleted={}", uid, userPermissions.getIsDelete());
    }

    // ================= HELPERS =================
    private boolean isValidRoleName(String roleName) {
        return roleName != null && roleName.matches("^[a-zA-Z ]+$");
    }

    private void validateBeforeCreate(UserRoleRequestDto dto) throws ValidationException {

        if (dto.getRoleName() == null || dto.getRoleName().trim().isEmpty()) {
            throw new ValidationException("Role name is required.");
        }

        if (dto.getPermissionIds() == null || dto.getPermissionIds().isEmpty()) {
            throw new ValidationException("Please select Permissions.");
        }

        if (!dto.getRoleName().matches("^[a-zA-Z ]+$")) {
            throw new ValidationException("Role name should only contain alphabetic characters.");
        }
    }
}
