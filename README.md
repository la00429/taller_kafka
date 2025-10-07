# 🚀 EDA Microservices with Kafka

## 📋 Descripción del Proyecto

Este proyecto implementa una **Arquitectura Orientada a Eventos (EDA)** utilizando **Apache Kafka** y **Spring Boot** para gestionar microservicios de Customer, Login y Order. Incluye un **API Gateway** para centralizar el acceso a todos los microservicios.

## 👥 Autores

- **Cristian Andrés Basto Largo**
- **Andrea Katherine Bello Sotelo** 
- **Laura Vanessa Figueredo Martinez**

## 🏗️ Arquitectura

### Microservicios Implementados

1. **Customer Service** (Puerto 8080)
   - Gestión de clientes
   - Auto-creación de Login al crear Customer
   - Tópico unificado: `customer_events`

2. **Login Service** (Puerto 8081)
   - Gestión de autenticación
   - Tópico unificado: `login_events`

3. **Order Service** (Puerto 8082)
   - Gestión de pedidos
   - Tópico unificado: `order_events`

4. **API Gateway** (Puerto 8083)
   - Punto de entrada único
   - Enrutamiento a microservicios
   - CORS configurado

### Infraestructura

- **Apache Kafka**: Mensajería asíncrona
- **MySQL**: Base de datos relacional
- **Docker Compose**: Orquestación de servicios
- **Spring Cloud Gateway**: API Gateway moderno

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Apache Kafka**
- **MySQL 8.0**
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

### 2. Configurar Base de Datos

```sql
CREATE DATABASE customerorders;
CREATE USER 'customerOrder'@'localhost' IDENTIFIED BY 'corders123.';
GRANT ALL PRIVILEGES ON customerorders.* TO 'customerOrder'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Iniciar Infraestructura

```bash
# Iniciar Kafka y MySQL
docker-compose up -d
```

### 4. Compilar Microservicios

```bash
# Compilar todos los microservicios
mvn clean compile -f edamicrokafka/pom.xml
mvn clean compile -f edamicrokafka-login/pom.xml
mvn clean compile -f edamicrokafka-order/pom.xml
mvn clean compile -f edamicrokafka-gateway/pom.xml
```

### 5. Ejecutar Microservicios

```bash
# Terminal 1 - Customer Service
cd edamicrokafka && mvn spring-boot:run

# Terminal 2 - Login Service
cd edamicrokafka-login && mvn spring-boot:run

# Terminal 3 - Order Service
cd edamicrokafka-order && mvn spring-boot:run

# Terminal 4 - API Gateway
cd edamicrokafka-gateway && mvn spring-boot:run
```

### 6. Script de Prueba Automática

```bash
# Ejecutar script completo de pruebas
test-completo.bat
```

## 📡 API Endpoints

### Customer Service (Puerto 8080)

```http
POST   /api/customers          # Crear cliente
PUT    /api/customers          # Actualizar cliente
GET    /api/customers/{id}     # Obtener cliente por ID
GET    /api/customers          # Obtener todos los clientes
```

### Login Service (Puerto 8081)

```http
POST   /api/logins             # Crear login
PUT    /api/logins             # Actualizar login
GET    /api/logins/{id}        # Obtener login por ID
GET    /api/logins             # Obtener todos los logins
```

### Order Service (Puerto 8082)

```http
POST   /api/orders             # Crear pedido
PUT    /api/orders             # Actualizar pedido
GET    /api/orders/{id}        # Obtener pedido por ID
GET    /api/orders             # Obtener todos los pedidos
```

### API Gateway (Puerto 8083)

```http
# Todas las rutas anteriores accesibles a través del Gateway
GET    /api/customers          # → Customer Service
GET    /api/logins             # → Login Service
GET    /api/orders             # → Order Service
```

## 🎯 Funcionalidades Implementadas

### Punto 3: Auto-creación de Login
- Al crear un Customer, se crea automáticamente un Login
- Contraseña generada automáticamente basada en el documento
- Comunicación asíncrona via Kafka

### Punto 4: Tópicos Unificados
- Un solo tópico por entidad de negocio
- Claves para diferenciar tipos de eventos:
  - `customer_events`: addCustomer, editCustomer, findCustomerById, findAllCustomers
  - `login_events`: addLogin, editLogin, findLoginById, findAllLogins
  - `order_events`: addOrder, editOrder, findOrderById, findAllOrders

### Punto 5: API Gateway
- Punto de entrada único (puerto 8083)
- Enrutamiento automático a microservicios
- CORS configurado globalmente
- Logging detallado

## 🧪 Pruebas

### Scripts de Prueba Disponibles

1. **test-completo.bat**: Prueba completa de todos los microservicios
2. **evidencia-topico-unico.bat**: Prueba específica de tópicos unificados
3. **evidencia-netflix-zuul.bat**: Prueba específica del API Gateway

### Ejemplo de Prueba Manual

```bash
# Crear Customer (debe crear Login automáticamente)
curl -X POST http://localhost:8083/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "document": "19273",
    "firstname": "Laura",
    "lastname": "Perez",
    "address": "Norte",
    "phone": "5123452",
    "email": "pepito@c.com"
  }'

# Verificar Login creado automáticamente
curl -X GET http://localhost:8083/api/logins

# Obtener todos los customers
curl -X GET http://localhost:8083/api/customers
```

## 📊 Estructura del Proyecto

```
EDAKafka/
├── edamicrokafka/                 # Customer Service
│   ├── src/main/java/
│   │   └── co/edu/uptc/edamicrokafka/
│   │       ├── controller/        # REST Controllers
│   │       ├── model/             # JPA Entities
│   │       ├── repository/        # Data Repositories
│   │       ├── service/           # Business Logic
│   │       └── utils/             # Utility Classes
│   └── pom.xml
├── edamicrokafka-login/           # Login Service
├── edamicrokafka-order/           # Order Service
├── edamicrokafka-gateway/         # API Gateway
├── docker-compose.yaml            # Infrastructure
├── test-completo.bat              # Test Script
└── README.md                      # This file
```

## 🔧 Configuración de Base de Datos

### Propiedades de Conexión

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customerorders?serverTimezone=GMT-5
spring.datasource.username=customerOrder
spring.datasource.password=corders123.
```

### Configuración de Kafka

```properties
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
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

## 🔄 Flujo de Datos

1. **Cliente** → API Gateway (puerto 8083)
2. **API Gateway** → Microservicio específico
3. **Microservicio** → Base de datos (persistencia)
4. **Microservicio** → Kafka (evento)
5. **Kafka** → Otros microservicios (notificación)

## 📚 Documentación Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

---

**Desarrollado por:** Cristian Andrés Basto Largo, Andrea Katherine Bello Sotelo, Laura Vanessa Figueredo Martinez