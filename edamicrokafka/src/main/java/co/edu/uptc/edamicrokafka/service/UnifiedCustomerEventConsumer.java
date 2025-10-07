package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UnifiedCustomerEventConsumer {
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private LoginService loginService;

    @KafkaListener(topics = "customer_events", groupId = "unified_customer_group")
    public void handleCustomerEvent(String eventMessage) {
        try {
            JsonUtils jsonUtil = new JsonUtils();
            EventMessage event = jsonUtil.fromJson(eventMessage, EventMessage.class);
            
            if (!"CUSTOMER".equals(event.getEntityType())) {
                return; // Ignorar eventos que no son de Customer
            }
            
            switch (event.getEventType()) {
                case "ADD":
                    handleAddCustomer(event.getPayload());
                    break;
                case "EDIT":
                    handleEditCustomer(event.getPayload());
                    break;
                case "FIND_BY_ID":
                    handleFindCustomerById(event.getPayload());
                    break;
                case "FIND_ALL":
                    handleFindAllCustomers();
                    break;
                default:
                    System.out.println("Unknown event type: " + event.getEventType());
            }
        } catch (Exception e) {
            System.err.println("Error processing customer event: " + e.getMessage());
        }
    }

    private void handleAddCustomer(String customerJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Customer customer = jsonUtil.fromJson(customerJson, Customer.class);
        customerService.save(customer);
        createLoginForCustomer(customer);
        System.out.println("Customer added: " + customer.getDocument());
    }

    private void handleEditCustomer(String customerJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Customer customer = jsonUtil.fromJson(customerJson, Customer.class);
        customerService.save(customer);
        System.out.println("Customer edited: " + customer.getDocument());
    }

    private Customer handleFindCustomerById(String document) {
        Customer customer = customerService.findById(document);
        System.out.println("Customer found: " + customer);
        return customer;
    }

    private List<Customer> handleFindAllCustomers() {
        List<Customer> customers = customerService.findAll();
        System.out.println("All customers found: " + customers.size());
        return customers;
    }

    private void createLoginForCustomer(Customer customer) {
        try {
            String username = customer.getEmail() != null ? 
                customer.getEmail().split("@")[0] : 
                "user_" + customer.getDocument();
            
            String tempPassword = "temp_" + UUID.randomUUID().toString().substring(0, 8);
            
            Login login = new Login(
                username,
                tempPassword,
                customer.getDocument(),
                true
            );
            
            loginService.save(login);
            System.out.println("Login created automatically for customer: " + customer.getDocument() + 
                             " with username: " + username);
        } catch (Exception e) {
            System.err.println("Error creating login for customer " + customer.getDocument() + ": " + e.getMessage());
        }
    }
}
