package edu.uptc.swii.cqrsquery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Id
    private String document;
    private String username;
    private String password;

    public Login(String document, String username, String password) {
        this.document = document;
        this.username = username;
        this.password = password;
    }
}
