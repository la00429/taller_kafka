package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
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
        
        // Create login event when customer is created
        createLoginForCustomer(customer);
    }
    
    public void sendEditCustomerEvent(Customer customer) {
        String json = JsonUtils.toJson(customer);
        kafkaTemplate.send(CUSTOMER_TOPIC, "editCustomer", json);
    }
    
    public void sendFindByCustomerIdEvent(String document) {
        kafkaTemplate.send(CUSTOMER_TOPIC, "findCustomerById", document);
    }

    public void sendFindAllCustomersEvent(String customers) {
        kafkaTemplate.send(CUSTOMER_TOPIC, "findAllCustomers", customers);
    }
    
    private void createLoginForCustomer(Customer customer) {
        try {
            // Create login data for the customer
            Map<String, Object> loginData = new HashMap<>();
            loginData.put("customerId", customer.getId());
            loginData.put("username", customer.getEmail()); // Use email as username
            loginData.put("password", generateDefaultPassword(customer.getDocument())); // Generate default password
            loginData.put("email", customer.getEmail());
            
            String loginJson = JsonUtils.toJson(loginData);
            kafkaTemplate.send(LOGIN_TOPIC, "addLogin", loginJson);
            System.out.println("Published login creation event for customer: " + customer.getId());
        } catch (Exception e) {
            System.err.println("Error creating login for customer: " + e.getMessage());
        }
    }
    
    private String generateDefaultPassword(String document) {
        // Generate a default password based on document (in real app, this would be more secure)
        return "PWD_" + document.substring(Math.max(0, document.length() - 4));
    }
}
