package co.edu.uptc.loginquery.repository;

import co.edu.uptc.loginquery.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends MongoRepository<Login, String> {
}
