package co.edu.uptc.edamicrokafka.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Customer routes
                .route("customer-service", r -> r
                        .path("/api/customer/**")
                        .uri("http://localhost:8080"))
                
                // Login routes
                .route("login-service", r -> r
                        .path("/api/login/**")
                        .uri("http://localhost:8080"))
                
                // Order routes
                .route("order-service", r -> r
                        .path("/api/order/**")
                        .uri("http://localhost:8080"))
                
                // Unified Customer routes
                .route("unified-customer-service", r -> r
                        .path("/api/unified/customer/**")
                        .uri("http://localhost:8080"))
                
                // Health check route
                .route("health-check", r -> r
                        .path("/health")
                        .uri("http://localhost:8080/actuator/health"))
                
                .build();
    }
}
