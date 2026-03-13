package com.carRentalSystem.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.enums.District;

@Entity
@Table(name = "locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    
    @Enumerated(EnumType.STRING)
    private Province province;
    
    @Enumerated(EnumType.STRING)
    private District district;
    
    private String sector;

    private String cell;
    
    private String village;
    
    // Constructors
    public Location() {}
    
    public Location(Province province, District district, String sector, String cell, String village) {
        this.province = province;
        this.district = district;
        this.sector = sector;
        this.cell = cell;
        this.village = village;
    }
    

    // Getters and Setters
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    
    public Province getProvince() { return province; }
    public void setProvince(Province province) { this.province = province; }
    
    public District getDistrict() { return district; }
    public void setDistrict(District district) { this.district = district; }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public String getCell() { return cell; }
    public void setCell(String cell) { this.cell = cell; }
    
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    
    @Override
    public String toString() {
        return "Location [locationId=" + locationId + ", province=" + province + ", district=" + district + ", sector="
                + sector + ", cell=" + cell + ", village=" + village + ", getLocationId()=" + getLocationId()
                + ", getProvince()=" + getProvince() + ", getDistrict()=" + getDistrict() + ", getSector()="
                + getSector() + ", getCell()=" + getCell() + ", getVillage()=" + getVillage() + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
