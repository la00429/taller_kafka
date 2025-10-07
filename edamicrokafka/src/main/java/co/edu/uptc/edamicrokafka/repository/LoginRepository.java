package co.edu.uptc.edamicrokafka.repository;

import co.edu.uptc.edamicrokafka.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<Login> findByUsername(String username);
    List<Login> findByCustomerDocument(String customerDocument);
    List<Login> findByIsActiveTrue();
}
