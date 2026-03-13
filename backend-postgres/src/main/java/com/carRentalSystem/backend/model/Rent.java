package com.carRentalSystem.backend.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentId;
    
    @ManyToOne
    @JoinColumn(name = "car_platenum")
    private ManageCar car;
    
    @ManyToOne
    @JoinColumn(name = "cust_name")
    private Customer customer;
    
    private LocalDate rentDate;
    private LocalDate returnDate;
    private Double rentFee;
    
    // Constructors
    public Rent() {}
    
    // Getters and Setters
    public Long getRentId() { return rentId; }
    public void setRentId(Long rentId) { this.rentId = rentId; }
    
    public ManageCar getCar() { return car; }
    public void setCar(ManageCar car) { this.car = car; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public LocalDate getRentDate() { return rentDate; }
    public void setRentDate(LocalDate rentDate) { this.rentDate = rentDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public Double getRentFee() { return rentFee; }
    public void setRentFee(Double rentFee) { this.rentFee = rentFee; }
}