package co.edu.uptc.edamicrokafka.repository;

import co.edu.uptc.edamicrokafka.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerDocument(String customerDocument);
    List<Order> findByStatus(String status);
    List<Order> findByOrderDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
