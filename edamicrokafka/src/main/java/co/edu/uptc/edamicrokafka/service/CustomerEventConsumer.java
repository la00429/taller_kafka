package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerEventConsumer {
    @Autowired
    private CustomerService customerService;
    
    @KafkaListener(topics = "customer_events", groupId = "customer_group")
    public void handleCustomerEvent(String message, String key) {
        System.out.println("Received customer event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addCustomer":
                    handleAddUserEvent(message);
                    break;
                case "editCustomer":
                    handleEditUserEvent(message);
                    break;
                case "findCustomerById":
                    handleFindUserByIdEvent(message);
                    break;
                case "findAllCustomers":
                    handleFindAllUsersEvent();
                    break;
                default:
                    System.out.println("Unknown customer event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing customer event: " + e.getMessage());
        }
    }
    
    private void handleAddUserEvent(String customer) {
        Customer receiveAddCustomer = JsonUtils.fromJson(customer, Customer.class);
        customerService.save(receiveAddCustomer);
        System.out.println("Customer added successfully: " + receiveAddCustomer);
    }
    
    private void handleEditUserEvent(String customer) {
        Customer receiveEditCustomer = JsonUtils.fromJson(customer, Customer.class);
        customerService.save(receiveEditCustomer);
        System.out.println("Customer updated successfully: " + receiveEditCustomer);
    }
    
    private void handleFindUserByIdEvent(String document) {
        Customer customerReceived = customerService.findById(document);
        if (customerReceived != null) {
            System.out.println("Found customer: " + customerReceived);
        } else {
            System.out.println("Customer not found with document: " + document);
        }
    }
    
    private void handleFindAllUsersEvent() {
        List<Customer> customersReceived = customerService.findAll();
        System.out.println("Found " + customersReceived.size() + " customers:");
        customersReceived.forEach(System.out::println);
    }
}
