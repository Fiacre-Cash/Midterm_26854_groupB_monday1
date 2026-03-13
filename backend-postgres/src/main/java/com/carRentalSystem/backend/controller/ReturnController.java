package com.carRentalSystem.backend.controller;


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
@RequestMapping("/api/returns")
public class ReturnController {
    @Autowired
    private ReturnService returnService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<Return> createReturn(@RequestBody Return returnObj) {
        Return newReturn = returnService.createReturn(returnObj);
        return new ResponseEntity<>(newReturn, HttpStatus.CREATED);
    }
    
    // READ - All
    @GetMapping
    public ResponseEntity<List<Return>> getAllReturns() {
        List<Return> returns = returnService.getAllReturns();
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }
    
    // READ - By ID
    @GetMapping("/{id}")
    public ResponseEntity<Return> getReturnById(@PathVariable Long id) {
        Optional<Return> returnObj = returnService.getReturnById(id);
        return returnObj.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // READ - Returns with delays
    @GetMapping("/delayed")
    public ResponseEntity<List<Return>> getReturnsWithDelay() {
        List<Return> returns = returnService.getReturnsWithDelay(0);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }
    
    // READ - With pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<Return>> getReturnsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("returnDate").descending());
        Page<Return> returns = returnService.getReturnsWithPagination(pageable);
        return new ResponseEntity<>(returns, HttpStatus.OK);
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Return> updateReturn(@PathVariable Long id, @RequestBody Return returnObj) {
        Return updatedReturn = returnService.updateReturn(id, returnObj);
        if (updatedReturn != null) {
            return new ResponseEntity<>(updatedReturn, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        boolean deleted = returnService.deleteReturn(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
