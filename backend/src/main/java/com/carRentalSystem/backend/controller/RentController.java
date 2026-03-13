package com.carRentalSystem.backend.controller;


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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/rents")
public class RentController {
    @Autowired
    private RentService rentService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<Rent> createRent(@RequestBody Rent rent) {
        Rent newRent = rentService.createRent(rent);
        return new ResponseEntity<>(newRent, HttpStatus.CREATED);
    }
    
    // READ - All
    @GetMapping
    public ResponseEntity<List<Rent>> getAllRents() {
        List<Rent> rents = rentService.getAllRents();
        return new ResponseEntity<>(rents, HttpStatus.OK);
    }
    
    // READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<Rent> getRentById(@PathVariable Long id) {
        Optional<Rent> rent = rentService.getRentById(id);
        return rent.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - Active rents
    @GetMapping("/active")
    public ResponseEntity<List<Rent>> getActiveRents() {
        List<Rent> rents = rentService.getActiveRents(LocalDate.now());
        return new ResponseEntity<>(rents, HttpStatus.OK);
    }
    
    // READ - With pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Rent>> getRentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Rent> rents = rentService.getRentsWithPagination(pageable);
        return new ResponseEntity<>(rents, HttpStatus.OK);
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Rent> updateRent(@PathVariable Long id, @RequestBody Rent rent) {
        Rent updatedRent = rentService.updateRent(id, rent);
        if (updatedRent != null) {
            return new ResponseEntity<>(updatedRent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        boolean deleted = rentService.deleteRent(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
