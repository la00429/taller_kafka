package co.edu.uptc.edamicrokafka.login.controller;

import co.edu.uptc.edamicrokafka.login.model.Login;
import co.edu.uptc.edamicrokafka.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            // Validate required fields
            if (login.getCustomerId() == null || login.getUsername() == null || 
                login.getPassword() == null || login.getEmail() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if username or email already exists
            if (loginService.existsByUsername(login.getUsername()) || 
                loginService.existsByEmail(login.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            Login createdLogin = loginService.createLogin(login);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLogin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Login> updateLogin(@PathVariable Long id, @RequestBody Login login) {
        try {
            login.setId(id);
            Login updatedLogin = loginService.updateLogin(login);
            if (updatedLogin != null) {
                return ResponseEntity.ok(updatedLogin);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Login> getLoginById(@PathVariable Long id) {
        try {
            Optional<Login> login = loginService.findById(id);
            return login.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Login>> getAllLogins() {
        try {
            List<Login> logins = loginService.findAll();
            return ResponseEntity.ok(logins);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogin(@PathVariable Long id) {
        try {
            loginService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Login> getLoginByCustomerId(@PathVariable Long customerId) {
        try {
            Optional<Login> login = loginService.findByCustomerId(customerId);
            return login.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<Login> getLoginByUsername(@PathVariable String username) {
        try {
            Optional<Login> login = loginService.findByUsername(username);
            return login.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Login> getLoginByEmail(@PathVariable String email) {
        try {
            Optional<Login> login = loginService.findByEmail(email);
            return login.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
