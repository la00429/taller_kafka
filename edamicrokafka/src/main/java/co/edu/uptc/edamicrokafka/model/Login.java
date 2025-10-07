package co.edu.uptc.edamicrokafka.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "login")
@Data
@ToString
public class Login {
    @Id
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "customer_document")
    private String customerDocument;
    
    @Column(name = "is_active")
    private Boolean isActive;

    public Login() {
    }

    public Login(String username, String password, String customerDocument, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.customerDocument = customerDocument;
        this.isActive = isActive;
    }
}
