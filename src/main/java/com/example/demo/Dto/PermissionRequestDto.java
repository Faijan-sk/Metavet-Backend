package com.example.demo.Dto;


import jakarta.validation.constraints.NotBlank;

public class PermissionRequestDto {

 @NotBlank(message = "moduleName is required")
 private String moduleName;

 @NotBlank(message = "action is required")
 private String action;

 // Getters & Setters
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
}
