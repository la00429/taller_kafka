package co.edu.uptc.edamicrokafka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
@CrossOrigin(origins = "*")
public class GatewayController {

    @GetMapping("/info")
    public Map<String, Object> getGatewayInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "EDA Microservices API Gateway");
        info.put("version", "1.0.0");
        info.put("description", "Centralized API Gateway for Customer, Login, and Order microservices");
        
        Map<String, String> routes = new HashMap<>();
        routes.put("Customer Service", "/api/customer/**");
        routes.put("Login Service", "/api/login/**");
        routes.put("Order Service", "/api/order/**");
        routes.put("Unified Customer Service", "/api/unified/customer/**");
        routes.put("Health Check", "/health");
        
        info.put("available_routes", routes);
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("Customer CRUD", "GET, POST, PUT /api/customer/");
        endpoints.put("Login CRUD", "GET, POST, PUT /api/login/");
        endpoints.put("Order CRUD", "GET, POST, PUT /api/order/");
        endpoints.put("Unified Customer", "GET, POST, PUT /api/unified/customer/");
        
        info.put("endpoints", endpoints);
        
        return info;
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("gateway", "ACTIVE");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        return health;
    }
}
