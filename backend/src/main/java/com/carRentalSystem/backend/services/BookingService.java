package com.carRentalSystem.backend.services;

import com.carRentalSystem.backend.enums.BookingStatus;
import com.carRentalSystem.backend.model.Booking;
import com.carRentalSystem.backend.model.Customer;
import com.carRentalSystem.backend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    // CREATE
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    // READ - All bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    // READ - By ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    // READ - By customer
    public List<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }
    
    // READ - By status
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    
    // READ - With pagination
    public Page<Booking> getBookingsWithPagination(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }
    
    // UPDATE
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking existing = booking.get();
            existing.setCustomer(bookingDetails.getCustomer());
            existing.setCar(bookingDetails.getCar());
            existing.setStartDate(bookingDetails.getStartDate());
            existing.setEndDate(bookingDetails.getEndDate());
            existing.setTotalAmount(bookingDetails.getTotalAmount());
            existing.setStatus(bookingDetails.getStatus());
            return bookingRepository.save(existing);
        }
        return null;
    }
    
    // DELETE
    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}