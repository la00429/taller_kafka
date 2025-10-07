package co.edu.uptc.edamicrokafka.service;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginEventConsumer {
    @Autowired
    private LoginService loginService;

    @KafkaListener(topics = "addlogin_events", groupId = "login_group")
    public void handleAddLoginEvent(String login) {
        JsonUtils jsonUtil = new JsonUtils();
        Login receiveAddLogin = jsonUtil.fromJson(login, Login.class);
        loginService.save(receiveAddLogin);
        System.out.println("Login added: " + receiveAddLogin.getUsername());
    }

    @KafkaListener(topics = "editlogin_events", groupId = "login_group")
    public void handleEditLoginEvent(String login) {
        JsonUtils jsonUtil = new JsonUtils();
        Login receiveEditLogin = jsonUtil.fromJson(login, Login.class);
        loginService.save(receiveEditLogin);
        System.out.println("Login edited: " + receiveEditLogin.getUsername());
    }

    @KafkaListener(topics = "findloginbyid_events", groupId = "login_group")
    public Login handleFindLoginByIdEvent(String username) {
        Login loginReceived = loginService.findByUsername(username);
        System.out.println("Login found: " + loginReceived);
        return loginReceived;
    }

    @KafkaListener(topics = "findalllogins_events", groupId = "login_group")
    public List<Login> handleFindAllLogins() {
        List<Login> loginsReceived = loginService.findAll();
        System.out.println("All logins found: " + loginsReceived.size());
        return loginsReceived;
    }
}
