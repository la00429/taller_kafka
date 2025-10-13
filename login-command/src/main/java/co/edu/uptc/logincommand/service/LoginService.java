package co.edu.uptc.logincommand.service;

import co.edu.uptc.logincommand.model.Login;
import co.edu.uptc.logincommand.repository.LoginRepository;
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
    
    public Login save(Login login) {
        Login savedLogin = loginRepository.save(login);
        loginEventProducer.sendAddLoginEvent(savedLogin);
        return savedLogin;
    }
    
    public Login update(Login login) {
        Login updatedLogin = loginRepository.save(login);
        loginEventProducer.sendEditLoginEvent(updatedLogin);
        return updatedLogin;
    }
    
    public void delete(Long id) {
        loginRepository.deleteById(id);
        loginEventProducer.sendDeleteLoginEvent(id);
    }
    
    public Optional<Login> findById(Long id) {
        return loginRepository.findById(id);
    }
    
    public List<Login> findAll() {
        return loginRepository.findAll();
    }
}
