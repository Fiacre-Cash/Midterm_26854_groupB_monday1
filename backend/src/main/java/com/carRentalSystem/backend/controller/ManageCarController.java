package com.carRentalSystem.backend.controller;


import com.carRentalSystem.backend.enums.CarStatus;
import com.carRentalSystem.backend.enums.CarType;
import com.carRentalSystem.backend.model.*;
import com.carRentalSystem.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/cars")
public class ManageCarController {
    @Autowired
    private ManageCarService manageCarService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<ManageCar> createCar(@RequestBody ManageCar car) {
        ManageCar newCar = manageCarService.createCar(car);
        return new ResponseEntity<>(newCar, HttpStatus.CREATED);
    }
    
    // READ - All
    @GetMapping
    public ResponseEntity<List<ManageCar>> getAllCars() {
        List<ManageCar> cars = manageCarService.getAllCars();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // READ - By plate number
    @GetMapping("/{plateNum}")
    public ResponseEntity<ManageCar> getCarByPlateNum(@PathVariable String plateNum) {
        Optional<ManageCar> car = manageCarService.getCarByPlateNum(plateNum);
        return car.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - By status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ManageCar>> getCarsByStatus(@PathVariable CarStatus status) {
        List<ManageCar> cars = manageCarService.getCarsByStatus(status);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // READ - By type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ManageCar>> getCarsByType(@PathVariable CarType type) {
        List<ManageCar> cars = manageCarService.getCarsByType(type);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // READ - Available cars by type
    @GetMapping("/available/{type}")
    public ResponseEntity<List<ManageCar>> getAvailableCarsByType(@PathVariable CarType type) {
        List<ManageCar> cars = manageCarService.getAvailableCarsByType(type);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // READ - By price range
    @GetMapping("/price")
    public ResponseEntity<List<ManageCar>> getCarsByPriceRange(
            @RequestParam Double minPrice, 
            @RequestParam Double maxPrice) {
        List<ManageCar> cars = manageCarService.getCarsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // READ - With pagination and sorting
    @GetMapping("/paginated")
    public ResponseEntity<Page<ManageCar>> getCarsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "carPlatenum") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ManageCar> cars = manageCarService.getCarsWithPagination(pageable);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
    
    // UPDATE
    @PutMapping("/{plateNum}")
    public ResponseEntity<ManageCar> updateCar(@PathVariable String plateNum, @RequestBody ManageCar car) {
        ManageCar updatedCar = manageCarService.updateCar(plateNum, car);
        if (updatedCar != null) {
            return new ResponseEntity<>(updatedCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE
    @DeleteMapping("/{plateNum}")
    public ResponseEntity<Void> deleteCar(@PathVariable String plateNum) {
        boolean deleted = manageCarService.deleteCar(plateNum);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
