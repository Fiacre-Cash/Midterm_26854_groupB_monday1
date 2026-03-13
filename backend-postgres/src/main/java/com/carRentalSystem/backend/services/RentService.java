package com.carRentalSystem.backend.services;

import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.model.ManageCar;
import com.carRentalSystem.backend.model.Rent;
import com.carRentalSystem.backend.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class RentService {
    @Autowired
    private RentRepository rentRepository;
    
    // CREATE
    public Rent createRent(Rent rent) {
        return rentRepository.save(rent);
    }
    
    // READ - All rents
    public List<Rent> getAllRents() {
        return rentRepository.findAll();
    }
    
    // READ - By ID
    public Optional<Rent> getRentById(Long id) {
        return rentRepository.findById(id);
    }
    
    // READ - By customer
    public List<Rent> getRentsByCustomer(Customer customer) {
        return rentRepository.findByCustomer(customer);
    }
    
    // READ - By car
    public List<Rent> getRentsByCar(ManageCar car) {
        return rentRepository.findByCar(car);
    }
    
    // READ - Active rents
    public List<Rent> getActiveRents(LocalDate currentDate) {
        return rentRepository.findByReturnDateAfter(currentDate);
    }
    
    // READ - With pagination
    public Page<Rent> getRentsWithPagination(Pageable pageable) {
        return rentRepository.findAll(pageable);
    }
    
    // UPDATE
    public Rent updateRent(Long id, Rent rentDetails) {
        Optional<Rent> rent = rentRepository.findById(id);
        if (rent.isPresent()) {
            Rent existing = rent.get();
            existing.setCar(rentDetails.getCar());
            existing.setCustomer(rentDetails.getCustomer());
            existing.setRentDate(rentDetails.getRentDate());
            existing.setReturnDate(rentDetails.getReturnDate());
            existing.setRentFee(rentDetails.getRentFee());
            return rentRepository.save(existing);
        }
        return null;
    }
    
    // DELETE
    public boolean deleteRent(Long id) {
        if (rentRepository.existsById(id)) {
            rentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
