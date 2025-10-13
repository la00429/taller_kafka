package co.edu.uptc.cqrsquery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    private String document;
    
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String email;
}
