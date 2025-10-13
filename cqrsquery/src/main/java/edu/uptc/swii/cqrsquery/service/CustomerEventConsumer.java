package edu.uptc.swii.cqrsquery.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Customer;
import edu.uptc.swii.cqrsquery.utils.JsonUtils;

@Service
public class CustomerEventConsumer {
    private final CustomerService customerService;

    public CustomerEventConsumer(CustomerService customerService) {
        this.customerService = customerService;
    }

    @KafkaListener(topics = "add-customer-topic", groupId = "customer-group")
    public void addCustomerConsume(String message) {
        System.out.println("CustomerEventConsumer add message: " + message);
        Customer customer = JsonUtils.fromJson(message, Customer.class);
        customerService.addCustomer(customer);
    }

    @KafkaListener(topics = "update-customer-topic", groupId = "customer-group")
    public void updateCustomerConsume(String message) {
        System.out.println("CustomerEventConsumer update message: " + message);
        Customer customer = JsonUtils.fromJson(message, Customer.class);
        customerService.updateCustomer(customer);
    }

    @KafkaListener(topics = "delete-customer-topic", groupId = "customer-group")
    public void deleteCustomerConsume(String message) {
        System.out.println("CustomerEventConsumer delete message: " + message);
        Customer customer = JsonUtils.fromJson(message, Customer.class);
        customerService.deleteCustomer(customer);
    }
}
