package co.edu.uptc.cqrscontroller.controller;

import co.edu.uptc.cqrscontroller.model.Customer;
import co.edu.uptc.cqrscontroller.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    
    @GetMapping("/{document}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String document) {
        Optional<Customer> customer = customerService.findById(document);
        return customer.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }
}
