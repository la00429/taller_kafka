package co.edu.uptc.edamicrokafka.controller;

import co.edu.uptc.edamicrokafka.model.Login;
import co.edu.uptc.edamicrokafka.service.LoginEventProducer;
import co.edu.uptc.edamicrokafka.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginEventProducer loginEventProducer;

    @Autowired
    private LoginService loginService;

    @PostMapping("/add")
    public ResponseEntity<String> addLogin(@RequestBody Login login) {
        try {
            loginEventProducer.sendAddLoginEvent(login);
            return ResponseEntity.ok("Login add event sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending login add event: " + e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editLogin(@RequestBody Login login) {
        try {
            loginEventProducer.sendEditLoginEvent(login);
            return ResponseEntity.ok("Login edit event sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending login edit event: " + e.getMessage());
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Login> findLoginById(@PathVariable String username) {
        try {
            loginEventProducer.sendFindByLoginIdEvent(username);
            Login login = loginService.findByUsername(username);
            return ResponseEntity.ok(login);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Login>> findAllLogins() {
        try {
            loginEventProducer.sendFindAllLoginsEvent("findall");
            List<Login> logins = loginService.findAll();
            return ResponseEntity.ok(logins);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findbycustomer/{customerDocument}")
    public ResponseEntity<List<Login>> findLoginsByCustomer(@PathVariable String customerDocument) {
        try {
            List<Login> logins = loginService.findByCustomerDocument(customerDocument);
            return ResponseEntity.ok(logins);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
