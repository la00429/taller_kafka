package co.edu.uptc.loginquery.service;

import co.edu.uptc.loginquery.model.Login;
import co.edu.uptc.loginquery.repository.LoginRepository;
import co.edu.uptc.loginquery.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginEventConsumer {
    
    @Autowired
    private LoginRepository loginRepository;
    
    @KafkaListener(topics = "login_events", groupId = "login_query_group")
    public void handleLoginEvent(String message, String key) {
        System.out.println("Received login event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addLogin":
                    handleAddLoginEvent(message);
                    break;
                case "editLogin":
                    handleEditLoginEvent(message);
                    break;
                case "deleteLogin":
                    handleDeleteLoginEvent(message);
                    break;
                default:
                    System.out.println("Unknown login event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing login event: " + e.getMessage());
        }
    }
    
    private void handleAddLoginEvent(String loginJson) {
        // Handle both Login object and Map from auto-creation
        try {
            Login login = JsonUtils.fromJson(loginJson, Login.class);
            // Convert Long id to String for MongoDB
            login.setId(login.getId().toString());
            loginRepository.save(login);
            System.out.println("Login added to MongoDB: " + login);
        } catch (Exception e) {
            // If it's a Map from auto-creation, convert it
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> loginData = JsonUtils.fromJson(loginJson, Map.class);
                Login login = new Login();
                login.setId(loginData.get("customerId").toString());
                login.setCustomerId(loginData.get("customerId").toString());
                login.setUsername(loginData.get("username").toString());
                login.setPassword(loginData.get("password").toString());
                login.setEmail(loginData.get("email").toString());
                
                loginRepository.save(login);
                System.out.println("Login (auto-created) added to MongoDB: " + login);
            } catch (Exception ex) {
                System.err.println("Error processing auto-created login: " + ex.getMessage());
            }
        }
    }
    
    private void handleEditLoginEvent(String loginJson) {
        Login login = JsonUtils.fromJson(loginJson, Login.class);
        // Convert Long id to String for MongoDB
        login.setId(login.getId().toString());
        loginRepository.save(login);
        System.out.println("Login updated in MongoDB: " + login);
    }
    
    private void handleDeleteLoginEvent(String id) {
        loginRepository.deleteById(id);
        System.out.println("Login deleted from MongoDB with id: " + id);
    }
}
