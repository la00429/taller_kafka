# 📸 EVIDENCIA: TÓPICO ÚNICO POR ENTIDAD (Punto 4)

## 🎯 **OBJETIVO**
Demostrar que cada entidad de negocio (Customer, Login, Order) usa un **único tópico de Kafka** para manejar **TODOS** sus eventos, diferenciados por claves.

## 📋 **EVIDENCIAS A CAPTURAR**

### **1. CONSOLAS DE MICROSERVICIOS**

#### **Customer Service (Puerto 8080)**
```
2025-10-06T20:30:15.123 --- [customer_group] Received customer event with key: addCustomer
2025-10-06T20:30:15.124 --- [customer_group] Customer added successfully: Customer{...}

2025-10-06T20:30:16.456 --- [customer_group] Received customer event with key: findCustomerById
2025-10-06T20:30:16.457 --- [customer_group] Found customer: Customer{...}

2025-10-06T20:30:17.789 --- [customer_group] Received customer event with key: findAllCustomers
2025-10-06T20:30:17.790 --- [customer_group] Found 1 customers: [Customer{...}]

2025-10-06T20:30:18.012 --- [customer_group] Received customer event with key: editCustomer
2025-10-06T20:30:18.013 --- [customer_group] Customer updated successfully: Customer{...}
```

**✅ EVIDENCIA**: Todos los eventos usan el **MISMO tópico** `customer_events` con **claves diferentes**.

#### **Login Service (Puerto 8081)**
```
2025-10-06T20:30:15.123 --- [login_group] Received login event with key: addLogin
2025-10-06T20:30:15.124 --- [login_group] Login added successfully: Login{...}
```

**✅ EVIDENCIA**: Todos los eventos de Login usan el **MISMO tópico** `login_events`.

#### **Order Service (Puerto 8082)**
```
2025-10-06T20:30:15.123 --- [order_group] Received order event with key: addOrder
2025-10-06T20:30:15.124 --- [order_group] Order added successfully: Order{...}

2025-10-06T20:30:16.456 --- [order_group] Received order event with key: findAllOrders
2025-10-06T20:30:16.457 --- [order_group] Found 1 orders: [Order{...}]
```

**✅ EVIDENCIA**: Todos los eventos de Order usan el **MISMO tópico** `order_events`.

### **2. CÓDIGO FUENTE**

#### **CustomerEventConsumer.java (Líneas 16-40)**
```java
@KafkaListener(topics = "customer_events", groupId = "customer_group")
public void handleCustomerEvent(String message, String key) {
    System.out.println("Received customer event with key: " + key + ", message: " + message);
    
    try {
        switch (key) {
            case "addCustomer":
                handleAddCustomerEvent(message);
                break;
            case "editCustomer":
                handleEditCustomerEvent(message);
                break;
            case "findCustomerById":
                handleFindCustomerByIdEvent(message);
                break;
            case "findAllCustomers":
                handleFindAllCustomersEvent();
                break;
            default:
                System.out.println("Unknown customer event type: " + key);
        }
    } catch (Exception e) {
        System.err.println("Error processing customer event: " + e.getMessage());
    }
}
```

**✅ EVIDENCIA**: Un solo `@KafkaListener` escucha el tópico `customer_events` y usa `switch` para manejar diferentes claves.

#### **CustomerEventProducer.java (Líneas 20-39)**
```java
public void sendAddCustomerEvent(Customer customer) {
    String json = JsonUtils.toJson(customer);
    kafkaTemplate.send(CUSTOMER_TOPIC, "addCustomer", json);  // Clave: addCustomer
}

public void sendEditCustomerEvent(Customer customer) {
    String json = JsonUtils.toJson(customer);
    kafkaTemplate.send(CUSTOMER_TOPIC, "editCustomer", json);  // Clave: editCustomer
}

public void sendFindByCustomerIdEvent(String document) {
    kafkaTemplate.send(CUSTOMER_TOPIC, "findCustomerById", document);  // Clave: findCustomerById
}

public void sendFindAllCustomersEvent(String customers) {
    kafkaTemplate.send(CUSTOMER_TOPIC, "findAllCustomers", customers);  // Clave: findAllCustomers
}
```

**✅ EVIDENCIA**: Todos los métodos envían al **MISMO tópico** `CUSTOMER_TOPIC` con **claves diferentes**.

### **3. CONFIGURACIÓN DE KAFKA**

#### **docker-compose.yaml (Líneas 45-47)**
```yaml
kafka-init:
  image: confluentinc/cp-kafka:7.4.0
  depends_on:
    - kafka
  entrypoint: ["/bin/sh", "-c"]
  command: |
    "
    kafka-topics --create --if-not-exists --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1 --topic customer_events &&
    kafka-topics --create --if-not-exists --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1 --topic login_events &&
    kafka-topics --create --if-not-exists --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1 --topic order_events
    "
```

**✅ EVIDENCIA**: Solo se crean **3 tópicos únicos** (uno por entidad), no múltiples tópicos por operación.

## 🧪 **PRUEBAS PARA GENERAR EVIDENCIA**

### **Ejecutar el script:**
```bash
evidencia-topico-unico.bat
```

### **O manualmente:**

#### **1. Crear Customer (genera evento `addCustomer`)**
```bash
POST http://localhost:8080/api/customers
{
    "document": "19273",
    "firstname": "Laura", 
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "pepito@c.com"
}
```

#### **2. Buscar Customer por ID (genera evento `findCustomerById`)**
```bash
GET http://localhost:8080/api/customers/19273
```

#### **3. Obtener todos los Customers (genera evento `findAllCustomers`)**
```bash
GET http://localhost:8080/api/customers
```

#### **4. Actualizar Customer (genera evento `editCustomer`)**
```bash
PUT http://localhost:8080/api/customers
{
    "document": "19273",
    "firstname": "Laura", 
    "lastname": "Perez Updated",
    "address": "Sur",
    "phone": "5123452",
    "email": "pepito@c.com"
}
```

## 📸 **CAPTURAS DE PANTALLA REQUERIDAS**

1. **Consola Customer Service** mostrando eventos con claves diferentes en el mismo tópico
2. **Consola Login Service** mostrando eventos de Login
3. **Consola Order Service** mostrando eventos de Order
4. **Código CustomerEventConsumer.java** con el switch de claves
5. **Código CustomerEventProducer.java** con envío de claves diferentes
6. **docker-compose.yaml** mostrando creación de tópicos únicos
7. **Logs de Kafka** mostrando los tópicos creados

## ✅ **CONCLUSIÓN**

**ANTES (Múltiples tópicos):**
- `addcustomer_events`
- `editcustomer_events`
- `findcustomerbyid_events`
- `findallcustomers_events`

**DESPUÉS (Tópico único):**
- `customer_events` (con claves: addCustomer, editCustomer, findCustomerById, findAllCustomers)
- `login_events` (con claves: addLogin, editLogin, findLoginById, findAllLogins)
- `order_events` (con claves: addOrder, editOrder, findOrderById, findAllOrders)

**🎯 RESULTADO**: Cada entidad usa un **único tópico** que maneja **TODOS** sus eventos, diferenciados por claves.
