package edu.uptc.swii.cqrsquery.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import edu.uptc.swii.cqrsquery.model.Order;
import edu.uptc.swii.cqrsquery.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderQueryController {
    private final OrderService service;
    
    public OrderQueryController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/find/{id}")
    public Optional<Order> getOrderById(@PathVariable String id) {
        return service.getOrderById(id);
    }
}
