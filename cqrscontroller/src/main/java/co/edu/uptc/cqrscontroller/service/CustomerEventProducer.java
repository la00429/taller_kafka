package co.edu.uptc.cqrscontroller.service;

import co.edu.uptc.cqrscontroller.model.Customer;
import co.edu.uptc.cqrscontroller.model.Login;
import co.edu.uptc.cqrscontroller.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerEventProducer {
    
    private static final String CUSTOMER_TOPIC = "customer_events";
    private static final String LOGIN_TOPIC = "login_events";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    public void sendAddCustomerEvent(Customer customer) {
        String json = JsonUtils.toJson(customer);
        kafkaTemplate.send(CUSTOMER_TOPIC, "addCustomer", json);
        System.out.println("Published addCustomer event: " + json);
        
        // Auto-create Login when Customer is created
        createLoginForCustomer(customer);
    }
    
    public void sendEditCustomerEvent(Customer customer) {
        String json = JsonUtils.toJson(customer);
        kafkaTemplate.send(CUSTOMER_TOPIC, "editCustomer", json);
        System.out.println("Published editCustomer event: " + json);
    }
    
    public void sendDeleteCustomerEvent(String document) {
        kafkaTemplate.send(CUSTOMER_TOPIC, "deleteCustomer", document);
        System.out.println("Published deleteCustomer event for document: " + document);
    }
    
    private void createLoginForCustomer(Customer customer) {
        try {
            // Create login data for the customer
            Map<String, Object> loginData = new HashMap<>();
            loginData.put("customerId", customer.getDocument());
            loginData.put("username", customer.getEmail()); // Use email as username
            loginData.put("password", generateDefaultPassword(customer.getDocument())); // Generate default password
            loginData.put("email", customer.getEmail());
            
            String loginJson = JsonUtils.toJson(loginData);
            kafkaTemplate.send(LOGIN_TOPIC, "addLogin", loginJson);
            System.out.println("Published login creation event for customer: " + customer.getDocument());
        } catch (Exception e) {
            System.err.println("Error creating login for customer: " + e.getMessage());
        }
    }
    
    private String generateDefaultPassword(String document) {
        // Generate a default password based on document (in real app, this would be more secure)
        return "PWD_" + document.substring(Math.max(0, document.length() - 4));
    }
}
