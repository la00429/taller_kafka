package edu.uptc.swii.cqrscontroller.controller;

import org.springframework.web.bind.annotation.*;
import edu.uptc.swii.cqrscontroller.model.Login;
import edu.uptc.swii.cqrscontroller.service.LoginService;

@RestController
@RequestMapping("/api/logins")
public class LoginController {
    private final LoginService service;
    
    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Login add(@RequestBody Login login) {
        return service.addLogin(login);
    }

    @PutMapping("/update")
    public Login update(@RequestBody Login login) {
        return service.updateLogin(login);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestBody Login login) {
        return service.deleteLogin(login);
    }
}
