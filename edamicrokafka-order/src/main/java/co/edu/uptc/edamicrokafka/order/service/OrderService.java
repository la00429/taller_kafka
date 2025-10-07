package co.edu.uptc.edamicrokafka.order.service;

import co.edu.uptc.edamicrokafka.order.model.Order;
import co.edu.uptc.edamicrokafka.order.model.OrderStatus;
import co.edu.uptc.edamicrokafka.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderEventProducer orderEventProducer;
    
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        orderEventProducer.publishAddOrderEvent(savedOrder);
        return savedOrder;
    }
    
    public Order updateOrder(Order order) {
        if (orderRepository.existsById(order.getId())) {
            Order savedOrder = orderRepository.save(order);
            orderEventProducer.publishEditOrderEvent(savedOrder);
            return savedOrder;
        }
        return null;
    }
    
    public Optional<Order> findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderEventProducer.publishFindOrderByIdEvent(id);
        }
        return order;
    }
    
    public List<Order> findAll() {
        List<Order> orders = orderRepository.findAll();
        orderEventProducer.publishFindAllOrdersEvent();
        return orders;
    }
    
    public void deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            orderEventProducer.publishDeleteOrderEvent(id);
        }
    }
    
    public List<Order> findByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        orderEventProducer.publishFindOrdersByCustomerIdEvent(customerId);
        return orders;
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }
    
    public List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    public boolean existsByOrderNumber(String orderNumber) {
        return orderRepository.existsByOrderNumber(orderNumber);
    }
}
