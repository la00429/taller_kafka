package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.model.EventMessage;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnifiedOrderEventConsumer {
    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "order_events", groupId = "unified_order_group")
    public void handleOrderEvent(String eventMessage) {
        try {
            JsonUtils jsonUtil = new JsonUtils();
            EventMessage event = jsonUtil.fromJson(eventMessage, EventMessage.class);
            
            if (!"ORDER".equals(event.getEntityType())) {
                return; // Ignorar eventos que no son de Order
            }
            
            switch (event.getEventType()) {
                case "ADD":
                    handleAddOrder(event.getPayload());
                    break;
                case "EDIT":
                    handleEditOrder(event.getPayload());
                    break;
                case "FIND_BY_ID":
                    handleFindOrderById(event.getPayload());
                    break;
                case "FIND_ALL":
                    handleFindAllOrders();
                    break;
                default:
                    System.out.println("Unknown event type: " + event.getEventType());
            }
        } catch (Exception e) {
            System.err.println("Error processing order event: " + e.getMessage());
        }
    }

    private void handleAddOrder(String orderJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Order order = jsonUtil.fromJson(orderJson, Order.class);
        orderService.save(order);
        System.out.println("Order added: " + order.getOrderId());
    }

    private void handleEditOrder(String orderJson) {
        JsonUtils jsonUtil = new JsonUtils();
        Order order = jsonUtil.fromJson(orderJson, Order.class);
        orderService.save(order);
        System.out.println("Order edited: " + order.getOrderId());
    }

    private Order handleFindOrderById(String orderId) {
        Order order = orderService.findById(Long.parseLong(orderId)).orElse(null);
        System.out.println("Order found: " + order);
        return order;
    }

    private List<Order> handleFindAllOrders() {
        List<Order> orders = orderService.findAll();
        System.out.println("All orders found: " + orders.size());
        return orders;
    }
}
