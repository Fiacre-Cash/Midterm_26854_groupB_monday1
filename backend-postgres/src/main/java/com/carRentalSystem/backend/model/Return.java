package com.carRentalSystem.backend.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "returns")
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnId;
    
    @OneToOne
    @JoinColumn(name = "rent_id")
    private Rent rent;
    
    private LocalDate returnDate;
    private Integer delay; // in days
    private Double fine;
    
    // Constructors
    public Return() {}
    
    // Getters and Setters
    public Long getReturnId() { return returnId; }
    public void setReturnId(Long returnId) { this.returnId = returnId; }
    
    public Rent getRent() { return rent; }
    public void setRent(Rent rent) { this.rent = rent; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public Integer getDelay() { return delay; }
    public void setDelay(Integer delay) { this.delay = delay; }
    
    public Double getFine() { return fine; }
    public void setFine(Double fine) { this.fine = fine; }
}