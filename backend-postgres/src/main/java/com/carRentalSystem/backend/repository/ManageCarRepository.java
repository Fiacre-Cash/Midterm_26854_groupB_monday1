package com.carRentalSystem.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.CarStatus;
import com.carRentalSystem.backend.enums.CarType;
import com.carRentalSystem.backend.model.ManageCar;

@Repository
public interface ManageCarRepository extends JpaRepository<ManageCar, String> {
    // Find cars by status
    List<ManageCar> findByCarStatus(CarStatus status);
    
    // Find cars by type
    List<ManageCar> findByCarType(CarType type);
    
    // Find available cars by type
    List<ManageCar> findByCarStatusAndCarType(CarStatus status, CarType type);
    
    // Find cars by year range
    List<ManageCar> findByCarYearBetween(Integer startYear, Integer endYear);
    
    // Find cars by price range
    List<ManageCar> findByCarPriceBetween(Double minPrice, Double maxPrice);
    
    // Check if car exists by plate number
    boolean existsByCarPlatenum(String plateNum);
    
    // Find with pagination and sorting
    Page<ManageCar> findAll(Pageable pageable);
    
    // Find available cars with pagination
    Page<ManageCar> findByCarStatus(CarStatus status, Pageable pageable);
}
