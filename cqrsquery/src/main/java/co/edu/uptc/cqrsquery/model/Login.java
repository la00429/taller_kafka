package co.edu.uptc.cqrsquery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    
    @Id
    private String id;
    
    private String customerId;
    private String username;
    private String password;
    private String email;
}
