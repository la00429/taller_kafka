package co.edu.uptc.edamicrokafka.order.repository;

import co.edu.uptc.edamicrokafka.order.model.Order;
import co.edu.uptc.edamicrokafka.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerId(Long customerId);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);
    
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Order> findByTotalAmountGreaterThanEqual(BigDecimal minAmount);
    
    List<Order> findByTotalAmountLessThanEqual(BigDecimal maxAmount);
    
    boolean existsByOrderNumber(String orderNumber);
}
