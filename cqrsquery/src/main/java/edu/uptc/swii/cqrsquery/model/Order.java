package edu.uptc.swii.cqrsquery.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String orderId;
    private String customerDocument;
    private String productName;
    private Integer quantity;
    private Double price;
    private LocalDateTime orderDate;
    private String status;

    public Order(String orderId, String customerDocument, String productName, Integer quantity, Double price) {
        this.orderId = orderId;
        this.customerDocument = customerDocument;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }
}
