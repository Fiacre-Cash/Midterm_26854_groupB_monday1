package com.carRentalSystem.backend.services;

import com.carRentalSystem.backend.enums.CarStatus;
import com.carRentalSystem.backend.enums.CarType;
import com.carRentalSystem.backend.model.ManageCar;
import com.carRentalSystem.backend.repository.ManageCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ManageCarService {
    @Autowired
    private ManageCarRepository manageCarRepository;
    
    // CREATE
    public ManageCar createCar(ManageCar car) {
        return manageCarRepository.save(car);
    }
    
    // READ - All cars
    public List<ManageCar> getAllCars() {
        return manageCarRepository.findAll();
    }
    
    // READ - By plate number
    public Optional<ManageCar> getCarByPlateNum(String plateNum) {
        return manageCarRepository.findById(plateNum);
    }
    
    // READ - By status
    public List<ManageCar> getCarsByStatus(CarStatus status) {
        return manageCarRepository.findByCarStatus(status);
    }
    
    // READ - By type
    public List<ManageCar> getCarsByType(CarType type) {
        return manageCarRepository.findByCarType(type);
    }
    
    // READ - Available cars by type
    public List<ManageCar> getAvailableCarsByType(CarType type) {
        return manageCarRepository.findByCarStatusAndCarType(CarStatus.available, type);
    }
    
    // READ - By price range
    public List<ManageCar> getCarsByPriceRange(Double minPrice, Double maxPrice) {
        return manageCarRepository.findByCarPriceBetween(minPrice, maxPrice);
    }
    
    // READ - With pagination
    public Page<ManageCar> getCarsWithPagination(Pageable pageable) {
        return manageCarRepository.findAll(pageable);
    }
    
    // UPDATE
    public ManageCar updateCar(String plateNum, ManageCar carDetails) {
        Optional<ManageCar> car = manageCarRepository.findById(plateNum);
        if (car.isPresent()) {
            ManageCar existing = car.get();
            existing.setCarType(carDetails.getCarType());
            existing.setCarModel(carDetails.getCarModel());
            existing.setCarYear(carDetails.getCarYear());
            existing.setCarPrice(carDetails.getCarPrice());
            existing.setCarStatus(carDetails.getCarStatus());
            return manageCarRepository.save(existing);
        }
        return null;
    }
    
    // DELETE
    public boolean deleteCar(String plateNum) {
        if (manageCarRepository.existsById(plateNum)) {
            manageCarRepository.deleteById(plateNum);
            return true;
        }
        return false;
    }
}
