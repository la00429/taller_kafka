package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean save(Customer customer) {
        boolean flag = false;
        Customer processCustomer = customerRepository.saveAndFlush(customer);
        if (processCustomer != null) {
            flag = true;
        }
        return flag;
    }

    public Customer findById(String document) {
        Customer customer = null;
        Optional<Customer> optionalCustomer = customerRepository.findById(document);
        if (optionalCustomer.isPresent()) {
            customer = optionalCustomer.get();
        }
        return customer;
    }

    public List<Customer> findAll() {
        List<Customer> listCustomer = new ArrayList<Customer>();
        Iterable<Customer> customers = customerRepository.findAll();
        customers.forEach(customer -> listCustomer.add(customer)
        );
        return listCustomer;

    }
}
