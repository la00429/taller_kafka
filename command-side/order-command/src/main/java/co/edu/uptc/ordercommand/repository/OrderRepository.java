package co.edu.uptc.ordercommand.repository;

import co.edu.uptc.ordercommand.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
