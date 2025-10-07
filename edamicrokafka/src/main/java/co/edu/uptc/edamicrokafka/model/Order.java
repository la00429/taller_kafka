package co.edu.uptc.edamicrokafka.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "customer_document")
    private String customerDocument;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "unit_price")
    private Double unitPrice;
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "status")
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

    public Order() {
    }

    public Order(String customerDocument, String productName, Integer quantity, 
                Double unitPrice, Double totalAmount, String status) {
        this.customerDocument = customerDocument;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }
}
