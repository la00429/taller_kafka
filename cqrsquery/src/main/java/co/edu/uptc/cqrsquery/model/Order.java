package co.edu.uptc.cqrsquery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    private String id;
    
    private String customerId;
    private String productName;
    private Integer quantity;
    private Double price;
    private LocalDateTime orderDate;
    private String status;
}
