package com.carRentalSystem.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.enums.District;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

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
    
    // Parent-Child Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_location_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children"})
    private Location parentLocation;
    
    @OneToMany(mappedBy = "parentLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"parentLocation", "children"})
    private List<Location> children = new ArrayList<>();
    
    // Constructors
    public Location() {}
    
    public Location(Province province, District district, String sector, String cell, String village) {
        this.province = province;
        this.district = district;
        this.sector = sector;
        this.cell = cell;
        this.village = village;
    }
    
    // Helper methods for parent-child relationship
    public void addChild(Location child) {
        children.add(child);
        child.setParentLocation(this);
    }
    
    public void removeChild(Location child) {
        children.remove(child);
        child.setParentLocation(null);
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
    
    public Location getParentLocation() { return parentLocation; }
    public void setParentLocation(Location parentLocation) { this.parentLocation = parentLocation; }
    
    public List<Location> getChildren() { return children; }
    public void setChildren(List<Location> children) { this.children = children; }
    
    @Override
    public String toString() {
        return "Location [locationId=" + locationId + ", province=" + province + ", district=" + district + ", sector="
                + sector + ", cell=" + cell + ", village=" + village + ", parentLocationId=" 
                + (parentLocation != null ? parentLocation.getLocationId() : null) + "]";
    }

}
