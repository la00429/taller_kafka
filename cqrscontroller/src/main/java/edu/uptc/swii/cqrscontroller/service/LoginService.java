package edu.uptc.swii.cqrscontroller.service;

import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrscontroller.model.Login;
import edu.uptc.swii.cqrscontroller.repository.LoginRepository;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final LoginEventProducer loginEventProducer;
    
    private final String ADD_LOGIN_TOPIC = "add-login-topic";
    private final String UPDATE_LOGIN_TOPIC = "update-login-topic";
    private final String DELETE_LOGIN_TOPIC = "delete-login-topic";

    public LoginService(LoginRepository loginRepository, LoginEventProducer loginEventProducer) {
        this.loginRepository = loginRepository;
        this.loginEventProducer = loginEventProducer;
    }

    public Login addLogin(Login login) {
        Login saved = loginRepository.save(login);
        loginEventProducer.sendMessage(ADD_LOGIN_TOPIC, saved);
        return saved;
    }

    public Login updateLogin(Login login) {
        Login saved = loginRepository.save(login);
        loginEventProducer.sendMessage(UPDATE_LOGIN_TOPIC, saved);
        return saved;
    }

    public String deleteLogin(Login login) {
        try {
            loginRepository.delete(login);
            loginEventProducer.sendMessage(DELETE_LOGIN_TOPIC, login);
            return "deleted";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
