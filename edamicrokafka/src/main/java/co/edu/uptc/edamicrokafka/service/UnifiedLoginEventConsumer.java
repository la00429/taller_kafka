package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnifiedLoginEventConsumer {
    @Autowired
    private LoginService loginService;

    @KafkaListener(topics = "login_events", groupId = "unified_login_group")
    public void handleLoginEvent(String eventMessage) {
        try {
            JsonUtils jsonUtil = new JsonUtils();
            EventMessage event = jsonUtil.fromJson(eventMessage, EventMessage.class);
            
            if (!"LOGIN".equals(event.getEntityType())) {
                return; // Ignorar eventos que no son de Login
            }
            
            switch (event.getEventType()) {
                case "ADD":
                    handleAddLogin(event.getPayload());
                    break;
                case "EDIT":
                    handleEditLogin(event.getPayload());
                    break;
                case "FIND_BY_ID":
                    handleFindLoginById(event.getPayload());
                    break;
                case "FIND_ALL":
                    handleFindAllLogins();
                    break;
                default:
                    System.out.println("Unknown event type: " + event.getEventType());
            }
        } catch (Exception e) {
            System.err.println("Error processing login event: " + e.getMessage());
        }
    }

    private void handleAddLogin(String loginJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Login login = jsonUtil.fromJson(loginJson, Login.class);
        loginService.save(login);
        System.out.println("Login added: " + login.getUsername());
    }

    private void handleEditLogin(String loginJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Login login = jsonUtil.fromJson(loginJson, Login.class);
        loginService.save(login);
        System.out.println("Login edited: " + login.getUsername());
    }

    private Login handleFindLoginById(String username) {
        Login login = loginService.findByUsername(username);
        System.out.println("Login found: " + login);
        return login;
    }

    private List<Login> handleFindAllLogins() {
        List<Login> logins = loginService.findAll();
        System.out.println("All logins found: " + logins.size());
        return logins;
    }
}
