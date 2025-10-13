package edu.uptc.swii.cqrscontroller.controller;

import org.springframework.web.bind.annotation.*;
import edu.uptc.swii.cqrscontroller.model.Order;
import edu.uptc.swii.cqrscontroller.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Order add(@RequestBody Order order) {
        return service.addOrder(order);
    }

    @PutMapping("/update")
    public Order update(@RequestBody Order order) {
        return service.updateOrder(order);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestBody Order order) {
        return service.deleteOrder(order);
    }
}
