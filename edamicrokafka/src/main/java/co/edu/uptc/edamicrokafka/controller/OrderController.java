package co.edu.uptc.edamicrokafka.controller;

import co.edu.uptc.edamicrokafka.model.Order;
import co.edu.uptc.edamicrokafka.service.OrderEventProducer;
import co.edu.uptc.edamicrokafka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        try {
            orderEventProducer.sendAddOrderEvent(order);
            return ResponseEntity.ok("Order add event sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending order add event: " + e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editOrder(@RequestBody Order order) {
        try {
            orderEventProducer.sendEditOrderEvent(order);
            return ResponseEntity.ok("Order edit event sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending order edit event: " + e.getMessage());
        }
    }

    @GetMapping("/find/{orderId}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long orderId) {
        try {
            orderEventProducer.sendFindByOrderIdEvent(orderId);
            Order order = orderService.findById(orderId).orElse(null);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Order>> findAllOrders() {
        try {
            orderEventProducer.sendFindAllOrdersEvent("findall");
            List<Order> orders = orderService.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findbycustomer/{customerDocument}")
    public ResponseEntity<List<Order>> findOrdersByCustomer(@PathVariable String customerDocument) {
        try {
            List<Order> orders = orderService.findByCustomerDocument(customerDocument);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findbystatus/{status}")
    public ResponseEntity<List<Order>> findOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.findByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
