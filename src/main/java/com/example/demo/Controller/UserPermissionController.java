package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.UserPermissionDto;
import com.example.demo.Dto.UserRoleRequestDto;
import com.example.demo.Entities.UserPermission;
import com.example.demo.Exceptions.StandardResponse;
import com.example.demo.Exceptions.SuccessResponse;
import com.example.demo.Service.UserPermissionsService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/userpermission")
public class UserPermissionController {

    private static final Logger log = LoggerFactory.getLogger(UserPermissionController.class);

    private final UserPermissionsService service;

    public UserPermissionController(UserPermissionsService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping(path = "/")
    public ResponseEntity<StandardResponse> createUserRoleOrPermission(@Valid @RequestBody UserRoleRequestDto dto)
            throws ValidationException {
        log.debug("Create UserRole request received: roleName={}, permissionCount={}",
                dto.getRoleName(), dto.getPermissionIds() == null ? 0 : dto.getPermissionIds().size());

        UserPermission userPermission = service.create(dto);

        SuccessResponse res = new SuccessResponse(201, "UserRole was created with the permissions",
                userPermission.getUid().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // UPDATE
    @PutMapping("/{uid}")
    public ResponseEntity<StandardResponse> updatePermissions(@PathVariable UUID uid,
            @Valid @RequestBody UserRoleRequestDto dto) throws ValidationException {
        log.debug("Update UserRole request received: uid={}, roleName={}", uid, dto.getRoleName());

        UserPermission updateUserRole = service.update(uid, dto);

        SuccessResponse res = new SuccessResponse(200, "UserRole was successfully updated with the permissions");

        return ResponseEntity.ok(res);
    }

    // RETRIEVE by UID
    @GetMapping("/{uid}")
    public ResponseEntity<StandardResponse> getUserPermissionByUid(@PathVariable UUID uid)
            throws ValidationException {
        log.debug("Retrieve UserRole by uid: {}", uid);

        UserPermissionDto userPermissionDto = service.retrieve(uid);

        if (userPermissionDto == null) {
            SuccessResponse res = new SuccessResponse(404,
                    "User role and permissions with UUID " + uid + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
        return ResponseEntity.ok(new SuccessResponse(200, userPermissionDto));
    }

    // DELETE (toggle)
    @DeleteMapping(path = "/{uid}")
    public ResponseEntity<StandardResponse> deleteUserPermission(@PathVariable UUID uid) throws ValidationException {
        log.debug("Delete (toggle) UserRole request received for uid: {}", uid);

        service.deleteUserPermissions(uid);

        SuccessResponse res = new SuccessResponse(200, "UserRole delete toggle successful");
        return ResponseEntity.ok(res);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<StandardResponse> getUserPermissions() throws ValidationException {
        log.debug("Get all UserRoles request received.");

        Map<String, Object> res = new HashMap<>();

        List<UserPermissionDto> userPermissions = service.getAll();

        res.put("count", userPermissions.size());
        res.put("result", userPermissions);

        StandardResponse sr = new SuccessResponse(200, res);
        return ResponseEntity.ok(sr);
    }
}
