package edu.uptc.swii.cqrscontroller.controller;

import org.springframework.web.bind.annotation.*;
import edu.uptc.swii.cqrscontroller.model.Customer;
import edu.uptc.swii.cqrscontroller.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Customer add(@RequestBody Customer customer) {
        return service.addCustomer(customer);
    }

    @PutMapping("/update")
    public Customer update(@RequestBody Customer customer) {
        return service.updateCustomer(customer);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestBody Customer customer) {
        return service.deleteCustomer(customer);
    }
}
