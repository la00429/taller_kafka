package co.edu.uptc.edamicrokafka.controller;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.service.CustomerEventProducer;
import co.edu.uptc.edamicrokafka.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @Autowired
    private CustomerEventProducer customerEventProducer;
    
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        customerEventProducer.sendAddCustomerEvent(customer);
        return customer;
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        customerEventProducer.sendEditCustomerEvent(customer);
        return customer;
    }

    @GetMapping("/{document}")
    public Customer getCustomerById(@PathVariable String document) {
        customerEventProducer.sendFindByCustomerIdEvent(document);
        return customerService.findById(document);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        customerEventProducer.sendFindAllCustomersEvent("");
        return customerService.findAll();
    }
}

