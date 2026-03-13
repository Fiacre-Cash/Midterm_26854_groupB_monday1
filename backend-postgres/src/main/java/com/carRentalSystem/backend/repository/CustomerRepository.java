package com.carRentalSystem.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.model.Location;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Find customer by email
    Customer findByCustEmail(String email);
    
    // Find customers by location
    List<Customer> findByLocation(Location location);
    
    // Find customers by province (through location)
    List<Customer> findByLocation_Province(Province province);
    
    // Find customers by district
    List<Customer> findByLocation_District(District district);
    
    // Check if customer exists by email
    boolean existsByCustEmail(String email);
    
    // Find customers with pagination and sorting
    Page<Customer> findAll(Pageable pageable);
    
    // Find by phone number
    Customer findByCustPhone(String phone);
    
    // Custom query to find all users from a given province using province code OR province name
    @Query("SELECT c FROM Customer c WHERE c.location.province = :province")
    List<Customer> findCustomersByProvince(@Param("province") Province province);
}
