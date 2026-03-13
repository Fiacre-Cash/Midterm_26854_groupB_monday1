package com.carRentalSystem.backend.model;

import java.util.List;

import com.carRentalSystem.backend.enums.AdminRole;

import jakarta.persistence.*;


@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    
    private String adminName;
    
    @Column(unique = true)
    private String adminEmail;
    
    private String adminPassword;
    
    @Enumerated(EnumType.STRING)
    private AdminRole adminRole;
    
    // Many-to-Many relationship with ManageCar
    @ManyToMany
    @JoinTable(
        name = "admin_car_management",
        joinColumns = @JoinColumn(name = "admin_id"),
        inverseJoinColumns = @JoinColumn(name = "car_platenum")
    )
    private List<ManageCar> managedCars;
    
    // Constructors
    public Admin() {}
    
    // Getters and Setters
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
    
    public String getAdminPassword() { return adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }
    
    public AdminRole getAdminRole() { return adminRole; }
    public void setAdminRole(AdminRole adminRole) { this.adminRole = adminRole; }
    
    public List<ManageCar> getManagedCars() { return managedCars; }
    public void setManagedCars(List<ManageCar> managedCars) { this.managedCars = managedCars; }
}