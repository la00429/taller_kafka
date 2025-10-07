# EDA Microservices Architecture

Este proyecto implementa una arquitectura de microservicios basada en Event-Driven Architecture (EDA) utilizando Apache Kafka y Spring Boot.

## 🏗️ Arquitectura

### Microservicios
- **Customer Service** (Puerto 8080): Gestión de clientes
- **Login Service** (Puerto 8081): Gestión de autenticación
- **Order Service** (Puerto 8082): Gestión de pedidos
- **API Gateway** (Puerto 8083): Punto único de acceso

### Infraestructura
- **MySQL**: Base de datos principal
- **Apache Kafka**: Sistema de mensajería para eventos
- **Zookeeper**: Coordinador de Kafka

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- Docker y Docker Compose
- MySQL (opcional, se incluye en Docker)

### 1. Iniciar la Infraestructura
```bash
docker-compose up -d
```

### 2. Ejecutar los Microservicios

#### Customer Service
```bash
cd edamicrokafka
mvn spring-boot:run
```

#### Login Service
```bash
cd edamicrokafka-login
mvn spring-boot:run
```

#### Order Service
```bash
cd edamicrokafka-order
mvn spring-boot:run
```

#### API Gateway
```bash
cd edamicrokafka-gateway
mvn spring-boot:run
```

### 3. Probar los Servicios
```bash
# Ejecutar script de pruebas
./test-microservices.bat
```

## 📡 API Endpoints

### A través del API Gateway (Puerto 8083)

#### Customer Service
- `GET /api/customers` - Obtener todos los clientes
- `POST /api/customers` - Crear cliente
- `GET /api/customers/{id}` - Obtener cliente por ID
- `PUT /api/customers/{id}` - Actualizar cliente
- `DELETE /api/customers/{id}` - Eliminar cliente

#### Login Service
- `GET /api/logins` - Obtener todos los logins
- `POST /api/logins` - Crear login
- `GET /api/logins/{id}` - Obtener login por ID
- `GET /api/logins/customer/{customerId}` - Obtener login por cliente
- `GET /api/logins/username/{username}` - Obtener login por usuario

#### Order Service
- `GET /api/orders` - Obtener todos los pedidos
- `POST /api/orders` - Crear pedido
- `GET /api/orders/{id}` - Obtener pedido por ID
- `GET /api/orders/customer/{customerId}` - Obtener pedidos por cliente
- `GET /api/orders/status/{status}` - Obtener pedidos por estado

## 🔄 Event-Driven Architecture

### Tópicos de Kafka
- `customer_events`: Eventos de clientes (addCustomer, editCustomer, findCustomerById, findAllCustomers)
- `login_events`: Eventos de autenticación (addLogin, editLogin, findLoginById, findAllLogins)
- `order_events`: Eventos de pedidos (addOrder, editOrder, findOrderById, findAllOrders)

### Flujo de Eventos
1. **Creación de Cliente**: Cuando se crea un cliente, automáticamente se crea un registro de login
2. **Tópicos Únicos**: Cada entidad usa un solo tópico con diferentes claves de evento
3. **Desacoplamiento**: Los microservicios se comunican solo a través de eventos

## 🗄️ Base de Datos

### Tablas
- `customers`: Información de clientes
- `logins`: Credenciales de autenticación
- `orders`: Pedidos
- `order_items`: Items de pedidos

### Configuración
- **Host**: localhost:3306
- **Database**: customerorders
- **Usuario**: customerorders
- **Contraseña**: corders123.

## 🧪 Pruebas

### Prueba Manual
1. Crear un cliente:
```bash
curl -X POST http://localhost:8083/api/customers \
  -H "Content-Type: application/json" \
  -d '{"document":"12345678","name":"Juan Perez","email":"juan@example.com","phone":"3001234567"}'
```

2. Verificar que se creó el login automáticamente:
```bash
curl -X GET http://localhost:8083/api/logins
```

3. Crear un pedido:
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"orderNumber":"ORD-001","totalAmount":100.50,"shippingAddress":"Calle 123","billingAddress":"Calle 123"}'
```

## 📊 Monitoreo

### Logs de Kafka
Los eventos se registran en la consola de cada microservicio. Busca mensajes como:
- `Published addCustomer event`
- `Received customer event with key: addCustomer`
- `Customer added successfully`

### Verificar Tópicos
```bash
docker exec -it edakafka-kafka-1 kafka-topics --list --bootstrap-server localhost:9092
```

## 🔧 Configuración Avanzada

### Variables de Entorno
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Servidores de Kafka
- `SPRING_DATASOURCE_URL`: URL de la base de datos
- `SERVER_PORT`: Puerto del microservicio

### Personalización de Eventos
Los eventos se pueden personalizar modificando las clases `*EventProducer` y `*EventConsumer` en cada microservicio.

## 🚨 Solución de Problemas

### Puerto en Uso
Si un puerto está en uso, modifica el `application.properties` del microservicio correspondiente.

### Kafka No Conecta
Verifica que Docker esté ejecutándose y que los contenedores estén activos:
```bash
docker ps
```

### Base de Datos
Si hay problemas de conexión, verifica que MySQL esté ejecutándose y que las credenciales sean correctas.

## 📝 Commits Realizados

1. **feat: Add Login and Order microservices with EDA pattern** - Creación de microservicios Login y Order
2. **feat: Implement unified topics and API Gateway** - Implementación de tópicos únicos y API Gateway

## 👥 Contribuidores

- Laura - Desarrollo de la arquitectura EDA
- Asistente IA - Implementación de microservicios
