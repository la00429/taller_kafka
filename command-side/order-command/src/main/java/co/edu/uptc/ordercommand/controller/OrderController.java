package co.edu.uptc.ordercommand.controller;

import co.edu.uptc.ordercommand.model.Order;
import co.edu.uptc.ordercommand.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.save(order);
        return ResponseEntity.ok(savedOrder);
    }
    
    @PutMapping
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        Order updatedOrder = orderService.update(order);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
