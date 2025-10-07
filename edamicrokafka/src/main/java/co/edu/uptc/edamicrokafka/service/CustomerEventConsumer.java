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
    @KafkaListener(topics = "addcustomer_events", groupId = "customer_group" )
    public void handleAddOrderEvent(String  customer){
        JsonUtils jsonUtil = new JsonUtils();
        Customer receiveAddCustomer = jsonUtil.fromJson(customer, Customer.class);
        customerService.save(receiveAddCustomer);
    }
    @KafkaListener(topics = "editcustomer_events", groupId = "customer_group" )
    public void handleEditOrderEvent(String  customer){
        JsonUtils jsonUtil = new JsonUtils();
        Customer receiveEditCustomer = jsonUtil.fromJson(customer, Customer.class);
        customerService.save(receiveEditCustomer);
    }
    @KafkaListener(topics = "findcustomerbyid_events", groupId = "customer_group" )
    public Customer handleFindCustomerByIdEvent(String  customer){
        Customer customerReceived = customerService.findById(customer);
        return customerReceived;
    }
    @KafkaListener(topics = "findallcustomers_events", groupId = "customer_group" )
    public List<Customer> handleFindAllCustomers(){
        List<Customer> customersReceived = customerService.findAll();
        return customersReceived;
    }
}
