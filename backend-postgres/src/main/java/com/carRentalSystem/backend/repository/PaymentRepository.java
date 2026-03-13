package com.carRentalSystem.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.PaymentMethod;
import com.carRentalSystem.backend.enums.PaymentStatus;
import com.carRentalSystem.backend.model.Booking;
import com.carRentalSystem.backend.model.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find payment by booking
    Payment findByBooking(Booking booking);
    
    // Find payments by status
    List<Payment> findByPaymentStatus(PaymentStatus status);
    
    // Find payments by method
    List<Payment> findByPaymentMethod(PaymentMethod method);
    
    // Find payments by date range
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Check if payment exists for booking
    boolean existsByBooking(Booking booking);
    
    // Find with pagination
    Page<Payment> findAll(Pageable pageable);
}
