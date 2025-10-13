package edu.uptc.swii.cqrsquery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Customer;
import edu.uptc.swii.cqrsquery.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }
    
    public void addCustomer(Customer customer) {
        repository.insert(customer);
        System.out.println("Customer added to MongoDB: " + customer.getDocument());
    }
    
    public void updateCustomer(Customer customer) {
        repository.save(customer);
        System.out.println("Customer updated in MongoDB: " + customer.getDocument());
    }
    
    public void deleteCustomer(Customer customer) {
        repository.delete(customer);
        System.out.println("Customer deleted from MongoDB: " + customer.getDocument());
    }
    
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }
    
    public Optional<Customer> getCustomerById(String id) {
        return repository.findById(id);
    }
}
