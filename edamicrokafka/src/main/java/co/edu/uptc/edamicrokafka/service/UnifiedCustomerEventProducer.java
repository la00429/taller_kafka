package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Customer;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UnifiedCustomerEventProducer {
    private static final String CUSTOMER_TOPIC = "customer_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAddCustomerEvent(Customer customer) {
        String json = new JsonUtils().toJson(customer);
        EventMessage event = new EventMessage("ADD", "CUSTOMER", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(CUSTOMER_TOPIC, eventJson);
    }

    public void sendEditCustomerEvent(Customer customer) {
        String json = new JsonUtils().toJson(customer);
        EventMessage event = new EventMessage("EDIT", "CUSTOMER", json);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(CUSTOMER_TOPIC, eventJson);
    }

    public void sendFindByCustomerIdEvent(String document) {
        EventMessage event = new EventMessage("FIND_BY_ID", "CUSTOMER", document);
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(CUSTOMER_TOPIC, eventJson);
    }

    public void sendFindAllCustomersEvent() {
        EventMessage event = new EventMessage("FIND_ALL", "CUSTOMER", "");
        String eventJson = new JsonUtils().toJson(event);
        kafkaTemplate.send(CUSTOMER_TOPIC, eventJson);
    }
}
