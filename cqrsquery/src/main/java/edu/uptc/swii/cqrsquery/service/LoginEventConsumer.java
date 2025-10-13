package edu.uptc.swii.cqrsquery.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import edu.uptc.swii.cqrsquery.model.Login;
import edu.uptc.swii.cqrsquery.utils.JsonUtils;

@Service
public class LoginEventConsumer {
    private final LoginService loginService;

    public LoginEventConsumer(LoginService loginService) {
        this.loginService = loginService;
    }

    @KafkaListener(topics = "add-login-topic", groupId = "login-group")
    public void addLoginConsume(String message) {
        System.out.println("LoginEventConsumer add message: " + message);
        Login login = JsonUtils.fromJson(message, Login.class);
        loginService.addLogin(login);
    }

    @KafkaListener(topics = "update-login-topic", groupId = "login-group")
    public void updateLoginConsume(String message) {
        System.out.println("LoginEventConsumer update message: " + message);
        Login login = JsonUtils.fromJson(message, Login.class);
        loginService.updateLogin(login);
    }

    @KafkaListener(topics = "delete-login-topic", groupId = "login-group")
    public void deleteLoginConsume(String message) {
        System.out.println("LoginEventConsumer delete message: " + message);
        Login login = JsonUtils.fromJson(message, Login.class);
        loginService.deleteLogin(login);
    }
}
