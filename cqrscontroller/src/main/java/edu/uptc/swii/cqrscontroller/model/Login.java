package edu.uptc.swii.cqrscontroller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Id
    private String document;
    private String username;
    private String password; // En producción almacenar hash

    public Login(String document, String username, String password) {
        this.document = document;
        this.username = username;
        this.password = password;
    }
}
