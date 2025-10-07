package co.edu.uptc.edamicrokafka.order.service;

import co.edu.uptc.edamicrokafka.order.model.Order;
import co.edu.uptc.edamicrokafka.order.repository.OrderRepository;
import co.edu.uptc.edamicrokafka.order.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEventConsumer {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @KafkaListener(topics = "order_events", groupId = "order_group")
    public void handleOrderEvent(String message, String key) {
        System.out.println("Received order event with key: " + key + ", message: " + message);
        
        try {
            switch (key) {
                case "addOrder":
                    handleAddOrder(message);
                    break;
                case "editOrder":
                    handleEditOrder(message);
                    break;
                case "findOrderById":
                    handleFindOrderById(message);
                    break;
                case "findAllOrders":
                    handleFindAllOrders();
                    break;
                case "deleteOrder":
                    handleDeleteOrder(message);
                    break;
                case "findOrdersByCustomerId":
                    handleFindOrdersByCustomerId(message);
                    break;
                default:
                    System.out.println("Unknown event type: " + key);
            }
        } catch (Exception e) {
            System.err.println("Error processing order event: " + e.getMessage());
        }
    }
    
    private void handleAddOrder(String message) {
        try {
            Order order = JsonUtils.fromJson(message, Order.class);
            Order savedOrder = orderRepository.save(order);
            System.out.println("Order created successfully: " + savedOrder);
        } catch (Exception e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
    }
    
    private void handleEditOrder(String message) {
        try {
            Order order = JsonUtils.fromJson(message, Order.class);
            if (orderRepository.existsById(order.getId())) {
                Order savedOrder = orderRepository.save(order);
                System.out.println("Order updated successfully: " + savedOrder);
            } else {
                System.out.println("Order not found with ID: " + order.getId());
            }
        } catch (Exception e) {
            System.err.println("Error editing order: " + e.getMessage());
        }
    }
    
    private void handleFindOrderById(String message) {
        try {
            Long orderId = JsonUtils.fromJson(message, Long.class);
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                System.out.println("Found order: " + order);
            } else {
                System.out.println("Order not found with ID: " + orderId);
            }
        } catch (Exception e) {
            System.err.println("Error finding order by ID: " + e.getMessage());
        }
    }
    
    private void handleFindAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            System.out.println("Found " + orders.size() + " orders:");
            orders.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error finding all orders: " + e.getMessage());
        }
    }
    
    private void handleDeleteOrder(String message) {
        try {
            Long orderId = JsonUtils.fromJson(message, Long.class);
            if (orderRepository.existsById(orderId)) {
                orderRepository.deleteById(orderId);
                System.out.println("Order deleted successfully with ID: " + orderId);
            } else {
                System.out.println("Order not found with ID: " + orderId);
            }
        } catch (Exception e) {
            System.err.println("Error deleting order: " + e.getMessage());
        }
    }
    
    private void handleFindOrdersByCustomerId(String message) {
        try {
            Long customerId = JsonUtils.fromJson(message, Long.class);
            List<Order> orders = orderRepository.findByCustomerId(customerId);
            System.out.println("Found " + orders.size() + " orders for customer ID: " + customerId);
            orders.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Error finding orders by customer ID: " + e.getMessage());
        }
    }
}
