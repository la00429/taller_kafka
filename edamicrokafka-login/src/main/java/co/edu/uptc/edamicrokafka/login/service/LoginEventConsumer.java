package co.edu.uptc.edamicrokafka.login.service;

import co.edu.uptc.edamicrokafka.login.model.Login;
import co.edu.uptc.edamicrokafka.login.repository.LoginRepository;
import co.edu.uptc.edamicrokafka.login.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginEventConsumer {
    
    @Autowired
    private LoginRepository loginRepository;
    
    @KafkaListener(topics = "login_events", groupId = "login_group")
    public void handleLoginEvent(String message, String key) {
        System.out.println("Received login event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addLogin":
                    handleAddLogin(message);
                    break;
                case "editLogin":
                    handleEditLogin(message);
                    break;
                case "findLoginById":
                    handleFindLoginById(message);
                    break;
                case "findAllLogins":
                    handleFindAllLogins();
                    break;
                case "deleteLogin":
                    handleDeleteLogin(message);
                    break;
                default:
                    System.out.println("Unknown event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing login event: " + e.getMessage());
        }
    }
    
    private void handleAddLogin(String message) {
        try {
            Login login = JsonUtils.fromJson(message, Login.class);
            Login savedLogin = loginRepository.save(login);
            System.out.println("Login created successfully: " + savedLogin);
        } catch (Exception e) {
            System.err.println("Error adding login: " + e.getMessage());
        }
    }
    
    private void handleEditLogin(String message) {
        try {
            Login login = JsonUtils.fromJson(message, Login.class);
            if (loginRepository.existsById(login.getId())) {
                Login savedLogin = loginRepository.save(login);
                System.out.println("Login updated successfully: " + savedLogin);
            } else {
                System.out.println("Login not found with ID: " + login.getId());
            }
        } catch (Exception e) {
            System.err.println("Error editing login: " + e.getMessage());
        }
    }
    
    private void handleFindLoginById(String message) {
        try {
            Long loginId = JsonUtils.fromJson(message, Long.class);
            Login login = loginRepository.findById(loginId).orElse(null);
            if (login != null) {
                System.out.println("Found login: " + login);
            } else {
                System.out.println("Login not found with ID: " + loginId);
            }
        } catch (Exception e) {
            System.err.println("Error finding login by ID: " + e.getMessage());
        }
    }
    
    private void handleFindAllLogins() {
        try {
            List<Login> logins = loginRepository.findAll();
            System.out.println("Found " + logins.size() + " logins:");
            logins.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error finding all logins: " + e.getMessage());
        }
    }
    
    private void handleDeleteLogin(String message) {
        try {
            Long loginId = JsonUtils.fromJson(message, Long.class);
            if (loginRepository.existsById(loginId)) {
                loginRepository.deleteById(loginId);
                System.out.println("Login deleted successfully with ID: " + loginId);
            } else {
                System.out.println("Login not found with ID: " + loginId);
            }
        } catch (Exception e) {
            System.err.println("Error deleting login: " + e.getMessage());
        }
    }
}
