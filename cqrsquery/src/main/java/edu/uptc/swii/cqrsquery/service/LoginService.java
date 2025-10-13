package edu.uptc.swii.cqrsquery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Login;
import edu.uptc.swii.cqrsquery.repository.LoginRepository;

@Service
public class LoginService {
    private final LoginRepository repository;
    
    public LoginService(LoginRepository repository) {
        this.repository = repository;
    }
    
    public void addLogin(Login login) {
        repository.insert(login);
        System.out.println("Login added to MongoDB: " + login.getDocument());
    }
    
    public void updateLogin(Login login) {
        repository.save(login);
        System.out.println("Login updated in MongoDB: " + login.getDocument());
    }
    
    public void deleteLogin(Login login) {
        repository.delete(login);
        System.out.println("Login deleted from MongoDB: " + login.getDocument());
    }
    
    public List<Login> getAllLogins() {
        return repository.findAll();
    }
    
    public Optional<Login> getLoginById(String id) {
        return repository.findById(id);
    }
}
