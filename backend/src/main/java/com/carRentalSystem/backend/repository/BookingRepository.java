package com.carRentalSystem.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.BookingStatus;
import com.carRentalSystem.backend.model.Booking;
import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.model.ManageCar;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Find bookings by customer
    List<Booking> findByCustomer(Customer customer);
    
    // Find bookings by car
    List<Booking> findByCar(ManageCar car);
    
    // Find bookings by status
    List<Booking> findByStatus(BookingStatus status);
    
    // Find bookings by customer and status
    List<Booking> findByCustomerAndStatus(Customer customer, BookingStatus status);
    
    // Check if booking exists for a car in date range
    boolean existsByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        ManageCar car, LocalDate endDate, LocalDate startDate);
    
    // Find with pagination
    Page<Booking> findAll(Pageable pageable);
    
    // Find by customer with pagination
    Page<Booking> findByCustomer(Customer customer, Pageable pageable);
}
