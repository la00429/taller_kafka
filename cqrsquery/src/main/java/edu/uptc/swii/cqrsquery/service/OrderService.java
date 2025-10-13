package edu.uptc.swii.cqrsquery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Order;
import edu.uptc.swii.cqrsquery.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository repository;
    
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
    
    public void addOrder(Order order) {
        repository.insert(order);
        System.out.println("Order added to MongoDB: " + order.getOrderId());
    }
    
    public void updateOrder(Order order) {
        repository.save(order);
        System.out.println("Order updated in MongoDB: " + order.getOrderId());
    }
    
    public void deleteOrder(Order order) {
        repository.delete(order);
        System.out.println("Order deleted from MongoDB: " + order.getOrderId());
    }
    
    public List<Order> getAllOrders() {
        return repository.findAll();
    }
    
    public Optional<Order> getOrderById(String id) {
        return repository.findById(id);
    }
}
