package co.edu.uptc.edamicrokafka.controller;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.repository.CustomerRepository;
import co.edu.uptc.edamicrokafka.service.CustomerEventProducer;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
@Autowired
    private CustomerEventProducer customerEventProducer;
    private static JsonUtils jsonUtils = new JsonUtils();

    @PostMapping("/addcustomer")
    public String sendMessageAddCustomer(@RequestBody String customer){
        Customer customerObj = jsonUtils.fromJson(customer, Customer.class);
        customerEventProducer.sendAddCustomerEvent( customerObj);
        return customerEventProducer.toString();
    }

    @PostMapping("/editcustomer")
    public String sendMessageEditCustomer(@RequestBody String customer){
        Customer customerObj = jsonUtils.fromJson(customer, Customer.class);
        customerEventProducer.sendEditCustomerEvent( customerObj);
        return customerEventProducer.toString();
    }
}

