package edu.uptc.swii.cqrsquery.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Order;
import edu.uptc.swii.cqrsquery.utils.JsonUtils;

@Service
public class OrderEventConsumer {
    private final OrderService orderService;

    public OrderEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "add-order-topic", groupId = "order-group")
    public void addOrderConsume(String message) {
        System.out.println("OrderEventConsumer add message: " + message);
        Order order = JsonUtils.fromJson(message, Order.class);
        orderService.addOrder(order);
    }

    @KafkaListener(topics = "update-order-topic", groupId = "order-group")
    public void updateOrderConsume(String message) {
        System.out.println("OrderEventConsumer update message: " + message);
        Order order = JsonUtils.fromJson(message, Order.class);
        orderService.updateOrder(order);
    }

    @KafkaListener(topics = "delete-order-topic", groupId = "order-group")
    public void deleteOrderConsume(String message) {
        System.out.println("OrderEventConsumer delete message: " + message);
        Order order = JsonUtils.fromJson(message, Order.class);
        orderService.deleteOrder(order);
    }
}
