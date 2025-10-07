package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UnifiedOrderEventProducer {
    private static final String ORDER_TOPIC = "order_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAddOrderEvent(Order order) {
        String json = new JsonUtils().toJson(order);
        EventMessage event = new EventMessage("ADD", "ORDER", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(ORDER_TOPIC, eventJson);
    }

    public void sendEditOrderEvent(Order order) {
        String json = new JsonUtils().toJson(order);
        EventMessage event = new EventMessage("EDIT", "ORDER", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(ORDER_TOPIC, eventJson);
    }

    public void sendFindByOrderIdEvent(Long orderId) {
        EventMessage event = new EventMessage("FIND_BY_ID", "ORDER", orderId.toString());
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(ORDER_TOPIC, eventJson);
    }

    public void sendFindAllOrdersEvent() {
        EventMessage event = new EventMessage("FIND_ALL", "ORDER", "");
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(ORDER_TOPIC, eventJson);
    }
}
