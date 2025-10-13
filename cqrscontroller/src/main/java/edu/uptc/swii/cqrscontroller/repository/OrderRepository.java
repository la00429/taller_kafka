package edu.uptc.swii.cqrscontroller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.uptc.swii.cqrscontroller.model.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
}
