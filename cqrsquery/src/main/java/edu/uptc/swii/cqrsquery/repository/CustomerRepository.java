package edu.uptc.swii.cqrsquery.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.uptc.swii.cqrsquery.model.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
