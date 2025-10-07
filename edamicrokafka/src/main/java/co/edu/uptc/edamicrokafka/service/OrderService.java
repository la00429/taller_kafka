package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> findByCustomerDocument(String customerDocument) {
        return orderRepository.findByCustomerDocument(customerDocument);
    }

    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> findByOrderDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
}
