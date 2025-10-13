package co.edu.uptc.customercommand.service;

import co.edu.uptc.customercommand.model.Customer;
import co.edu.uptc.customercommand.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerEventProducer customerEventProducer;
    
    public Customer save(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        customerEventProducer.sendAddCustomerEvent(savedCustomer);
        return savedCustomer;
    }
    
    public Customer update(Customer customer) {
        Customer updatedCustomer = customerRepository.save(customer);
        customerEventProducer.sendEditCustomerEvent(updatedCustomer);
        return updatedCustomer;
    }
    
    public void delete(String document) {
        customerRepository.deleteById(document);
        customerEventProducer.sendDeleteCustomerEvent(document);
    }
    
    public Optional<Customer> findById(String document) {
        return customerRepository.findById(document);
    }
    
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
