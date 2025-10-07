@echo off
echo ========================================
echo    EVIDENCIAS PUNTOS 3, 4 y 5
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
echo    PRUEBAS PARA EVIDENCIAS
echo ========================================
echo.

echo 3. Probando PUNTO 3: Creacion automatica de Login...
echo    Creando Customer (debe crear Login automaticamente)
curl -X POST http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"document\": \"19273\", \"firstname\": \"Laura\", \"lastname\": \"Perez\", \"address\": \"Norte\", \"phone\": \"5123452\", \"email\": \"pepito@c.com\"}"

echo.
echo    Verificando que se creo el Login...
curl -X GET http://localhost:8081/api/logins

echo.
echo 4. Probando PUNTO 4: Topicos unificados...
echo    Probando Customer Service con topicos unificados...
curl -X GET http://localhost:8080/api/customers

echo.
echo 5. Probando PUNTO 5: API Gateway...
echo    Probando acceso a traves del Gateway...
curl -X GET http://localhost:8083/api/customers

echo.
echo ========================================
echo    EVIDENCIAS COMPLETADAS
echo ========================================
echo.
echo Presiona cualquier tecla para cerrar...
pause > nul
