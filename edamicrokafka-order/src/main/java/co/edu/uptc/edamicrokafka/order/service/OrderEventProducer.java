package co.edu.uptc.edamicrokafka.order.service;

import co.edu.uptc.edamicrokafka.order.model.Order;
import co.edu.uptc.edamicrokafka.order.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private static final String ORDER_TOPIC = "order_events";
    
    public void sendOrderEvent(Order order) {
        try {
            String message = JsonUtils.toJson(order);
            kafkaTemplate.send(ORDER_TOPIC, "addOrder", message);
            System.out.println("Published addOrder event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing addOrder event: " + e.getMessage());
        }
    }
    
    public void publishEditOrderEvent(Order order) {
        try {
            String message = JsonUtils.toJson(order);
            kafkaTemplate.send(ORDER_TOPIC, "editOrder", message);
            System.out.println("Published editOrder event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing editOrder event: " + e.getMessage());
        }
    }
    
    public void publishFindOrderByIdEvent(Long orderId) {
        try {
            String message = JsonUtils.toJson(orderId);
            kafkaTemplate.send(ORDER_TOPIC, "findOrderById", message);
            System.out.println("Published findOrderById event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing findOrderById event: " + e.getMessage());
        }
    }
    
    public void publishFindAllOrdersEvent() {
        try {
            kafkaTemplate.send(ORDER_TOPIC, "findAllOrders", "");
            System.out.println("Published findAllOrders event");
        } catch (Exception e) {
            System.err.println("Error publishing findAllOrders event: " + e.getMessage());
        }
    }
    
    public void publishDeleteOrderEvent(Long orderId) {
        try {
            String message = JsonUtils.toJson(orderId);
            kafkaTemplate.send(ORDER_TOPIC, "deleteOrder", message);
            System.out.println("Published deleteOrder event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing deleteOrder event: " + e.getMessage());
        }
    }
    
    public void publishFindOrdersByCustomerIdEvent(Long customerId) {
        try {
            String message = JsonUtils.toJson(customerId);
            kafkaTemplate.send(ORDER_TOPIC, "findOrdersByCustomerId", message);
            System.out.println("Published findOrdersByCustomerId event: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing findOrdersByCustomerId event: " + e.getMessage());
        }
    }
}
