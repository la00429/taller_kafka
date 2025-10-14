# 🚀 CQRS Microservices with Kafka

## 📋 Descripción del Proyecto

Este proyecto implementa el patrón arquitectónico **CQRS (Command Query Responsibility Segregation)** utilizando **Event-Driven Architecture (EDA)** con **Apache Kafka** como message broker. La solución separa las operaciones de escritura (Command Side) de las operaciones de lectura (Query Side), utilizando **MySQL** para persistencia de comandos y **MongoDB** para consultas optimizadas. El sistema incluye microservicios para las entidades **Customer**, **Login** y **Order**, con auto-creación de credenciales de login al registrar nuevos clientes, garantizando sincronización de datos a través de eventos de Kafka entre ambas capas.

## 👥 Autores

- **Cristian Andrés Basto Largo**
- **Andrea Katherine Bello Sotelo** 
- **Laura Vanessa Figueredo Martinez**

## 🏗️ Arquitectura CQRS

### Command Side (Escritura) - MySQL

1. **Customer Command** (Puerto 8080)
   - Operaciones de escritura (POST, PUT, DELETE)
   - Persistencia en MySQL
   - Envío de eventos a Kafka

2. **Login Command** (Puerto 8081)
   - Operaciones de escritura (POST, PUT, DELETE)
   - Persistencia en MySQL
   - Envío de eventos a Kafka

3. **Order Command** (Puerto 8082)
   - Operaciones de escritura (POST, PUT, DELETE)
   - Persistencia en MySQL
   - Envío de eventos a Kafka

### Query Side (Lectura) - MongoDB

4. **Customer Query** (Puerto 8083)
   - Operaciones de lectura (GET)
   - Persistencia en MongoDB
   - Consumo de eventos de Kafka

5. **Login Query** (Puerto 8084)
   - Operaciones de lectura (GET)
   - Persistencia en MongoDB
   - Consumo de eventos de Kafka

6. **Order Query** (Puerto 8085)
   - Operaciones de lectura (GET)
   - Persistencia en MongoDB
   - Consumo de eventos de Kafka

7. **API Gateway** (Puerto 8086)
   - Punto de entrada único
   - Enrutamiento automático Command/Query
   - CORS configurado

### Infraestructura

- **Apache Kafka**: Mensajería asíncrona entre Command y Query
- **MySQL**: Base de datos Command Side (escritura)
- **MongoDB**: Base de datos Query Side (lectura)
- **Docker Compose**: Orquestación de servicios

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Apache Kafka**
- **MySQL 8.0** (Command Side)
- **MongoDB 7.0** (Query Side)
- **Docker & Docker Compose**
- **Maven**

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose
- MySQL (opcional, se incluye en Docker)

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd EDAKafka
```

### 2. Configurar Bases de Datos

#### MySQL (Command Side)
```sql
CREATE DATABASE cqrs_commands;
CREATE USER 'cqrsCommand'@'localhost' IDENTIFIED BY 'cqrs123.';
GRANT ALL PRIVILEGES ON cqrs_commands.* TO 'cqrsCommand'@'localhost';
FLUSH PRIVILEGES;
```

#### MongoDB (Query Side)
```javascript
// MongoDB se inicializa automáticamente con Docker
// Base de datos: cqrs_queries
// Colecciones: customers, logins, orders
```

### 3. Iniciar Infraestructura

```bash
# Iniciar Kafka, MySQL y MongoDB
docker-compose up -d
```

### 4. Compilar Microservicios

```bash
# Compilar Command Side
mvn clean compile -f command-side/customer-command/pom.xml
mvn clean compile -f command-side/login-command/pom.xml
mvn clean compile -f command-side/order-command/pom.xml

# Compilar Query Side
mvn clean compile -f query-side/customer-query/pom.xml
mvn clean compile -f query-side/login-query/pom.xml
mvn clean compile -f query-side/order-query/pom.xml

# Compilar API Gateway
mvn clean compile -f cqrs-gateway/pom.xml
```

### 5. Ejecutar Microservicios

```bash
# Command Side
cd command-side/customer-command && mvn spring-boot:run
cd command-side/login-command && mvn spring-boot:run
cd command-side/order-command && mvn spring-boot:run

# Query Side
cd query-side/customer-query && mvn spring-boot:run
cd query-side/login-query && mvn spring-boot:run
cd query-side/order-query && mvn spring-boot:run

# API Gateway
cd cqrs-gateway && mvn spring-boot:run
```

### 6. Pruebas del Sistema

```bash
# Opción 1: Pruebas con curl (ver ejemplos abajo)
# Opción 2: Pruebas con Postman (ver POSTMAN-INSTRUCTIONS.md)
```

## 📡 API Endpoints

### Command Side (Escritura)

#### Customer Command (Puerto 8080)
```http
POST   /api/customers          # Crear cliente
PUT    /api/customers          # Actualizar cliente
DELETE /api/customers/{id}     # Eliminar cliente
```

#### Login Command (Puerto 8081)
```http
POST   /api/logins             # Crear login
PUT    /api/logins             # Actualizar login
DELETE /api/logins/{id}        # Eliminar login
```

#### Order Command (Puerto 8082)
```http
POST   /api/orders             # Crear pedido
PUT    /api/orders             # Actualizar pedido
DELETE /api/orders/{id}        # Eliminar pedido
```

### Query Side (Lectura)

#### Customer Query (Puerto 8083)
```http
GET    /api/customers/{id}     # Obtener cliente por ID
GET    /api/customers          # Obtener todos los clientes
```

#### Login Query (Puerto 8084)
```http
GET    /api/logins/{id}        # Obtener login por ID
GET    /api/logins             # Obtener todos los logins
```

#### Order Query (Puerto 8085)
```http
GET    /api/orders/{id}        # Obtener pedido por ID
GET    /api/orders             # Obtener todos los pedidos
```

### API Gateway (Puerto 8086)

```http
# Enrutamiento automático Command/Query
POST   /api/customers          # → Customer Command (8080)
GET    /api/customers          # → Customer Query (8083)
POST   /api/logins             # → Login Command (8081)
GET    /api/logins             # → Login Query (8084)
POST   /api/orders             # → Order Command (8082)
GET    /api/orders             # → Order Query (8085)
```

## 🎯 Funcionalidades Implementadas

### 1. Separación Command/Query (CQRS)
- **Command Side**: Operaciones de escritura en MySQL
- **Query Side**: Operaciones de lectura en MongoDB
- **Sincronización**: Eventos de Kafka entre ambas capas

### 2. Auto-creación de Login
- Al crear un Customer, se crea automáticamente un Login
- Contraseña generada automáticamente
- Comunicación asíncrona via Kafka

### 3. Tópicos Unificados
- Un solo tópico por entidad de negocio
- Claves para diferenciar tipos de eventos:
  - `customer_events`: addCustomer, editCustomer, deleteCustomer
  - `login_events`: addLogin, editLogin, deleteLogin
  - `order_events`: addOrder, editOrder, deleteOrder

### 4. API Gateway Inteligente
- Punto de entrada único (puerto 8086)
- Enrutamiento automático Command/Query según método HTTP
- CORS configurado globalmente
- Logging detallado

## 🧪 Pruebas

### Pruebas Disponibles

1. **Pruebas con curl**: Ejemplos de comandos curl (ver abajo)
2. **Pruebas con Postman**: Colección completa en `CQRS-Postman-Tests.json`
3. **Instrucciones detalladas**: Ver `POSTMAN-INSTRUCTIONS.md`
4. **Pruebas de Integración**: Verificar sincronización Command/Query
5. **Pruebas de Auto-creación**: Verificar creación automática de Login

### Ejemplo de Prueba Manual

```bash
# Crear Customer (debe crear Login automáticamente)
curl -X POST http://localhost:8086/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "document": "19273",
    "firstname": "Laura",
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "laura@test.com"
  }'

# Verificar Customer en Query Side
curl -X GET http://localhost:8086/api/customers/19273

# Verificar Login creado automáticamente
curl -X GET http://localhost:8086/api/logins

# Obtener todos los customers
curl -X GET http://localhost:8086/api/customers
```

## 📊 Estructura del Proyecto

```
EDAKafka/
├── command-side/                  # Command Side (Escritura)
│   ├── customer-command/          # Customer Command Service
│   ├── login-command/             # Login Command Service
│   └── order-command/             # Order Command Service
├── query-side/                    # Query Side (Lectura)
│   ├── customer-query/            # Customer Query Service
│   ├── login-query/               # Login Query Service
│   └── order-query/               # Order Query Service
├── cqrs-gateway/                  # API Gateway
├── docker-compose.yaml            # Infrastructure (Kafka, MySQL, MongoDB)
├── mongo-init.js                  # MongoDB Initialization
├── CQRS-Postman-Tests.json       # Colección de pruebas Postman
├── POSTMAN-INSTRUCTIONS.md        # Instrucciones de pruebas
├── RESUMEN-PROYECTO.md            # Resumen del proyecto
└── README.md                      # This file
```

## 🔧 Configuración de Bases de Datos

### MySQL (Command Side)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cqrs_commands?serverTimezone=GMT-5
spring.datasource.username=cqrsCommand
spring.datasource.password=cqrs123.
```

### MongoDB (Query Side)

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/cqrs_queries
spring.data.mongodb.database=cqrs_queries
```

### Configuración de Kafka

```properties
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

## 📈 Monitoreo y Logs

### Logs de Kafka
- Tópicos creados automáticamente via Docker Compose
- Logs de eventos en consolas de microservicios
- Claves de eventos para diferenciación

### Logs de Gateway
- Enrutamiento de requests
- CORS handling
- Performance metrics

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos
```bash
# Verificar que MySQL esté ejecutándose
docker ps | grep mysql

# Verificar credenciales
mysql -u customerOrder -p customerorders
```

### Error de Conexión a Kafka
```bash
# Verificar que Kafka esté ejecutándose
docker ps | grep kafka

# Verificar tópicos creados
docker exec -it edakafka_kafka_1 kafka-topics --list --bootstrap-server localhost:29092
```

### Puerto en Uso
```bash
# Verificar puertos ocupados
netstat -ano | findstr :8080
netstat -ano | findstr :8081
netstat -ano | findstr :8082
netstat -ano | findstr :8083
```

## 📝 Notas de Desarrollo

- **Patrón EDA**: Comunicación asíncrona entre microservicios
- **Tópicos Unificados**: Un tópico por entidad con claves diferenciadas
- **Auto-creación**: Login se crea automáticamente al crear Customer
- **API Gateway**: Punto de entrada único con enrutamiento automático
- **CORS**: Configurado globalmente para desarrollo

## 🔄 Flujo de Datos CQRS

1. **Cliente** → API Gateway (puerto 8086)
2. **API Gateway** → Command Side (escritura) o Query Side (lectura)
3. **Command Side** → MySQL (persistencia) + Kafka (evento)
4. **Kafka** → Query Side (consumo de eventos)
5. **Query Side** → MongoDB (persistencia optimizada para lectura)
6. **Query Side** → Cliente (respuesta de consulta)

## 📚 Documentación Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez