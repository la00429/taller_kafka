package co.edu.uptc.cqrsquery.repository;

import co.edu.uptc.cqrsquery.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
