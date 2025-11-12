package com.example.demo.Dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PermissionDto {

    private UUID uid;

    @NotBlank(message = "Module name is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Module name must contain only alphabets and spaces.")
    private String moduleName;

    @NotBlank(message = "Action is required")
    private String action;

    private Boolean isDeleted;

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
