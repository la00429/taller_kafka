package co.edu.uptc.edamicrokafka.controller;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.service.UnifiedCustomerEventProducer;
import co.edu.uptc.edamicrokafka.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unified/customer")
@CrossOrigin(origins = "*")
public class UnifiedCustomerController {

    @Autowired
    private UnifiedCustomerEventProducer unifiedCustomerEventProducer;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        try {
            unifiedCustomerEventProducer.sendAddCustomerEvent(customer);
            return ResponseEntity.ok("Customer add event sent successfully via unified topic");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending customer add event: " + e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editCustomer(@RequestBody Customer customer) {
        try {
            unifiedCustomerEventProducer.sendEditCustomerEvent(customer);
            return ResponseEntity.ok("Customer edit event sent successfully via unified topic");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending customer edit event: " + e.getMessage());
        }
    }

    @GetMapping("/find/{document}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable String document) {
        try {
            unifiedCustomerEventProducer.sendFindByCustomerIdEvent(document);
            Customer customer = customerService.findById(document);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Customer>> findAllCustomers() {
        try {
            unifiedCustomerEventProducer.sendFindAllCustomersEvent();
            List<Customer> customers = customerService.findAll();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
