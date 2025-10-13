package co.edu.uptc.cqrsquery.controller;

import co.edu.uptc.cqrsquery.model.Login;
import co.edu.uptc.cqrsquery.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/logins")
@CrossOrigin(origins = "*")
public class LoginController {
    
    @Autowired
    private LoginRepository loginRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Login> getLoginById(@PathVariable String id) {
        Optional<Login> login = loginRepository.findById(id);
        return login.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Login>> getAllLogins() {
        List<Login> logins = loginRepository.findAll();
        return ResponseEntity.ok(logins);
    }
}
