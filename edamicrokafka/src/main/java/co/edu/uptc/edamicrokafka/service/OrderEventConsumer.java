package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEventConsumer {
    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "addorder_events", groupId = "order_group")
    public void handleAddOrderEvent(String order) {
        JsonUtils jsonUtil = new JsonUtils();
        Order receiveAddOrder = jsonUtil.fromJson(order, Order.class);
        orderService.save(receiveAddOrder);
        System.out.println("Order added: " + receiveAddOrder.getOrderId());
    }

    @KafkaListener(topics = "editorder_events", groupId = "order_group")
    public void handleEditOrderEvent(String order) {
        JsonUtils jsonUtil = new JsonUtils();
        Order receiveEditOrder = jsonUtil.fromJson(order, Order.class);
        orderService.save(receiveEditOrder);
        System.out.println("Order edited: " + receiveEditOrder.getOrderId());
    }

    @KafkaListener(topics = "findorderbyid_events", groupId = "order_group")
    public Order handleFindOrderByIdEvent(String orderId) {
        Order orderReceived = orderService.findById(Long.parseLong(orderId)).orElse(null);
        System.out.println("Order found: " + orderReceived);
        return orderReceived;
    }

    @KafkaListener(topics = "findallorders_events", groupId = "order_group")
    public List<Order> handleFindAllOrders() {
        List<Order> ordersReceived = orderService.findAll();
        System.out.println("All orders found: " + ordersReceived.size());
        return ordersReceived;
    }
}
