package edu.uptc.swii.cqrsquery.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.uptc.swii.cqrsquery.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}
