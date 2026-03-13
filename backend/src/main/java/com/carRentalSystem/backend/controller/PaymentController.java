package com.carRentalSystem.backend.controller;


import com.carRentalSystem.backend.enums.PaymentStatus;
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
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment newPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }
    
    // READ - All
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    // READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - By status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    // READ - With pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Payment>> getPaymentsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Payment> payments = paymentService.getPaymentsWithPagination(pageable);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(id, payment);
        if (updatedPayment != null) {
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        boolean deleted = paymentService.deletePayment(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
