package edu.uptc.swii.cqrsquery.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import edu.uptc.swii.cqrsquery.model.Login;
import edu.uptc.swii.cqrsquery.service.LoginService;

@RestController
@RequestMapping("/api/logins")
public class LoginQueryController {
    private final LoginService service;
    
    public LoginQueryController(LoginService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Login> getAllLogins() {
        return service.getAllLogins();
    }

    @GetMapping("/find/{id}")
    public Optional<Login> getLoginById(@PathVariable String id) {
        return service.getLoginById(id);
    }
}
