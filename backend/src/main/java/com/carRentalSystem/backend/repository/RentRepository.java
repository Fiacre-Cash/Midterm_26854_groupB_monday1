package com.carRentalSystem.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.model.ManageCar;
import com.carRentalSystem.backend.model.Rent;


@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    // Find rents by customer
    List<Rent> findByCustomer(Customer customer);
    
    // Find rents by car
    List<Rent> findByCar(ManageCar car);
    
    // Find rents by date range
    List<Rent> findByRentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find active rents (return date not passed)
    List<Rent> findByReturnDateAfter(LocalDate currentDate);
    
    // Check if rent exists for car
    boolean existsByCar(ManageCar car);
    
    // Find with pagination
    Page<Rent> findAll(Pageable pageable);
}
