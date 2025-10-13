# 🔍 CQRS Query Side

## 📋 Descripción
Este repositorio contiene los **3 microservicios del Query Side** del patrón CQRS. Estos microservicios son responsables de las **operaciones de lectura** y utilizan **MongoDB** como base de datos optimizada para consultas.

## 🏛️ Arquitectura Query Side

### Microservicios Implementados

1. **customer-query** (Puerto 8083)
   - Gestión de operaciones de lectura de Customer
   - Base de datos: MongoDB
   - Event Consumer: Consume eventos de Kafka
   - Sincronización automática con Command Side

2. **login-query** (Puerto 8084)
   - Gestión de operaciones de lectura de Login
   - Base de datos: MongoDB
   - Event Consumer: Consume eventos de Kafka
   - Sincronización automática con Command Side

3. **order-query** (Puerto 8085)
   - Gestión de operaciones de lectura de Order
   - Base de datos: MongoDB
   - Event Consumer: Consume eventos de Kafka
   - Sincronización automática con Command Side

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data MongoDB**
- **MongoDB 7.0**
- **Apache Kafka**
- **Maven**

## 🚀 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- MongoDB 7.0
- Apache Kafka

### 1. Configurar Base de Datos

```bash
# Iniciar MongoDB (usar docker-compose del proyecto principal)
docker-compose up -d mongodb
```

### 2. Iniciar Kafka

```bash
# Iniciar Kafka (usar docker-compose del proyecto principal)
docker-compose up -d kafka zookeeper
```

### 3. Compilar Microservicios

```bash
# Compilar todos los microservicios
cd customer-query && mvn clean compile
cd ../login-query && mvn clean compile
cd ../order-query && mvn clean compile
```

### 4. Ejecutar Microservicios

```bash
# Terminal 1 - Customer Query
cd customer-query && mvn spring-boot:run

# Terminal 2 - Login Query
cd login-query && mvn spring-boot:run

# Terminal 3 - Order Query
cd order-query && mvn spring-boot:run
```

## 📡 API Endpoints

### Customer Query (Puerto 8083)

```http
GET    /api/customers/{id}     # Obtener cliente por ID
GET    /api/customers          # Obtener todos los clientes
```

### Login Query (Puerto 8084)

```http
GET    /api/logins/{id}        # Obtener login por ID
GET    /api/logins             # Obtener todos los logins
```

### Order Query (Puerto 8085)

```http
GET    /api/orders/{id}        # Obtener pedido por ID
GET    /api/orders             # Obtener todos los pedidos
```

## 🎯 Funcionalidades Implementadas

### Sincronización Automática
- Consume eventos de Kafka del Command Side
- Sincroniza automáticamente con MongoDB
- Mantiene datos actualizados en tiempo real

### Event-Driven Architecture
- **Tópicos Kafka**: `customer_events`, `login_events`, `order_events`
- **Event Consumers**: `@KafkaListener` para cada tópico
- **Deserialización**: JSON automática

### Auto-creación de Login
- Detecta eventos de creación de Customer
- Crea automáticamente Login en MongoDB
- Mantiene sincronización con Command Side

## 📊 Estructura del Proyecto

```
query-side/
├── customer-query/            # Customer Query Microservice
│   ├── src/main/java/
│   │   └── co/edu/uptc/customerquery/
│   │       ├── controller/    # REST Controllers (Read Only)
│   │       ├── model/         # MongoDB Documents
│   │       ├── repository/    # Mongo Repositories
│   │       ├── service/       # Event Consumers
│   │       └── utils/         # Utility Classes
│   └── pom.xml
├── login-query/               # Login Query Microservice
├── order-query/               # Order Query Microservice
└── README.md                  # This file
```

## 🔧 Configuración de Base de Datos

### Propiedades de Conexión

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/cqrs_queries
spring.data.mongodb.database=cqrs_queries
```

### Configuración de Kafka

```properties
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.group-id=customer_query_group
```

## 🧪 Pruebas

### Ejemplo de Prueba Manual

```bash
# Leer Customers desde MongoDB
curl -X GET http://localhost:8083/api/customers

# Leer Logins desde MongoDB (incluye auto-creados)
curl -X GET http://localhost:8084/api/logins

# Leer Orders desde MongoDB
curl -X GET http://localhost:8085/api/orders
```

## 📝 Notas de Desarrollo

- **Patrón CQRS**: Solo operaciones de lectura
- **Base de datos**: MongoDB optimizada para consultas
- **Eventos**: Consumo automático de Kafka
- **Sincronización**: Automática con Command Side
- **Separación**: Cada entidad tiene su propio microservicio

## 🔄 Flujo de Datos

1. **Command Side** → Kafka (evento)
2. **Kafka** → Query Side (consumo)
3. **Query Side** → MongoDB (sincronización)
4. **Cliente** → Query Side (consulta)

## 📈 Ventajas del Query Side

- **Rendimiento**: MongoDB optimizada para consultas
- **Escalabilidad**: Independiente del Command Side
- **Flexibilidad**: Estructura de datos optimizada para lectura
- **Tiempo real**: Sincronización automática via Kafka

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez
