@echo off
echo ========================================
echo    EVIDENCIA: API GATEWAY CON NETFLIX ZUUL
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

echo    - API Gateway con NETFLIX ZUUL (Puerto 8083)
start "Netflix Zuul Gateway" cmd /k "cd edamicrokafka-gateway && mvn spring-boot:run"

timeout /t 20 /nobreak > nul

echo.
echo ========================================
echo    PRUEBAS PARA EVIDENCIA DE NETFLIX ZUUL
echo ========================================
echo.

echo 3. EVIDENCIA: Netflix Zuul como puerta de entrada única
echo.
echo    a) Acceso DIRECTO a Customer Service (puerto 8080)...
curl -X GET http://localhost:8080/api/customers

echo.
echo    b) Acceso a TRAVES DEL GATEWAY ZUUL (puerto 8083)...
curl -X GET http://localhost:8083/api/customers

echo.
echo    c) Acceso DIRECTO a Login Service (puerto 8081)...
curl -X GET http://localhost:8081/api/logins

echo.
echo    d) Acceso a TRAVES DEL GATEWAY ZUUL (puerto 8083)...
curl -X GET http://localhost:8083/api/logins

echo.
echo    e) Acceso DIRECTO a Order Service (puerto 8082)...
curl -X GET http://localhost:8082/api/orders

echo.
echo    f) Acceso a TRAVES DEL GATEWAY ZUUL (puerto 8083)...
curl -X GET http://localhost:8083/api/orders

echo.
echo 4. EVIDENCIA: Crear Customer a traves del Gateway Zuul
echo.
echo    Creando Customer via Gateway (debe crear Login automaticamente)...
curl -X POST http://localhost:8083/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\": \"19273\", \"firstname\": \"Laura\", \"lastname\": \"Perez\", \"address\": \"Norte\", \"phone\": \"5123452\", \"email\": \"pepito@c.com\"}"

echo.
echo    Verificando Login creado via Gateway...
curl -X GET http://localhost:8083/api/logins

echo.
echo ========================================
echo    EVIDENCIAS CLAVE A CAPTURAR:
echo ========================================
echo.
echo 1. CONSOLA Gateway Zuul: Debe mostrar:
echo    - "Started EdamicrokafkaGatewayApplication"
echo    - "@EnableZuulProxy" annotation activa
echo    - Logs de Zuul procesando requests
echo.
echo 2. CONFIGURACION: Mostrar application.yml con:
echo    - zuul.routes configuration
echo    - customer-service, login-service, order-service routes
echo    - stripPrefix: false
echo.
echo 3. CODIGO: Mostrar EdamicrokafkaGatewayApplication.java con:
echo    - @EnableZuulProxy annotation
echo    - @SpringBootApplication
echo.
echo 4. FILTRO: Mostrar CorsFilter.java con:
echo    - extends ZuulFilter
echo    - filterType(), filterOrder(), shouldFilter(), run()
echo.
echo 5. PRUEBAS: Mostrar que:
echo    - http://localhost:8083/api/customers funciona
echo    - http://localhost:8083/api/logins funciona  
echo    - http://localhost:8083/api/orders funciona
echo    - Todas las rutas redirigen a microservicios correctos
echo.
echo 6. LOGS ZUUL: Mostrar logs de Zuul procesando requests:
echo    - "ZuulFilter" logs
echo    - "Route" logs
echo    - "Proxy" logs
echo.
echo ========================================
echo    EVIDENCIA COMPLETADA
echo ========================================
echo.
echo Presiona cualquier tecla para cerrar...
pause > nul
