package co.edu.uptc.edamicrokafka.login.service;

import co.edu.uptc.edamicrokafka.login.model.Login;
import co.edu.uptc.edamicrokafka.login.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginEventProducer {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private static final String LOGIN_TOPIC = "login_events";
    
    public void publishAddLoginEvent(Login login) {
        try {
            String message = JsonUtils.toJson(login);
            kafkaTemplate.send(LOGIN_TOPIC, "addLogin", message);
            System.out.println("Published addLogin event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing addLogin event: " + e.getMessage());
        }
    }
    
    public void publishEditLoginEvent(Login login) {
        try {
            String message = JsonUtils.toJson(login);
            kafkaTemplate.send(LOGIN_TOPIC, "editLogin", message);
            System.out.println("Published editLogin event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing editLogin event: " + e.getMessage());
        }
    }
    
    public void publishFindLoginByIdEvent(Long loginId) {
        try {
            String message = JsonUtils.toJson(loginId);
            kafkaTemplate.send(LOGIN_TOPIC, "findLoginById", message);
            System.out.println("Published findLoginById event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing findLoginById event: " + e.getMessage());
        }
    }
    
    public void publishFindAllLoginsEvent() {
        try {
            kafkaTemplate.send(LOGIN_TOPIC, "findAllLogins", "");
            System.out.println("Published findAllLogins event");
        } catch (Exception e) {
            System.err.println("Error publishing findAllLogins event: " + e.getMessage());
        }
    }
    
    public void publishDeleteLoginEvent(Long loginId) {
        try {
            String message = JsonUtils.toJson(loginId);
            kafkaTemplate.send(LOGIN_TOPIC, "deleteLogin", message);
            System.out.println("Published deleteLogin event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing deleteLogin event: " + e.getMessage());
        }
    }
}
