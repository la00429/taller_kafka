package co.edu.uptc.logincommand.controller;

import co.edu.uptc.logincommand.model.Login;
import co.edu.uptc.logincommand.service.LoginService;
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
    private LoginService loginService;
    
    @PostMapping
    public ResponseEntity<Login> createLogin(@RequestBody Login login) {
        Login savedLogin = loginService.save(login);
        return ResponseEntity.ok(savedLogin);
    }
    
    @PutMapping
    public ResponseEntity<Login> updateLogin(@RequestBody Login login) {
        Login updatedLogin = loginService.update(login);
        return ResponseEntity.ok(updatedLogin);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogin(@PathVariable Long id) {
        loginService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Login> getLoginById(@PathVariable Long id) {
        Optional<Login> login = loginService.findById(id);
        return login.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Login>> getAllLogins() {
        List<Login> logins = loginService.findAll();
        return ResponseEntity.ok(logins);
    }
}
