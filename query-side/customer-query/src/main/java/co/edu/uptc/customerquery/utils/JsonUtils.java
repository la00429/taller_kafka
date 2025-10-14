package co.edu.uptc.customerquery.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            // Remove surrounding quotes if present
            String cleanJson = json;
            if (json.startsWith("\"") && json.endsWith("\"")) {
                cleanJson = json.substring(1, json.length() - 1);
                // Unescape quotes
                cleanJson = cleanJson.replace("\\\"", "\"");
            }
            System.out.println("Cleaned JSON: " + cleanJson);
            return objectMapper.readValue(cleanJson, clazz);
        } catch (Exception e) {
            System.err.println("Error converting JSON to object: " + e.getMessage());
            System.err.println("Original JSON: " + json);
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }
}
