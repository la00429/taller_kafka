package edu.uptc.swii.cqrscontroller.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Login;
import edu.uptc.swii.cqrscontroller.utils.JsonUtils;

@Service
public class LoginEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoginEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Login login) {
        String message = JsonUtils.toJson(login);
        System.out.println("LoginEventProducer send: " + message);
        kafkaTemplate.send(topic, message);
    }
}
