package com.carRentalSystem.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carRentalSystem.backend.enums.AdminRole;
import com.carRentalSystem.backend.model.Admin;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Find admin by email
    Admin findByAdminEmail(String email);
    
    // Find admins by role
    List<Admin> findByAdminRole(AdminRole role);
    
    // Check if admin exists by email
    boolean existsByAdminEmail(String email);
    
    // Find with pagination
    Page<Admin> findAll(Pageable pageable);
}
