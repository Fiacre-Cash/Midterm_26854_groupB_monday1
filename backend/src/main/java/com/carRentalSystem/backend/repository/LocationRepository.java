package com.carRentalSystem.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Find locations by province
    List<Location> findByProvince(Province province);
    
    // Find locations by district
    List<Location> findByDistrict(District district);
    
    // Check if location exists by province and district
    boolean existsByProvinceAndDistrict(Province province, District district);
    
    // Find with pagination
    Page<Location> findAll(Pageable pageable);
    
    // Find child locations by parent ID
    List<Location> findByParentLocationLocationId(Long parentId);
    
    // Find root locations (no parent)
    List<Location> findByParentLocationIsNull();
}
