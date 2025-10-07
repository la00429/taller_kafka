package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerEventConsumer {
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private LoginService loginService;
    @KafkaListener(topics = "addcustomer_events", groupId = "customer_group" )
    public void handleAddOrderEvent(String  customer){
        JsonUtils jsonUtil = new JsonUtils();
        Customer receiveAddCustomer = jsonUtil.fromJson(customer, Customer.class);
        customerService.save(receiveAddCustomer);
        
        // Crear automáticamente un Login para el cliente
        createLoginForCustomer(receiveAddCustomer);
    }
    
    private void createLoginForCustomer(Customer customer) {
        try {
            // Generar username basado en el email o documento
            String username = customer.getEmail() != null ? 
                customer.getEmail().split("@")[0] : 
                "user_" + customer.getDocument();
            
            // Generar password temporal (en producción esto debería ser más seguro)
            String tempPassword = "temp_" + UUID.randomUUID().toString().substring(0, 8);
            
            // Crear el Login
            Login login = new Login(
                username,
                tempPassword,
                customer.getDocument(),
                true
            );
            
            // Guardar el Login
            loginService.save(login);
            System.out.println("Login created automatically for customer: " + customer.getDocument() + 
                             " with username: " + username);
        } catch (Exception e) {
            System.err.println("Error creating login for customer " + customer.getDocument() + ": " + e.getMessage());
        }
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
