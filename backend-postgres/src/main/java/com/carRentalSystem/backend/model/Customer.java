package com.carRentalSystem.backend.model;


import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private String custName; // Primary Key
    
    private String custAddr;
    private String custPhone;
    
    @Column(unique = true)
    private String custEmail;
    
    private String custDriverLicense;
    private String custPassword;
    
    // Relationship with Location (Many customers can be in one location)
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    
    // Constructors
    public Customer() {}
    
    // Getters and Setters
    public String getCustName() { return custName; }
    public void setCustName(String custName) { this.custName = custName; }
    
    public String getCustAddr() { return custAddr; }
    public void setCustAddr(String custAddr) { this.custAddr = custAddr; }
    
    public String getCustPhone() { return custPhone; }
    public void setCustPhone(String custPhone) { this.custPhone = custPhone; }
    
    public String getCustEmail() { return custEmail; }
    public void setCustEmail(String custEmail) { this.custEmail = custEmail; }
    
    public String getCustDriverLicense() { return custDriverLicense; }
    public void setCustDriverLicense(String custDriverLicense) { this.custDriverLicense = custDriverLicense; }
    
    public String getCustPassword() { return custPassword; }
    public void setCustPassword(String custPassword) { this.custPassword = custPassword; }
    
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}