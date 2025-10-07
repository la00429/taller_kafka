package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private static final String TOPIC_ADD = "addorder_events";
    private static final String TOPIC_EDIT = "editorder_events";
    private static final String TOPIC_FINDBYID = "findorderbyid_events";
    private static final String TOPIC_FINDALL = "findallorders_events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateAdd;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateEdit;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateFindById;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateFindAll;

    public void sendAddOrderEvent(Order order) {
        String json = new JsonUtils().toJson(order);
        kafkaTemplateAdd.send(TOPIC_ADD, json);
    }

    public void sendEditOrderEvent(Order order) {
        String json = new JsonUtils().toJson(order);
        kafkaTemplateEdit.send(TOPIC_EDIT, json);
    }

    public void sendFindByOrderIdEvent(Long orderId) {
        kafkaTemplateFindById.send(TOPIC_FINDBYID, orderId.toString());
    }

    public void sendFindAllOrdersEvent(String orders) {
        kafkaTemplateFindAll.send(TOPIC_FINDALL, orders);
    }
}
