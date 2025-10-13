package edu.uptc.swii.cqrsquery.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import edu.uptc.swii.cqrsquery.model.Customer;
import edu.uptc.swii.cqrsquery.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerQueryController {
    private final CustomerService service;
    
    public CustomerQueryController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/find/{id}")
    public Optional<Customer> getCustomerById(@PathVariable String id) {
        return service.getCustomerById(id);
    }
}
