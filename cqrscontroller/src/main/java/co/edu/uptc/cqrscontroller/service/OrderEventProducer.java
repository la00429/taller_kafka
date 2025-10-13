package co.edu.uptc.cqrscontroller.service;

import co.edu.uptc.cqrscontroller.model.Order;
import co.edu.uptc.cqrscontroller.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    
    private static final String ORDER_TOPIC = "order_events";
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    public void sendAddOrderEvent(Order order) {
        String json = JsonUtils.toJson(order);
        kafkaTemplate.send(ORDER_TOPIC, "addOrder", json);
        System.out.println("Published addOrder event: " + json);
    }
    
    public void sendEditOrderEvent(Order order) {
        String json = JsonUtils.toJson(order);
        kafkaTemplate.send(ORDER_TOPIC, "editOrder", json);
        System.out.println("Published editOrder event: " + json);
    }
    
    public void sendDeleteOrderEvent(Long id) {
        kafkaTemplate.send(ORDER_TOPIC, "deleteOrder", id.toString());
        System.out.println("Published deleteOrder event for id: " + id);
    }
}
