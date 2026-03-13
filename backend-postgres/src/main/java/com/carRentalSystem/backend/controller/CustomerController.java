package com.carRentalSystem.backend.controller;


import com.carRentalSystem.backend.enums.District;
import com.carRentalSystem.backend.enums.Province;
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
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
    
    // READ - All
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    // READ - By name
    @GetMapping("/{name}")
    public ResponseEntity<Customer> getCustomerByName(@PathVariable String name) {
        Optional<Customer> customer = customerService.getCustomerByName(name);
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - By province (Required for project)
    @GetMapping("/province/{province}")
    public ResponseEntity<List<Customer>> getCustomersByProvince(@PathVariable Province province) {
        List<Customer> customers = customerService.getCustomersByProvince(province);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    // READ - By district
    @GetMapping("/district/{district}")
    public ResponseEntity<List<Customer>> getCustomersByDistrict(@PathVariable District district) {
        List<Customer> customers = customerService.getCustomersByDistrict(district);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    // READ - With pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Customer>> getCustomersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "custName") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Customer> customers = customerService.getCustomersWithPagination(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    // UPDATE
    @PutMapping("/{name}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String name, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(name, customer);
        if (updatedCustomer != null) {
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String name) {
        boolean deleted = customerService.deleteCustomer(name);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // GET - Find customers by province using custom query (Required for project - 4 marks)
    @GetMapping("/province/query/{province}")
    public ResponseEntity<List<Customer>> findCustomersByProvince(@PathVariable Province province) {
        List<Customer> customers = customerService.findCustomersByProvince(province);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
