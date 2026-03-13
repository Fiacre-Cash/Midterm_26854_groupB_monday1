package com.carRentalSystem.backend.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import com.carRentalSystem.backend.enums.PaymentMethod;
import com.carRentalSystem.backend.enums.PaymentStatus;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    
    private LocalDate paymentDate;
    private Double paymentAmount;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    // Constructors
    public Payment() {}
    
    // Getters and Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    
    public Double getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(Double paymentAmount) { this.paymentAmount = paymentAmount; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
}