# 🏗️ CQRS Command Side

## 📋 Descripción
Este repositorio contiene los **3 microservicios del Command Side** del patrón CQRS. Estos microservicios son responsables de las **operaciones de escritura** y utilizan **MySQL** como base de datos principal.

## 🏛️ Arquitectura Command Side

### Microservicios Implementados

1. **customer-command** (Puerto 8080)
   - Gestión de operaciones de escritura de Customer
   - Base de datos: MySQL
   - Auto-creación de Login al crear Customer
   - Event Producer: Publica eventos a Kafka

2. **login-command** (Puerto 8081)
   - Gestión de operaciones de escritura de Login
   - Base de datos: MySQL
   - Event Producer: Publica eventos a Kafka

3. **order-command** (Puerto 8082)
   - Gestión de operaciones de escritura de Order
   - Base de datos: MySQL
   - Event Producer: Publica eventos a Kafka

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL 8.0**
- **Apache Kafka**
- **Maven**

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0
- Apache Kafka

### 1. Configurar Base de Datos

```sql
CREATE DATABASE cqrs_commands;
CREATE USER 'cqrsCommand'@'localhost' IDENTIFIED BY 'cqrs123.';
GRANT ALL PRIVILEGES ON cqrs_commands.* TO 'cqrsCommand'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Iniciar Kafka

```bash
# Iniciar Kafka (usar docker-compose del proyecto principal)
docker-compose up -d kafka zookeeper mysql
```

### 3. Compilar Microservicios

```bash
# Compilar todos los microservicios
cd customer-command && mvn clean compile
cd ../login-command && mvn clean compile
cd ../order-command && mvn clean compile
```

### 4. Ejecutar Microservicios

```bash
# Terminal 1 - Customer Command
cd customer-command && mvn spring-boot:run

# Terminal 2 - Login Command
cd login-command && mvn spring-boot:run

# Terminal 3 - Order Command
cd order-command && mvn spring-boot:run
```

## 📡 API Endpoints

### Customer Command (Puerto 8080)

```http
POST   /api/customers          # Crear cliente
PUT    /api/customers          # Actualizar cliente
DELETE /api/customers/{id}     # Eliminar cliente
GET    /api/customers/{id}     # Obtener cliente por ID
GET    /api/customers          # Obtener todos los clientes
```

### Login Command (Puerto 8081)

```http
POST   /api/logins             # Crear login
PUT    /api/logins             # Actualizar login
DELETE /api/logins/{id}        # Eliminar login
GET    /api/logins/{id}        # Obtener login por ID
GET    /api/logins             # Obtener todos los logins
```

### Order Command (Puerto 8082)

```http
POST   /api/orders             # Crear pedido
PUT    /api/orders             # Actualizar pedido
DELETE /api/orders/{id}        # Eliminar pedido
GET    /api/orders/{id}        # Obtener pedido por ID
GET    /api/orders             # Obtener todos los pedidos
```

## 🎯 Funcionalidades Implementadas

### Auto-creación de Login
- Al crear un Customer, se crea automáticamente un Login
- Contraseña generada automáticamente basada en el documento
- Evento publicado a Kafka para sincronización con Query Side

### Event-Driven Architecture
- **Tópicos Kafka**: `customer_events`, `login_events`, `order_events`
- **Claves de eventos**: `addCustomer`, `editCustomer`, `deleteCustomer`, etc.
- **Serialización**: JSON automática

## 📊 Estructura del Proyecto

```
command-side/
├── customer-command/          # Customer Command Microservice
│   ├── src/main/java/
│   │   └── co/edu/uptc/customercommand/
│   │       ├── controller/    # REST Controllers
│   │       ├── model/         # JPA Entities
│   │       ├── repository/    # JPA Repositories
│   │       ├── service/       # Business Logic + Event Producers
│   │       └── utils/         # Utility Classes
│   └── pom.xml
├── login-command/             # Login Command Microservice
├── order-command/             # Order Command Microservice
└── README.md                  # This file
```

## 🔧 Configuración de Base de Datos

### Propiedades de Conexión

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cqrs_commands?serverTimezone=GMT-5
spring.datasource.username=cqrsCommand
spring.datasource.password=cqrs123.
```

### Configuración de Kafka

```properties
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

## 🧪 Pruebas

### Ejemplo de Prueba Manual

```bash
# Crear Customer (debe crear Login automáticamente)
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "document": "12345",
    "firstname": "Laura",
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "laura@test.com"
  }'

# Crear Order
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "12345",
    "productName": "Laptop",
    "quantity": 1,
    "price": 1500.0,
    "orderDate": "2024-01-15T10:00:00",
    "status": "PENDING"
  }'
```

## 📝 Notas de Desarrollo

- **Patrón CQRS**: Solo operaciones de escritura
- **Base de datos**: MySQL como fuente de verdad
- **Eventos**: Publicación automática a Kafka
- **Auto-creación**: Login se crea automáticamente al crear Customer
- **Separación**: Cada entidad tiene su propio microservicio

## 🔄 Flujo de Datos

1. **Cliente** → Microservicio Command
2. **Microservicio** → MySQL (persistencia)
3. **Microservicio** → Kafka (evento)
4. **Kafka** → Query Side (sincronización)

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez
