# 📋 Instrucciones de Pruebas con Postman

## 🚀 Configuración Inicial

### 1. Importar Colección
- Importa el archivo `CQRS-Postman-Tests.json` en Postman
- Configura el environment con la variable `base_url` = `http://localhost:8086`

### 2. Iniciar Servicios
```bash
# Iniciar infraestructura
docker-compose up -d

# Iniciar microservicios (en terminales separadas)
cd command-side/customer-command && mvn spring-boot:run
cd command-side/login-command && mvn spring-boot:run
cd command-side/order-command && mvn spring-boot:run
cd query-side/customer-query && mvn spring-boot:run
cd query-side/login-query && mvn spring-boot:run
cd query-side/order-query && mvn spring-boot:run
cd cqrs-gateway && mvn spring-boot:run
```

## 🧪 Pruebas de Funcionalidad

### 1. Prueba de Auto-creación de Login

#### Paso 1: Crear Customer
```http
POST {{base_url}}/api/customers
Content-Type: application/json

{
  "document": "12345",
  "firstname": "Laura",
  "lastname": "Perez",
  "address": "Norte",
  "phone": "5123452",
  "email": "laura@test.com"
}
```

**Resultado esperado:**
- Customer creado en MySQL (Command Side)
- Login auto-creado con contraseña generada
- Eventos enviados a Kafka

#### Paso 2: Verificar Customer en Query Side
```http
GET {{base_url}}/api/customers/12345
```

**Resultado esperado:**
- Customer visible en MongoDB (Query Side)
- Datos sincronizados correctamente

#### Paso 3: Verificar Login Auto-creado
```http
GET {{base_url}}/api/logins
```

**Resultado esperado:**
- Login visible en MongoDB (Query Side)
- Contraseña generada automáticamente
- CustomerId coincidente con el Customer creado

### 2. Prueba de Sincronización Command/Query

#### Crear Login Manual
```http
POST {{base_url}}/api/logins
Content-Type: application/json

{
  "id": "login_67890",
  "customerId": "67890",
  "username": "test.user",
  "password": "password123",
  "email": "test@example.com"
}
```

#### Verificar en Query Side
```http
GET {{base_url}}/api/logins/login_67890
```

### 3. Prueba de Orders

#### Crear Order
```http
POST {{base_url}}/api/orders
Content-Type: application/json

{
  "id": "order_001",
  "customerId": "12345",
  "productName": "Laptop",
  "price": 1500.00,
  "quantity": 1,
  "orderDate": "2025-01-13T10:30:00"
}
```

#### Verificar Order en Query Side
```http
GET {{base_url}}/api/orders/order_001
```

## 🔍 Verificaciones Adicionales

### 1. Verificar Logs de Kafka
- Revisar consolas de microservicios para eventos de Kafka
- Verificar que los eventos se procesan correctamente

### 2. Verificar Bases de Datos
- **MySQL**: Verificar datos en `cqrs_commands` database
- **MongoDB**: Verificar datos en `cqrs_queries` database

### 3. Verificar API Gateway
- Todas las peticiones deben pasar por puerto 8086
- Enrutamiento automático Command/Query según método HTTP

## 📊 Colección de Pruebas

La colección incluye:

1. **Customer Tests**
   - POST /api/customers (crear)
   - PUT /api/customers (actualizar)
   - DELETE /api/customers/{id} (eliminar)
   - GET /api/customers (listar todos)
   - GET /api/customers/{id} (obtener por ID)

2. **Login Tests**
   - POST /api/logins (crear)
   - PUT /api/logins (actualizar)
   - DELETE /api/logins/{id} (eliminar)
   - GET /api/logins (listar todos)
   - GET /api/logins/{id} (obtener por ID)

3. **Order Tests**
   - POST /api/orders (crear)
   - PUT /api/orders (actualizar)
   - DELETE /api/orders/{id} (eliminar)
   - GET /api/orders (listar todos)
   - GET /api/orders/{id} (obtener por ID)

## ⚠️ Notas Importantes

1. **Puertos**: Asegúrate de que todos los microservicios estén ejecutándose
2. **Bases de Datos**: MySQL y MongoDB deben estar disponibles
3. **Kafka**: Debe estar ejecutándose para la sincronización
4. **Orden**: Ejecuta las pruebas en el orden indicado para mejor resultado

## 🐛 Solución de Problemas

### Error de Conexión
- Verificar que todos los servicios estén ejecutándose
- Verificar configuración de puertos

### Error de Sincronización
- Verificar logs de Kafka
- Verificar configuración de bases de datos

### Error de Auto-creación
- Verificar que Customer se creó correctamente
- Verificar logs del CustomerEventProducer