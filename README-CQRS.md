# 🏗️ CQRS Architecture with Kafka

## 📋 Descripción del Proyecto

Este proyecto implementa **CQRS (Command Query Responsibility Segregation)** con **Apache Kafka** para separar las operaciones de escritura (Command) y lectura (Query) en microservicios independientes.

## 👥 Autores

- **Cristian Andrés Basto Largo**
- **Andrea Katherine Bello Sotelo** 
- **Laura Vanessa Figueredo Martinez**

## 🏗️ Arquitectura CQRS

### Command Side (CQRS Controller)
- **Puerto**: 11080
- **Base de datos**: MySQL (fuente de verdad)
- **Responsabilidad**: Manejar comandos (CREATE, UPDATE, DELETE)
- **Proyecto**: `cqrscontroller/`

### Query Side (CQRS Query)
- **Puerto**: 12080
- **Base de datos**: MongoDB (vistas optimizadas para lectura)
- **Responsabilidad**: Manejar consultas (READ)
- **Proyecto**: `cqrsquery/`

### Infraestructura
- **Apache Kafka**: Mensajería asíncrona entre Command y Query
- **MySQL**: Base de datos transaccional (Command Side)
- **MongoDB**: Base de datos de lectura (Query Side)
- **Docker Compose**: Orquestación de servicios

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.5**
- **Apache Kafka**
- **MySQL 8.0** (Command Side)
- **MongoDB** (Query Side)
- **Docker & Docker Compose**
- **Maven**

## 🚀 Instalación y Configuración

### 1. Iniciar Infraestructura

```bash
# Iniciar Kafka, MySQL y MongoDB
docker-compose -f docker-compose-cqrs.yml up -d
```

### 2. Compilar Proyectos

```bash
# Compilar CQRS Controller (Command Side)
cd cqrscontroller
mvn clean compile

# Compilar CQRS Query (Query Side)
cd cqrsquery
mvn clean compile
```

### 3. Ejecutar Microservicios

```bash
# Terminal 1 - CQRS Controller (Command Side)
cd cqrscontroller
mvn spring-boot:run

# Terminal 2 - CQRS Query (Query Side)
cd cqrsquery
mvn spring-boot:run
```

### 4. Script de Prueba Automática

```bash
# Ejecutar script completo de pruebas
test-cqrs.bat
```

## 📡 API Endpoints

### Command Side (CQRS Controller) - Puerto 11080

#### Customer Commands
```http
POST   /api/customers/add      # Crear cliente
PUT    /api/customers/update   # Actualizar cliente
DELETE /api/customers/delete   # Eliminar cliente
```

#### Login Commands
```http
POST   /api/logins/add         # Crear login
PUT    /api/logins/update      # Actualizar login
DELETE /api/logins/delete      # Eliminar login
```

#### Order Commands
```http
POST   /api/orders/add         # Crear pedido
PUT    /api/orders/update      # Actualizar pedido
DELETE /api/orders/delete      # Eliminar pedido
```

### Query Side (CQRS Query) - Puerto 12080

#### Customer Queries
```http
GET    /api/customers/all      # Obtener todos los clientes
GET    /api/customers/find/{id} # Obtener cliente por ID
```

#### Login Queries
```http
GET    /api/logins/all         # Obtener todos los logins
GET    /api/logins/find/{id}   # Obtener login por ID
```

#### Order Queries
```http
GET    /api/orders/all         # Obtener todos los pedidos
GET    /api/orders/find/{id}   # Obtener pedido por ID
```

## 🎯 Funcionalidades Implementadas

### ✅ Auto-creación de Login
- Al crear un Customer en el Command Side, se crea automáticamente un Login
- Contraseña generada automáticamente basada en el documento
- Eventos publicados a Kafka para sincronización con Query Side

### ✅ Separación CQRS
- **Command Side**: Maneja todas las operaciones de escritura en MySQL
- **Query Side**: Maneja todas las operaciones de lectura en MongoDB
- **Sincronización**: Via Kafka entre Command y Query sides

### ✅ Tópicos Kafka Específicos
- `add-customer-topic`, `update-customer-topic`, `delete-customer-topic`
- `add-login-topic`, `update-login-topic`, `delete-login-topic`
- `add-order-topic`, `update-order-topic`, `delete-order-topic`

## 🧪 Pruebas

### Prueba Manual

```bash
# 1. Crear Customer (Command Side)
curl -X POST http://localhost:11080/api/customers/add \
  -H "Content-Type: application/json" \
  -d '{
    "document": "19273",
    "firstname": "Laura",
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "pepito@c.com"
  }'

# 2. Verificar Customer en Query Side
curl -X GET http://localhost:12080/api/customers/all

# 3. Verificar Login auto-creado en Query Side
curl -X GET http://localhost:12080/api/logins/all
```

### Prueba Automática

```bash
# Ejecutar script completo
test-cqrs.bat
```

## 📊 Estructura del Proyecto

```
taller_kafka/
├── cqrscontroller/                 # Command Side (MySQL)
│   ├── src/main/java/edu/uptc/swii/cqrscontroller/
│   │   ├── model/                  # JPA Entities (MySQL)
│   │   ├── repository/             # JPA Repositories
│   │   ├── service/                # Business Logic + Event Producers
│   │   ├── controller/             # REST Controllers (Commands)
│   │   └── utils/                  # Utility Classes
│   └── pom.xml
├── cqrsquery/                      # Query Side (MongoDB)
│   ├── src/main/java/edu/uptc/swii/cqrsquery/
│   │   ├── model/                  # MongoDB Documents
│   │   ├── repository/             # MongoDB Repositories
│   │   ├── service/                # Business Logic + Event Consumers
│   │   ├── controller/             # REST Controllers (Queries)
│   │   └── utils/                  # Utility Classes
│   └── pom.xml
├── docker-compose-cqrs.yml         # Infrastructure
├── test-cqrs.bat                   # Test Script
└── README-CQRS.md                  # This file
```

## 🔧 Configuración de Base de Datos

### MySQL (Command Side)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customerorders?serverTimezone=GMT-5
spring.datasource.username=customerOrder
spring.datasource.password=corders123.
```

### MongoDB (Query Side)
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=CustomerOrderDB
spring.data.mongodb.username=admin
spring.data.mongodb.password=UPTC2024
```

### Kafka
```properties
spring.kafka.bootstrap-servers=localhost:29092
```

## 📈 Flujo de Datos CQRS

1. **Cliente** → Command Side (puerto 11080)
2. **Command Side** → MySQL (persistencia)
3. **Command Side** → Kafka (evento)
4. **Kafka** → Query Side (consumo)
5. **Query Side** → MongoDB (vista de lectura)
6. **Cliente** → Query Side (puerto 12080) para consultas

## 🚨 Solución de Problemas

### Error de Conexión a MySQL
```bash
# Verificar que MySQL esté ejecutándose
docker ps | grep mysql

# Verificar credenciales
mysql -u customerOrder -p customerorders
```

### Error de Conexión a MongoDB
```bash
# Verificar que MongoDB esté ejecutándose
docker ps | grep mongodb

# Conectar a MongoDB
mongo mongodb://admin:UPTC2024@localhost:27017/CustomerOrderDB
```

### Error de Conexión a Kafka
```bash
# Verificar que Kafka esté ejecutándose
docker ps | grep kafka

# Verificar tópicos creados
docker exec -it kafka-cqrs kafka-topics --list --bootstrap-server localhost:29092
```

## 📝 Notas de Desarrollo

- **CQRS**: Separación clara entre Command y Query sides
- **Event Sourcing**: Eventos publicados via Kafka
- **Auto-creación**: Login se crea automáticamente al crear Customer
- **Bases de datos separadas**: MySQL para Command, MongoDB para Query
- **Sincronización**: Asíncrona via Kafka

## 🔄 Configuración para Máquinas Diferentes

### Para ejecutar en máquinas separadas:

#### Máquina 1 (Command Side)
```properties
# En cqrscontroller/src/main/resources/application.properties
spring.kafka.bootstrap-servers=IP_MAQUINA_KAFKA:29092
```

#### Máquina 2 (Query Side)
```properties
# En cqrsquery/src/main/resources/application.properties
spring.kafka.bootstrap-servers=IP_MAQUINA_KAFKA:29092
spring.data.mongodb.host=IP_MAQUINA_MONGODB
```

#### Máquina 3 (Infraestructura)
```bash
# Ejecutar solo los servicios de infraestructura
docker-compose -f docker-compose-cqrs.yml up kafka zookeeper mysql mongodb
```

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez
