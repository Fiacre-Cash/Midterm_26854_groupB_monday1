package com.carRentalSystem.backend.services;

import com.carRentalSystem.backend.enums.PaymentMethod;
import com.carRentalSystem.backend.enums.PaymentStatus;
import com.carRentalSystem.backend.model.Booking;
import com.carRentalSystem.backend.model.Payment;
import com.carRentalSystem.backend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    
    // CREATE
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    // READ - All payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    // READ - By ID
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    // READ - By booking
    public Payment getPaymentByBooking(Booking booking) {
        return paymentRepository.findByBooking(booking);
    }
    
    // READ - By status
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }
    
    // READ - By method
    public List<Payment> getPaymentsByMethod(PaymentMethod method) {
        return paymentRepository.findByPaymentMethod(method);
    }
    
    // READ - With pagination
    public Page<Payment> getPaymentsWithPagination(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
    
    // UPDATE
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            Payment existing = payment.get();
            existing.setBooking(paymentDetails.getBooking());
            existing.setPaymentDate(paymentDetails.getPaymentDate());
            existing.setPaymentAmount(paymentDetails.getPaymentAmount());
            existing.setPaymentMethod(paymentDetails.getPaymentMethod());
            existing.setPaymentStatus(paymentDetails.getPaymentStatus());
            return paymentRepository.save(existing);
        }
        return null;
    }
    
    // DELETE
    public boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}