package com.carRentalSystem.backend.services;

import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.model.Location;
import com.carRentalSystem.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// 1. Location Service
@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    
    // CREATE - Add new location
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }
    
    // READ - Get all locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    // READ - Get location by ID
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }
    
    // READ - Get locations by province
    public List<Location> getLocationsByProvince(Province province) {
        return locationRepository.findByProvince(province);
    }
    
    // READ - Get locations by district
    public List<Location> getLocationsByDistrict(District district) {
        return locationRepository.findByDistrict(district);
    }
    
    // READ - Get locations with pagination
    public Page<Location> getLocationsWithPagination(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }
    
    // UPDATE - Update existing location
    public Location updateLocation(Long id, Location locationDetails) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            Location existingLocation = location.get();
            existingLocation.setProvince(locationDetails.getProvince());
            existingLocation.setDistrict(locationDetails.getDistrict());
            existingLocation.setSector(locationDetails.getSector());
            existingLocation.setCell(locationDetails.getCell());
            existingLocation.setVillage(locationDetails.getVillage());
            return locationRepository.save(existingLocation);
        }
        return null;
    }
    
    // DELETE - Delete location
    public boolean deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Check if location exists
    public boolean locationExists(Province province, District district) {
        return locationRepository.existsByProvinceAndDistrict(province, district);
    }
}
