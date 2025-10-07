package co.edu.uptc.edamicrokafka.login.repository;

import co.edu.uptc.edamicrokafka.login.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    
    Optional<Login> findByCustomerId(Long customerId);
    
    Optional<Login> findByUsername(String username);
    
    Optional<Login> findByEmail(String email);
    
    List<Login> findByIsActiveTrue();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByCustomerId(Long customerId);
}
