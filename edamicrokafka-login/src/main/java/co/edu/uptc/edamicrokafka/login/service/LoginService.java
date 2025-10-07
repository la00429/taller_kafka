package co.edu.uptc.edamicrokafka.login.service;

import co.edu.uptc.edamicrokafka.login.model.Login;
import co.edu.uptc.edamicrokafka.login.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    
    @Autowired
    private LoginRepository loginRepository;
    
    @Autowired
    private LoginEventProducer loginEventProducer;
    
    public Login createLogin(Login login) {
        Login savedLogin = loginRepository.save(login);
        loginEventProducer.sendLoginEvent(savedLogin);
        return savedLogin;
    }
    
    public Login updateLogin(Login login) {
        if (loginRepository.existsById(login.getId())) {
            Login savedLogin = loginRepository.save(login);
            loginEventProducer.sendLoginEvent(savedLogin);
            return savedLogin;
        }
        return null;
    }
    
    public Optional<Login> findById(Long id) {
        Optional<Login> login = loginRepository.findById(id);
        if (login.isPresent()) {
            loginEventProducer.publishFindLoginByIdEvent(id);
        }
        return login;
    }
    
    public List<Login> findAll() {
        List<Login> logins = loginRepository.findAll();
        loginEventProducer.publishFindAllLoginsEvent();
        return logins;
    }
    
    public void deleteById(Long id) {
        if (loginRepository.existsById(id)) {
            loginRepository.deleteById(id);
            loginEventProducer.publishDeleteLoginEvent(id);
        }
    }
    
    public Optional<Login> findByCustomerId(Long customerId) {
        return loginRepository.findByCustomerId(customerId);
    }
    
    public Optional<Login> findByUsername(String username) {
        return loginRepository.findByUsername(username);
    }
    
    public Optional<Login> findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }
    
    public boolean existsByUsername(String username) {
        return loginRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return loginRepository.existsByEmail(email);
    }
    
    public boolean existsByCustomerId(Long customerId) {
        return loginRepository.existsByCustomerId(customerId);
    }
}
