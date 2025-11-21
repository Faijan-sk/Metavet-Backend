package com.example.demo.Dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class PermissionResponseDto {

 private UUID uid;
 private String moduleName;
 private String action;
 private boolean isDeleted;
 private LocalDateTime createdAt;
 private LocalDateTime updatedAt;

 // Getters & Setters
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

 public boolean isDeleted() {
     return isDeleted;
 }

 public void setDeleted(boolean deleted) {
     isDeleted = deleted;
 }

 public LocalDateTime getCreatedAt() {
     return createdAt;
 }

 public void setCreatedAt(LocalDateTime createdAt) {
     this.createdAt = createdAt;
 }

 public LocalDateTime getUpdatedAt() {
     return updatedAt;
 }

 public void setUpdatedAt(LocalDateTime updatedAt) {
     this.updatedAt = updatedAt;
 }
}
