# 🏗️ CQRS Architecture - Event-Driven Microservices

## 📋 Descripción del Proyecto

Este proyecto implementa una **Arquitectura CQRS (Command Query Responsibility Segregation)** completa con **6 microservicios** separados, utilizando **Apache Kafka** para la comunicación entre el Command Side y Query Side.

## 👥 Autores

- **Cristian Andrés Basto Largo**
- **Andrea Katherine Bello Sotelo** 
- **Laura Vanessa Figueredo Martinez**

## 🏛️ Arquitectura General

### Separación CQRS

El proyecto está organizado en **dos lados principales**:

#### **Command Side** (`command-side/`)
- **Responsabilidad**: Operaciones de escritura (POST, PUT, DELETE)
- **Base de datos**: MySQL (fuente de verdad)
- **Tecnología**: Spring Boot + JPA + Event Producers
- **Microservicios**: 3 (customer-command, login-command, order-command)

#### **Query Side** (`query-side/`)
- **Responsabilidad**: Operaciones de lectura (GET)
- **Base de datos**: MongoDB (optimizada para consultas)
- **Tecnología**: Spring Boot + MongoDB + Event Consumers
- **Microservicios**: 3 (customer-query, login-query, order-query)

## 🚀 Microservicios Implementados

### Command Side (Puertos 8080-8082)

| Microservicio | Puerto | Base de Datos | Responsabilidad |
|---------------|--------|---------------|-----------------|
| **customer-command** | 8080 | MySQL | Escritura de Customer + Auto-creación de Login |
| **login-command** | 8081 | MySQL | Escritura de Login |
| **order-command** | 8082 | MySQL | Escritura de Order |

### Query Side (Puertos 8083-8085)

| Microservicio | Puerto | Base de Datos | Responsabilidad |
|---------------|--------|---------------|-----------------|
| **customer-query** | 8083 | MongoDB | Lectura de Customer |
| **login-query** | 8084 | MongoDB | Lectura de Login |
| **order-query** | 8085 | MongoDB | Lectura de Order |

### API Gateway (Puerto 8086)

- **Ruteo inteligente**: Commands → Command Side, Queries → Query Side
- **Separación clara**: POST/PUT/DELETE → Command Side, GET → Query Side

## 🛠️ Stack Tecnológico

- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Spring Data JPA** (Command Side)
- **Spring Data MongoDB** (Query Side)
- **Apache Kafka** (Event Communication)
- **MySQL 8.0** (Command Database)
- **MongoDB 7.0** (Query Database)
- **Docker & Docker Compose** (Infrastructure)
- **Maven** (Build Tool)

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose
- MySQL 8.0
- MongoDB 7.0

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd EDAKafka
```

### 2. Configurar Base de Datos

```sql
-- MySQL para Command Side
CREATE DATABASE cqrs_commands;
CREATE USER 'cqrsCommand'@'localhost' IDENTIFIED BY 'cqrs123.';
GRANT ALL PRIVILEGES ON cqrs_commands.* TO 'cqrsCommand'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Iniciar Infraestructura

```bash
# Iniciar Kafka, MySQL y MongoDB
docker-compose up -d
```

### 4. Compilar y Ejecutar

#### Command Side

```bash
cd command-side

# Compilar todos los microservicios
cd customer-command && mvn clean compile
cd ../login-command && mvn clean compile
cd ../order-command && mvn clean compile

# Ejecutar microservicios
cd customer-command && mvn spring-boot:run &
cd ../login-command && mvn spring-boot:run &
cd ../order-command && mvn spring-boot:run &
```

#### Query Side

```bash
cd query-side

# Compilar todos los microservicios
cd customer-query && mvn clean compile
cd ../login-query && mvn clean compile
cd ../order-query && mvn clean compile

# Ejecutar microservicios
cd customer-query && mvn spring-boot:run &
cd ../login-query && mvn spring-boot:run &
cd ../order-query && mvn spring-boot:run &
```

#### API Gateway

```bash
cd cqrs-gateway
mvn clean compile
mvn spring-boot:run
```

### 5. Script de Prueba Automática

```bash
# Ejecutar script completo de pruebas
test-cqrs-6-microservices.bat
```

## 📡 API Endpoints

### Command Side (Escritura)

```http
# Customer Command (Puerto 8080)
POST   /api/customers          # Crear cliente
PUT    /api/customers          # Actualizar cliente
DELETE /api/customers/{id}     # Eliminar cliente

# Login Command (Puerto 8081)
POST   /api/logins             # Crear login
PUT    /api/logins             # Actualizar login
DELETE /api/logins/{id}        # Eliminar login

# Order Command (Puerto 8082)
POST   /api/orders             # Crear pedido
PUT    /api/orders             # Actualizar pedido
DELETE /api/orders/{id}        # Eliminar pedido
```

### Query Side (Lectura)

```http
# Customer Query (Puerto 8083)
GET    /api/customers/{id}     # Obtener cliente por ID
GET    /api/customers          # Obtener todos los clientes

# Login Query (Puerto 8084)
GET    /api/logins/{id}        # Obtener login por ID
GET    /api/logins             # Obtener todos los logins

# Order Query (Puerto 8085)
GET    /api/orders/{id}        # Obtener pedido por ID
GET    /api/orders             # Obtener todos los pedidos
```

### API Gateway (Puerto 8086)

```http
# Todas las rutas anteriores accesibles a través del Gateway
# Ruteo automático: Commands → Command Side, Queries → Query Side
```

## 🎯 Funcionalidades Implementadas

### Auto-creación de Login
- Al crear un Customer en Command Side → se crea automáticamente un Login
- Contraseña generada automáticamente basada en el documento
- Evento publicado a Kafka para sincronización con Query Side

### Event-Driven Architecture
- **Tópicos Kafka**: `customer_events`, `login_events`, `order_events`
- **Claves de eventos**: `addCustomer`, `editCustomer`, `deleteCustomer`, etc.
- **Serialización**: JSON automática

### Separación CQRS
- **Command Side**: Solo operaciones de escritura
- **Query Side**: Solo operaciones de lectura
- **Bases de datos**: MySQL (Command) + MongoDB (Query)
- **Sincronización**: Automática via Kafka

## 📊 Estructura del Proyecto

```
EDAKafka/
├── command-side/              # Command Side (Escritura)
│   ├── customer-command/      # Customer Command Microservice
│   ├── login-command/         # Login Command Microservice
│   ├── order-command/         # Order Command Microservice
│   └── README.md              # Command Side Documentation
├── query-side/                # Query Side (Lectura)
│   ├── customer-query/        # Customer Query Microservice
│   ├── login-query/           # Login Query Microservice
│   ├── order-query/           # Order Query Microservice
│   └── README.md              # Query Side Documentation
├── cqrs-gateway/              # API Gateway
├── docker-compose.yaml        # Infrastructure
├── test-cqrs-6-microservices.bat  # Test Script
└── README-CQRS.md             # This file
```

## 🧪 Pruebas

### Script de Prueba Completo

```bash
test-cqrs-6-microservices.bat
```

### Pruebas Manuales

```bash
# Crear Customer (auto-crea Login)
curl -X POST http://localhost:8086/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "document": "12345",
    "firstname": "Laura",
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "laura@test.com"
  }'

# Leer desde Query Side (MongoDB)
curl -X GET http://localhost:8086/api/customers
curl -X GET http://localhost:8086/api/logins
```

## 🔧 Configuración de Base de Datos

### Command Side (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cqrs_commands?serverTimezone=GMT-5
spring.datasource.username=cqrsCommand
spring.datasource.password=cqrs123.
```

### Query Side (MongoDB)

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/cqrs_queries
spring.data.mongodb.database=cqrs_queries
```

### Kafka

```properties
spring.kafka.bootstrap-servers=localhost:29092
```

## 📈 Ventajas de la Arquitectura CQRS

### Separación de Responsabilidades
- **Command Side**: Optimizado para escritura
- **Query Side**: Optimizado para lectura
- **Escalabilidad**: Independiente por lado

### Rendimiento
- **MySQL**: Transaccional para escritura
- **MongoDB**: Optimizada para consultas
- **Kafka**: Comunicación asíncrona

### Flexibilidad
- **Bases de datos**: Diferentes tecnologías por lado
- **Estructura**: Optimizada para cada caso de uso
- **Despliegue**: Independiente por microservicio

## 🔄 Flujo de Datos

1. **Cliente** → API Gateway (Puerto 8086)
2. **API Gateway** → Command Side (Puerto 8080-8082)
3. **Command Side** → MySQL (Persistencia)
4. **Command Side** → Kafka (Evento)
5. **Kafka** → Query Side (Puerto 8083-8085)
6. **Query Side** → MongoDB (Sincronización)
7. **Cliente** → API Gateway → Query Side (Consulta)

## 📝 Notas de Desarrollo

- **Patrón CQRS**: Implementación completa con separación clara
- **Microservicios**: 6 microservicios independientes
- **Event-Driven**: Comunicación asíncrona via Kafka
- **Auto-creación**: Login se crea automáticamente al crear Customer
- **Escalabilidad**: Cada microservicio puede escalarse independientemente

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos
```bash
# Verificar que MySQL y MongoDB estén ejecutándose
docker ps | grep mysql
docker ps | grep mongo
```

### Error de Conexión a Kafka
```bash
# Verificar que Kafka esté ejecutándose
docker ps | grep kafka
```

### Puerto en Uso
```bash
# Verificar puertos ocupados
netstat -ano | findstr :8080
netstat -ano | findstr :8081
# ... etc para todos los puertos
```

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez
