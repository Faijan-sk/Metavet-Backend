package com.example.demo.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "permissions")
public class Permissions extends BaseEntity {

    @NotBlank(message = "Module name is required.")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Module name must contain only alphabets and spaces.")
    @Column(name = "module_name", nullable = false)
    private String moduleName;

    @Column(nullable = false)
    @NotBlank(message = "Action is Required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Action must contain only alphabets or numbers.")
    private String action;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
