package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UnifiedLoginEventProducer {
    private static final String LOGIN_TOPIC = "login_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAddLoginEvent(Login login) {
        String json = new JsonUtils().toJson(login);
        EventMessage event = new EventMessage("ADD", "LOGIN", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(LOGIN_TOPIC, eventJson);
    }

    public void sendEditLoginEvent(Login login) {
        String json = new JsonUtils().toJson(login);
        EventMessage event = new EventMessage("EDIT", "LOGIN", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(LOGIN_TOPIC, eventJson);
    }

    public void sendFindByLoginIdEvent(String username) {
        EventMessage event = new EventMessage("FIND_BY_ID", "LOGIN", username);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(LOGIN_TOPIC, eventJson);
    }

    public void sendFindAllLoginsEvent() {
        EventMessage event = new EventMessage("FIND_ALL", "LOGIN", "");
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(LOGIN_TOPIC, eventJson);
    }
}
