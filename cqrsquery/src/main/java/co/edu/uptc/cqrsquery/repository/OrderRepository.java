package co.edu.uptc.cqrsquery.repository;

import co.edu.uptc.cqrsquery.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
