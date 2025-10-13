package co.edu.uptc.ordercommand.service;

import co.edu.uptc.ordercommand.model.Order;
import co.edu.uptc.ordercommand.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderEventProducer orderEventProducer;
    
    public Order save(Order order) {
        Order savedOrder = orderRepository.save(order);
        orderEventProducer.sendAddOrderEvent(savedOrder);
        return savedOrder;
    }
    
    public Order update(Order order) {
        Order updatedOrder = orderRepository.save(order);
        orderEventProducer.sendEditOrderEvent(updatedOrder);
        return updatedOrder;
    }
    
    public void delete(Long id) {
        orderRepository.deleteById(id);
        orderEventProducer.sendDeleteOrderEvent(id);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
