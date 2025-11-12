package com.example.demo.Dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Pattern;

public class UserRoleRequestDto {

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "RoleName only contain alphabetic characters and space.")
    private String roleName;

    private List<UUID> permissionIds;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UUID> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<UUID> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
