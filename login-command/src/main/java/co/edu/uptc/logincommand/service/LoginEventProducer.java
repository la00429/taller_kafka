package co.edu.uptc.logincommand.service;

import co.edu.uptc.logincommand.model.Login;
import co.edu.uptc.logincommand.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginEventProducer {
    
    private static final String LOGIN_TOPIC = "login_events";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    public void sendAddLoginEvent(Login login) {
        String json = JsonUtils.toJson(login);
        kafkaTemplate.send(LOGIN_TOPIC, "addLogin", json);
        System.out.println("Published addLogin event: " + json);
    }
    
    public void sendEditLoginEvent(Login login) {
        String json = JsonUtils.toJson(login);
        kafkaTemplate.send(LOGIN_TOPIC, "editLogin", json);
        System.out.println("Published editLogin event: " + json);
    }
    
    public void sendDeleteLoginEvent(Long id) {
        kafkaTemplate.send(LOGIN_TOPIC, "deleteLogin", id.toString());
        System.out.println("Published deleteLogin event for id: " + id);
    }
}
