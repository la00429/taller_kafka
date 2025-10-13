package edu.uptc.swii.cqrscontroller.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "document")
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
