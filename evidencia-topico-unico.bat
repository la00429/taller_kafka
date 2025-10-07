@echo off
echo ========================================
echo    EVIDENCIA: TOPICO UNICO POR ENTIDAD
echo ========================================
echo.

echo 1. Iniciando Docker Compose (Kafka + MySQL)...
docker-compose up -d
timeout /t 10 /nobreak > nul

echo.
echo 2. Iniciando microservicios...
echo    - Customer Service (Puerto 8080)
start "Customer Service" cmd /k "cd edamicrokafka && mvn spring-boot:run"

timeout /t 15 /nobreak > nul

echo    - Login Service (Puerto 8081)
start "Login Service" cmd /k "cd edamicrokafka-login && mvn spring-boot:run"

timeout /t 15 /nobreak > nul

echo    - Order Service (Puerto 8082)
start "Order Service" cmd /k "cd edamicrokafka-order && mvn spring-boot:run"

timeout /t 15 /nobreak > nul

echo    - API Gateway (Puerto 8083)
start "API Gateway" cmd /k "cd edamicrokafka-gateway && mvn spring-boot:run"

timeout /t 20 /nobreak > nul

echo.
echo ========================================
echo    PRUEBAS PARA EVIDENCIA DE TOPICO UNICO
echo ========================================
echo.

echo 3. EVIDENCIA: Un solo topico 'customer_events' maneja TODOS los eventos de Customer
echo.
echo    a) Creando Customer (genera evento 'addCustomer' en topico 'customer_events')...
curl -X POST http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\": \"19273\", \"firstname\": \"Laura\", \"lastname\": \"Perez\", \"address\": \"Norte\", \"phone\": \"5123452\", \"email\": \"pepito@c.com\"}"

echo.
echo    b) Buscando Customer por ID (genera evento 'findCustomerById' en MISMO topico 'customer_events')...
curl -X GET http://localhost:8080/api/customers/19273

echo.
echo    c) Obteniendo todos los Customers (genera evento 'findAllCustomers' en MISMO topico 'customer_events')...
curl -X GET http://localhost:8080/api/customers

echo.
echo    d) Actualizando Customer (genera evento 'editCustomer' en MISMO topico 'customer_events')...
curl -X PUT http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\": \"19273\", \"firstname\": \"Laura\", \"lastname\": \"Perez Updated\", \"address\": \"Sur\", \"phone\": \"5123452\", \"email\": \"pepito@c.com\"}"

echo.
echo 4. EVIDENCIA: Un solo topico 'login_events' maneja TODOS los eventos de Login
echo.
echo    a) Verificando Login creado automaticamente (genera evento 'addLogin' en topico 'login_events')...
curl -X GET http://localhost:8081/api/logins

echo.
echo 5. EVIDENCIA: Un solo topico 'order_events' maneja TODOS los eventos de Order
echo.
echo    a) Creando Order (genera evento 'addOrder' en topico 'order_events')...
curl -X POST http://localhost:8082/api/orders ^
  -H "Content-Type: application/json" ^
  -d "{\"customerId\": \"19273\", \"orderDate\": \"2025-10-06\", \"totalAmount\": 150.00, \"status\": \"PENDING\"}"

echo.
echo    b) Obteniendo todos los Orders (genera evento 'findAllOrders' en MISMO topico 'order_events')...
curl -X GET http://localhost:8082/api/orders

echo.
echo ========================================
echo    EVIDENCIAS CLAVE A CAPTURAR:
echo ========================================
echo.
echo 1. CONSOLA Customer Service: Debe mostrar eventos con claves diferentes:
echo    - "Received customer event with key: addCustomer"
echo    - "Received customer event with key: findCustomerById" 
echo    - "Received customer event with key: findAllCustomers"
echo    - "Received customer event with key: editCustomer"
echo    - TODOS en el MISMO topico: customer_events
echo.
echo 2. CONSOLA Login Service: Debe mostrar:
echo    - "Received login event with key: addLogin"
echo    - En el MISMO topico: login_events
echo.
echo 3. CONSOLA Order Service: Debe mostrar:
echo    - "Received order event with key: addOrder"
echo    - "Received order event with key: findAllOrders"
echo    - TODOS en el MISMO topico: order_events
echo.
echo 4. CODIGO: Mostrar CustomerEventConsumer.java con switch de claves
echo 5. CODIGO: Mostrar CustomerEventProducer.java con envio de claves diferentes
echo 6. DOCKER-COMPOSE: Mostrar creacion de topicos unificados
echo.
echo ========================================
echo    EVIDENCIA COMPLETADA
echo ========================================
echo.
echo Presiona cualquier tecla para cerrar...
pause > nul
