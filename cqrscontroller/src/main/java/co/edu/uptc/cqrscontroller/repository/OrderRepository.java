package co.edu.uptc.cqrscontroller.repository;

import co.edu.uptc.cqrscontroller.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
