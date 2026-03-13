package com.carRentalSystem.backend.model;

import java.util.List;
import com.carRentalSystem.backend.enums.CarStatus;
import com.carRentalSystem.backend.enums.CarType;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class ManageCar {
    @Id
    private String carPlatenum; // Primary Key
    
    @Enumerated(EnumType.STRING)
    private CarType carType;
    
    private String carModel;
    private Integer carYear;
    private Double carPrice;
    
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;
    
    // Many-to-Many relationship with Admin
    @ManyToMany(mappedBy = "managedCars")
    private List<Admin> managingAdmins;
    
    // Constructors
    public ManageCar() {}
    
    // Getters and Setters
    public String getCarPlatenum() { return carPlatenum; }
    public void setCarPlatenum(String carPlatenum) { this.carPlatenum = carPlatenum; }
    
    public CarType getCarType() { return carType; }
    public void setCarType(CarType carType) { this.carType = carType; }
    
    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    
    public Integer getCarYear() { return carYear; }
    public void setCarYear(Integer carYear) { this.carYear = carYear; }
    
    public Double getCarPrice() { return carPrice; }
    public void setCarPrice(Double carPrice) { this.carPrice = carPrice; }
    
    public CarStatus getCarStatus() { return carStatus; }
    public void setCarStatus(CarStatus carStatus) { this.carStatus = carStatus; }
    
    public List<Admin> getManagingAdmins() { return managingAdmins; }
    public void setManagingAdmins(List<Admin> managingAdmins) { this.managingAdmins = managingAdmins; }
}