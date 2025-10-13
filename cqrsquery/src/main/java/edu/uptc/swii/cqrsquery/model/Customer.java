package edu.uptc.swii.cqrsquery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "customer")
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

    public Customer(String document, String firstname, String lastname, String address, String phone, String email) {
        this.document = document;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
