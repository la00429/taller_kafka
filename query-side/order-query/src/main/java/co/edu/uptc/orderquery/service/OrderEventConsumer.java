package co.edu.uptc.orderquery.service;

import co.edu.uptc.orderquery.model.Order;
import co.edu.uptc.orderquery.repository.OrderRepository;
import co.edu.uptc.orderquery.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @KafkaListener(topics = "order_events", groupId = "order_query_group")
    public void handleOrderEvent(String message, String key) {
        System.out.println("Received order event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addOrder":
                    handleAddOrderEvent(message);
                    break;
                case "editOrder":
                    handleEditOrderEvent(message);
                    break;
                case "deleteOrder":
                    handleDeleteOrderEvent(message);
                    break;
                default:
                    System.out.println("Unknown order event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing order event: " + e.getMessage());
        }
    }
    
    private void handleAddOrderEvent(String orderJson) {
        Order order = JsonUtils.fromJson(orderJson, Order.class);
        // Convert Long id to String for MongoDB
        order.setId(order.getId().toString());
        orderRepository.save(order);
        System.out.println("Order added to MongoDB: " + order);
    }
    
    private void handleEditOrderEvent(String orderJson) {
        Order order = JsonUtils.fromJson(orderJson, Order.class);
        // Convert Long id to String for MongoDB
        order.setId(order.getId().toString());
        orderRepository.save(order);
        System.out.println("Order updated in MongoDB: " + order);
    }
    
    private void handleDeleteOrderEvent(String id) {
        orderRepository.deleteById(id);
        System.out.println("Order deleted from MongoDB with id: " + id);
    }
}
