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
            // Check if key and message are JSON (with or without quotes)
            boolean keyIsJson = key != null && (key.startsWith("{") || key.startsWith("\"{"));
            boolean messageIsJson = message != null && (message.startsWith("{") || message.startsWith("\"{"));
            
            System.out.println("Key is JSON: " + keyIsJson);
            System.out.println("Message is JSON: " + messageIsJson);
            
            // If both key and message are JSON, assume it's an addCustomer event
            if (keyIsJson && messageIsJson) {
                System.out.println("Both key and message are JSON, treating as addCustomer event");
                // Use message if it's clean JSON, otherwise use key
                String jsonData = message.startsWith("{") ? message : key;
                handleAddCustomerEvent(jsonData);
                return;
            }
            
            // If key looks like JSON, it means they are swapped
            if (key != null && key.startsWith("{")) {
                System.out.println("Key is JSON, treating as addCustomer event");
                handleAddCustomerEvent(key);
                return;
            }
            
            // If message looks like JSON and key is a string, use the key as event type
            if (message != null && message.startsWith("{") && key != null && !key.startsWith("{")) {
                System.out.println("Message is JSON, key is event type: " + key);
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
                return;
            }
            
            // Default case - try to process as normal
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
