package edu.uptc.swii.cqrscontroller.service;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Order;
import edu.uptc.swii.cqrscontroller.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    
    private final String ADD_ORDER_TOPIC = "add-order-topic";
    private final String UPDATE_ORDER_TOPIC = "update-order-topic";
    private final String DELETE_ORDER_TOPIC = "delete-order-topic";

    public OrderService(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
    }

    public Order addOrder(Order order) {
        Order saved = orderRepository.save(order);
        orderEventProducer.sendMessage(ADD_ORDER_TOPIC, saved);
        return saved;
    }

    public Order updateOrder(Order order) {
        Order saved = orderRepository.save(order);
        orderEventProducer.sendMessage(UPDATE_ORDER_TOPIC, saved);
        return saved;
    }

    public String deleteOrder(Order order) {
        try {
            orderRepository.delete(order);
            orderEventProducer.sendMessage(DELETE_ORDER_TOPIC, order);
            return "deleted";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
