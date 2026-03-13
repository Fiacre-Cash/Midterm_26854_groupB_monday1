package com.carRentalSystem.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.model.Rent;
import com.carRentalSystem.backend.model.Return;


@Repository
public interface ReturnRepository extends JpaRepository<Return, Long> {
    // Find return by rent
    Return findByRent(Rent rent);
    
    // Find returns with delay
    List<Return> findByDelayGreaterThan(Integer delay);
    
    // Find returns by date range
    List<Return> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Check if return exists for rent
    boolean existsByRent(Rent rent);
    
    // Find with pagination
    Page<Return> findAll(Pageable pageable);
}
