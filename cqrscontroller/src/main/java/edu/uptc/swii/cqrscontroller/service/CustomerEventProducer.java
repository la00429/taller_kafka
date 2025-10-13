package edu.uptc.swii.cqrscontroller.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Customer;
import edu.uptc.swii.cqrscontroller.utils.JsonUtils;

@Service
public class CustomerEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CustomerEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Customer customer) {
        String message = JsonUtils.toJson(customer);
        System.out.println("CustomerEventProducer send: " + message);
        kafkaTemplate.send(topic, message);
    }
}
