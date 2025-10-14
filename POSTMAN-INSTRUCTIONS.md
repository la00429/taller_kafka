# 📮 Guía de Pruebas con Postman - CQRS Microservices

## 🚀 Configuración Inicial

### 1. Importar Collection
1. Abre Postman
2. Haz clic en "Import"
3. Selecciona el archivo `CQRS-Postman-Tests.json`
4. La collection "CQRS Microservices Tests" aparecerá en tu workspace

### 2. Verificar Configuración
- **Base URL**: `http://localhost:8086` (API Gateway)
- **Puerto**: 8086 (Gateway que rutea a todos los microservicios)

## 🧪 Pruebas Disponibles

### **1. Crear Customer (Command Side)**
- **Método**: POST
- **URL**: `http://localhost:8086/api/customers`
- **Body**:
```json
{
  "document": "12345",
  "firstname": "Laura",
  "lastname": "Perez",
  "address": "Norte",
  "phone": "5123452",
  "email": "laura@test.com"
}
```
- **Resultado Esperado**: 
  - Customer creado en MySQL (Command Side)
  - Login auto-creado con contraseña `PWD_2345`
  - Eventos enviados a Kafka

### **2. Leer Customers (Query Side)**
- **Método**: GET
- **URL**: `http://localhost:8086/api/customers`
- **Resultado Esperado**: Lista de Customers desde MongoDB (Query Side)

### **3. Leer Customer por ID (Query Side)**
- **Método**: GET
- **URL**: `http://localhost:8086/api/customers/12345`
- **Resultado Esperado**: Customer específico desde MongoDB

### **4. Leer Logins (Query Side)**
- **Método**: GET
- **URL**: `http://localhost:8086/api/logins`
- **Resultado Esperado**: Lista de Logins (incluye auto-creados)

### **5. Crear Order (Command Side)**
- **Método**: POST
- **URL**: `http://localhost:8086/api/orders`
- **Body**:
```json
{
  "customerId": "12345",
  "productName": "Laptop",
  "quantity": 1,
  "price": 1500.0,
  "orderDate": "2024-01-15T10:00:00",
  "status": "PENDING"
}
```

### **6. Leer Orders (Query Side)**
- **Método**: GET
- **URL**: `http://localhost:8086/api/orders`

### **7. Actualizar Customer (Command Side)**
- **Método**: PUT
- **URL**: `http://localhost:8086/api/customers`

### **8. Eliminar Customer (Command Side)**
- **Método**: DELETE
- **URL**: `http://localhost:8086/api/customers/12345`

## 🔍 Lo que Deberías Ver en las Terminales

### **Command Side (Producers):**
```
[INFO] --- [customer-command] Started CustomerCommandApplication
Published addCustomer event: {"document":"12345","firstname":"Laura"...}
Published login creation event for customer: 12345
```

### **Query Side (Consumers):**
```
[INFO] --- [customer-query] Started CustomerQueryApplication
Received customer event with key: addCustomer, message: {"document":"12345"...}
Customer added to MongoDB: Customer(document=12345, firstname=Laura...)
Received login event with key: addLogin, message: {"customerId":"12345"...}
Login (auto-created) added to MongoDB: Login(id=12345, username=laura@test.com...)
```

## 📋 Secuencia de Pruebas Recomendada

### **Prueba 1: Crear Customer**
1. Ejecuta "1. Crear Customer (Command Side)"
2. Verifica en terminales que se publiquen eventos
3. Verifica que se auto-cree el Login

### **Prueba 2: Verificar Sincronización**
1. Ejecuta "2. Leer Customers (Query Side)"
2. Ejecuta "4. Leer Logins (Query Side)"
3. Verifica que los datos estén en MongoDB

### **Prueba 3: Operaciones CRUD**
1. Ejecuta "7. Actualizar Customer (Command Side)"
2. Ejecuta "2. Leer Customers (Query Side)" (verificar actualización)
3. Ejecuta "8. Eliminar Customer (Command Side)"
4. Ejecuta "2. Leer Customers (Query Side)" (verificar eliminación)

## 🎯 Validaciones Automáticas

Cada prueba incluye validaciones automáticas:
- ✅ Status code 200
- ✅ Response time < 5000ms
- ✅ Response no vacío

## 🚨 Solución de Problemas

### **Error de Conexión:**
- Verifica que todos los microservicios estén ejecutándose
- Verifica que Docker Compose esté corriendo
- Verifica que el API Gateway esté en puerto 8086

### **Error 404:**
- Verifica que el API Gateway esté ruteando correctamente
- Verifica que los microservicios estén en los puertos correctos

### **Error 500:**
- Verifica las bases de datos (MySQL y MongoDB)
- Verifica la conexión a Kafka
- Revisa los logs de los microservicios

## 📊 Datos de Prueba

### **Customers de Prueba:**
- **Laura Perez**: Documento 12345, Email laura@test.com
- **Cristian Basto**: Documento 67890, Email cristian@test.com  
- **Andrea Bello**: Documento 11111, Email andrea@test.com

### **Contraseñas Auto-generadas:**
- Laura: `PWD_2345` (últimos 4 dígitos del documento)
- Cristian: `PWD_7890`
- Andrea: `PWD_1111`

## 🔄 Flujo CQRS Verificado

1. **POST** → Command Side → MySQL → Kafka Event
2. **Kafka** → Query Side → MongoDB (Sincronización)
3. **GET** → Query Side → MongoDB (Lectura)

¡Disfruta probando la arquitectura CQRS! 🚀✨
