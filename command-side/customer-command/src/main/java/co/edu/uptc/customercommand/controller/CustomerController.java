package co.edu.uptc.customercommand.controller;

import co.edu.uptc.customercommand.model.Customer;
import co.edu.uptc.customercommand.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.ok(savedCustomer);
    }
    
    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.update(customer);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    @DeleteMapping("/{document}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String document) {
        customerService.delete(document);
        return ResponseEntity.ok().build();
    }
}
