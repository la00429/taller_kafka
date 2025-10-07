package co.edu.uptc.edamicrokafka.repository;

import co.edu.uptc.edamicrokafka.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
