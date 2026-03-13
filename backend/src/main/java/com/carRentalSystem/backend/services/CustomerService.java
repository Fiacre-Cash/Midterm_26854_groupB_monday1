package com.carRentalSystem.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    // CREATE
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    // READ - All customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    // READ - By name (ID)
    public Optional<Customer> getCustomerByName(String name) {
        return customerRepository.findById(name);
    }
    
    // READ - By email
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByCustEmail(email);
    }
    
    // READ - By province
    public List<Customer> getCustomersByProvince(Province province) {
        return customerRepository.findByLocation_Province(province);
    }
    
    // READ - By district
    public List<Customer> getCustomersByDistrict(District district) {
        return customerRepository.findByLocation_District(district);
    }
    
    // READ - With pagination
    public Page<Customer> getCustomersWithPagination(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    // UPDATE
    public Customer updateCustomer(String name, Customer customerDetails) {
        Optional<Customer> customer = customerRepository.findById(name);
        if (customer.isPresent()) {
            Customer existing = customer.get();
            existing.setCustAddr(customerDetails.getCustAddr());
            existing.setCustPhone(customerDetails.getCustPhone());
            existing.setCustEmail(customerDetails.getCustEmail());
            existing.setCustDriverLicense(customerDetails.getCustDriverLicense());
            existing.setCustPassword(customerDetails.getCustPassword());
            existing.setLocation(customerDetails.getLocation());
            return customerRepository.save(existing);
        }
        return null;
    }
    
    // DELETE
    public boolean deleteCustomer(String name) {
        if (customerRepository.existsById(name)) {
            customerRepository.deleteById(name);
            return true;
        }
        return false;
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        return customerRepository.existsByCustEmail(email);
    }
    
    // Find customers by province using custom query (supports province code or name)
    public List<Customer> findCustomersByProvince(Province province) {
        return customerRepository.findCustomersByProvince(province);
    }
}
