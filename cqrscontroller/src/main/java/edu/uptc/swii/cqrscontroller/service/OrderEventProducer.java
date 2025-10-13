package edu.uptc.swii.cqrscontroller.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Order;
import edu.uptc.swii.cqrscontroller.utils.JsonUtils;

@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Order order) {
        String message = JsonUtils.toJson(order);
        System.out.println("OrderEventProducer send: " + message);
        kafkaTemplate.send(topic, message);
    }
}
