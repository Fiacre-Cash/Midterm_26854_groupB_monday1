package com.carRentalSystem.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.carRentalSystem.backend.enums.BookingStatus;



@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    
    @ManyToOne
    @JoinColumn(name = "cust_name")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "car_platenum")
    private ManageCar car;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalAmount;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    // Constructors
    public Booking() {}
    
    // Getters and Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public ManageCar getCar() { return car; }
    public void setCar(ManageCar car) { this.car = car; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}