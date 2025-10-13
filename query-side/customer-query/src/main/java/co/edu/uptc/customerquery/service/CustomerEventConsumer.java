package co.edu.uptc.customerquery.service;

import co.edu.uptc.customerquery.model.Customer;
import co.edu.uptc.customerquery.repository.CustomerRepository;
import co.edu.uptc.customerquery.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerEventConsumer {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @KafkaListener(topics = "customer_events", groupId = "customer_query_group")
    public void handleCustomerEvent(String message, String key) {
        System.out.println("Received customer event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addCustomer":
                    handleAddCustomerEvent(message);
                    break;
                case "editCustomer":
                    handleEditCustomerEvent(message);
                    break;
                case "deleteCustomer":
                    handleDeleteCustomerEvent(message);
                    break;
                default:
                    System.out.println("Unknown customer event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing customer event: " + e.getMessage());
        }
    }
    
    private void handleAddCustomerEvent(String customerJson) {
        Customer customer = JsonUtils.fromJson(customerJson, Customer.class);
        customerRepository.save(customer);
        System.out.println("Customer added to MongoDB: " + customer);
    }
    
    private void handleEditCustomerEvent(String customerJson) {
        Customer customer = JsonUtils.fromJson(customerJson, Customer.class);
        customerRepository.save(customer);
        System.out.println("Customer updated in MongoDB: " + customer);
    }
    
    private void handleDeleteCustomerEvent(String document) {
        customerRepository.deleteById(document);
        System.out.println("Customer deleted from MongoDB with document: " + document);
    }
}
