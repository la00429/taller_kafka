package co.edu.uptc.edamicrokafka.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventMessage {
    private String eventType; // ADD, EDIT, FIND_BY_ID, FIND_ALL
    private String entityType; // CUSTOMER, LOGIN, ORDER
    private String payload; // JSON del objeto o parámetro
    private String timestamp;

    public EventMessage() {
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public EventMessage(String eventType, String entityType, String payload) {
        this();
        this.eventType = eventType;
        this.entityType = entityType;
        this.payload = payload;
    }
}
