package com.carRentalSystem.backend.controller;

import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.model.*;
import com.carRentalSystem.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;
    
    // CREATE - Add new location
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location newLocation = locationService.createLocation(location);
        return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
    }
    
    // READ - Get all locations
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // READ - Get location by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - Get locations by province
    @GetMapping("/province/{province}")
    public ResponseEntity<List<Location>> getLocationsByProvince(@PathVariable Province province) {
        List<Location> locations = locationService.getLocationsByProvince(province);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // READ - Get locations by district
    @GetMapping("/district/{district}")
    public ResponseEntity<List<Location>> getLocationsByDistrict(@PathVariable District district) {
        List<Location> locations = locationService.getLocationsByDistrict(district);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // READ - Get locations with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Location>> getLocationsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "locationId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Location> locations = locationService.getLocationsWithPagination(pageable);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // UPDATE - Update location
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        Location updatedLocation = locationService.updateLocation(id, location);
        if (updatedLocation != null) {
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE - Delete location
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        boolean deleted = locationService.deleteLocation(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // READ - Get child locations by parent ID
    @GetMapping("/{id}/children")
    public ResponseEntity<List<Location>> getChildLocations(@PathVariable Long id) {
        List<Location> children = locationService.getChildLocations(id);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }
    
    // READ - Get root locations (locations without parent)
    @GetMapping("/roots")
    public ResponseEntity<List<Location>> getRootLocations() {
        List<Location> rootLocations = locationService.getRootLocations();
        return new ResponseEntity<>(rootLocations, HttpStatus.OK);
    }
}
