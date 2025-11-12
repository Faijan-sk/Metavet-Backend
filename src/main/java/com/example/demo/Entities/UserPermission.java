package com.example.demo.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "role_permission")
public class UserPermission extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "role_permission_mapping",
        joinColumns = @JoinColumn(name = "role_uid", referencedColumnName = "uid"),
        inverseJoinColumns = @JoinColumn(name = "permission_uid", referencedColumnName = "uid")
    )
    private List<Permissions> permissions = new ArrayList<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public List<Map<String, String>> getPermissions() {
        List<Map<String, String>> list = new ArrayList<>();
        if (this.permissions == null) {
            return list;
        }

        for (Permissions permission : this.permissions) {
            if (permission == null) continue;
            Map<String, String> map = new HashMap<>();
            String module = permission.getModuleName();
            String action = permission.getAction();

            map.put("module", module != null ? module.toLowerCase() : null);
            map.put("action", action != null ? action.toLowerCase() : null);
            list.add(map);
        }
        return list;
    }

    // getters / setters

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Permissions> getPermissionsList() {
        return permissions;
    }

    public void setPermissionsList(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    // backward-compatible accessors (if older code expects getPermission())
    public List<Permissions> getPermission() {
        return permissions;
    }

    public void setPermission(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Boolean getIsDelete() {
        return isDeleted;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDeleted = isDelete;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
