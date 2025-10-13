package edu.uptc.swii.cqrscontroller.service;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Customer;
import edu.uptc.swii.cqrscontroller.model.Login;
import edu.uptc.swii.cqrscontroller.repository.CustomerRepository;
import edu.uptc.swii.cqrscontroller.repository.LoginRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final LoginRepository loginRepository;
    private final CustomerEventProducer customerEventProducer;
    private final LoginEventProducer loginEventProducer;
    
    private final String ADD_CUSTOMER_TOPIC = "add-customer-topic";
    private final String UPDATE_CUSTOMER_TOPIC = "update-customer-topic";
    private final String DELETE_CUSTOMER_TOPIC = "delete-customer-topic";
    private final String ADD_LOGIN_TOPIC = "add-login-topic";

    public CustomerService(CustomerRepository customerRepository, 
                          LoginRepository loginRepository,
                          CustomerEventProducer customerEventProducer,
                          LoginEventProducer loginEventProducer) {
        this.customerRepository = customerRepository;
        this.loginRepository = loginRepository;
        this.customerEventProducer = customerEventProducer;
        this.loginEventProducer = loginEventProducer;
    }

    public Customer addCustomer(Customer customer) {
        // Guardar Customer en MySQL
        Customer saved = customerRepository.save(customer);
        
        // Publicar evento de Customer
        customerEventProducer.sendMessage(ADD_CUSTOMER_TOPIC, saved);
        
        // Auto-crear Login para el Customer
        createLoginForCustomer(saved);
        
        return saved;
    }

    public Customer updateCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);
        customerEventProducer.sendMessage(UPDATE_CUSTOMER_TOPIC, saved);
        return saved;
    }

    public String deleteCustomer(Customer customer) {
        try {
            customerRepository.delete(customer);
            customerEventProducer.sendMessage(DELETE_CUSTOMER_TOPIC, customer);
            return "deleted";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    private void createLoginForCustomer(Customer customer) {
        try {
            // Crear Login automáticamente
            String defaultPassword = generateDefaultPassword(customer.getDocument());
            Login login = new Login(
                customer.getDocument(),
                customer.getEmail(), // Usar email como username
                defaultPassword
            );
            
            // Guardar Login en MySQL
            Login savedLogin = loginRepository.save(login);
            
            // Publicar evento de Login
            loginEventProducer.sendMessage(ADD_LOGIN_TOPIC, savedLogin);
            
            System.out.println("Auto-created Login for Customer: " + customer.getDocument());
        } catch (Exception e) {
            System.err.println("Error creating Login for Customer: " + e.getMessage());
        }
    }
    
    private String generateDefaultPassword(String document) {
        // Generar contraseña por defecto basada en el documento
        return "PWD_" + document.substring(Math.max(0, document.length() - 4));
    }
}
