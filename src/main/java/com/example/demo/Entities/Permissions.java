package com.example.demo.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permissions extends BaseEntity {

 @Column(name = "module_name", nullable = false, length = 100)
 private String moduleName;

 @Column(name = "action", nullable = false, length = 50)
 private String action;

 @Column(name = "is_deleted", nullable = false)
 private boolean isDeleted = false;

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

 public boolean isDeleted() {
     return isDeleted;
 }

 public void setDeleted(boolean deleted) {
     isDeleted = deleted;
 }
}
