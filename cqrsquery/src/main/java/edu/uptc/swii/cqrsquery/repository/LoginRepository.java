package edu.uptc.swii.cqrsquery.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import edu.uptc.swii.cqrsquery.model.Login;

public interface LoginRepository extends MongoRepository<Login, String> {
}
