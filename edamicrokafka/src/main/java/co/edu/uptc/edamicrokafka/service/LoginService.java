package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public Login save(Login login) {
        return loginRepository.save(login);
    }

    public Optional<Login> findById(String username) {
        return loginRepository.findById(username);
    }

    public List<Login> findAll() {
        return loginRepository.findAll();
    }

    public void deleteById(String username) {
        loginRepository.deleteById(username);
    }

    public List<Login> findByCustomerDocument(String customerDocument) {
        return loginRepository.findByCustomerDocument(customerDocument);
    }

    public List<Login> findActiveLogins() {
        return loginRepository.findByIsActiveTrue();
    }

    public Login findByUsername(String username) {
        return loginRepository.findByUsername(username).orElse(null);
    }
}
